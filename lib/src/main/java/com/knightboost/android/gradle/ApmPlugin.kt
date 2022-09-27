package com.knightboost.android.gradle

import com.android.build.gradle.AppExtension
import com.android.build.gradle.api.ApplicationVariant
import com.knightboost.android.gradle.ApmPropertiesFileProvider.getPropertiesFilePath
import com.knightboost.android.gradle.KnightTasksProvider.getBundleTask
import com.knightboost.android.gradle.KnightTasksProvider.getMergeAssetsProvider
import com.knightboost.android.gradle.KnightTasksProvider.getPackageBundleTask
import com.knightboost.android.gradle.KnightTasksProvider.getPackageProvider
import com.knightboost.android.gradle.KnightTasksProvider.getPreBundleTask
import com.knightboost.android.gradle.KnightTasksProvider.getTransformerTask
import com.knightboost.android.gradle.extensions.ApmPluginExtension
import com.knightboost.android.gradle.tasks.*
import com.knightboost.android.gradle.util.AgpVersions
import com.knightboost.android.gradle.util.ApmPluginUtil.capitalizeUS
import com.knightboost.android.gradle.util.ApmPluginUtil.isMinificationEnabled
import com.knightboost.android.gradle.util.ApmPluginUtil.withLogging
import com.knightboost.android.gradle.util.info
import org.gradle.api.*
import org.gradle.api.tasks.StopExecutionException
import org.gradle.api.tasks.TaskProvider
import org.slf4j.LoggerFactory
import java.io.File

class ApmPlugin : Plugin<Project> {
    companion object{
        internal val sep = File.separator

        // a single unified logger used by instrumentation
        internal val logger by lazy {
            LoggerFactory.getLogger(ApmPlugin::class.java)
        }
    }

    override fun apply(project: Project) {
//        if (AgpVersions.CURRENT < AgpVersions.VERSION_7_0_0) {
//            //TODO 完善不支持的提示
//            throw StopExecutionException(
//                """
//                    Not Support
//                """.trimIndent()
//            )
//        }

        val extension = project.extensions.create(
            "KnightBoostAPM", ApmPluginExtension::class.java, project
        )
        project.pluginManager.withPlugin("com.android.application"){
            val androidExtension= project.extensions.getByType(AppExtension::class.java)

            androidExtension.applicationVariants.matching{
               return@matching isVariantAllowed(extension,it.name,it.flavorName,it.buildType.name)
            }.configureEach(object :Action<ApplicationVariant>{
                override fun execute(variant: ApplicationVariant) {
                    val bundleTask = withLogging(project.logger, "bundleTask") {
                        getBundleTask(project, variant.name)
                    }

                    val sentryProperties = getPropertiesFilePath(project, variant)
                    val isMinificationEnabled = isMinificationEnabled(
                        project,
                        variant
                    )

                    val isDebuggable = variant.buildType.isDebuggable

                    var preBundleTaskProvider: TaskProvider<Task>? = null
                    var transformerTaskProvider: TaskProvider<Task>? = null
                    var packageBundleTaskProvider: TaskProvider<Task>? = null

                    if (isMinificationEnabled) {
                        preBundleTaskProvider = withLogging(project.logger, "preBundleTask") {
                            getPreBundleTask(project, variant.name)
                        }
                        transformerTaskProvider = withLogging(project.logger, "transformerTask") {
                            getTransformerTask(
                                project,
                                variant.name
                            )
                        }
                        packageBundleTaskProvider = withLogging(project.logger, "packageBundleTask") {
                            getPackageBundleTask(project, variant.name)
                        }
                    } else {
                        project.logger.info {
                            "Minification is not enabled for variant ${variant.name}."
                        }
                    }

                    val taskSuffix = variant.name.capitalizeUS()
                    if (isMinificationEnabled && extension.includeProguardMapping.get()){
                        // Set up the task to generate a UUID asset file
                       val generateUuidTask =  project.tasks.register(
                            "generateKnightAPMProguardUuid$taskSuffix",
                            GenerateProguardUuidTask::class.java
                        ){
                            this.outputDirectory.set(
                                project.file(
                                    File(
                                        project.buildDir,
                                        "generated${sep}assets${sep}knightApm${sep}${variant.name}"
                                    )
                                )
                            )
                        }
                        getMergeAssetsProvider(variant)?.configure {
                            this.dependsOn(generateUuidTask)
                        }

                        // Setup the task that uploads the proguard mapping and UUIDs

                        val uploadProguardMappingsTask = project.tasks.register(
                            "uploadApmProguardMappings${taskSuffix}",
                            UploadProguardMappingsTask::class.java
                        ){
                            this.dependsOn(generateUuidTask)
                            this.asyncUpload.set(true)
                            this.uuidDirectory.set(generateUuidTask.flatMap(GenerateProguardUuidTask::outputDirectory))
                            this.mappingFiles.set(KnightTasksProvider.getMappingFile(project,variant))
                            this.autoUploadProguardMapping.set(extension.autoUploadProguardMapping)
                            //TODO appKey
                        }

                        // add uuid file to asset
                        androidExtension.sourceSets.getByName(variant.name).assets.srcDir(
                            generateUuidTask.flatMap { it.outputDirectory }
                        )
                        transformerTaskProvider?.configure{
                            this.finalizedBy(uploadProguardMappingsTask)
                        }

                        //To include proguard uuid file into aab, run before bundle task.
                        preBundleTaskProvider?.configure {
                            this.dependsOn(generateUuidTask)
                        }
                        //The package task will only be executed if the generateUuidTask has already been executed.
                        getPackageProvider(variant)?.configure {
                            this.dependsOn(generateUuidTask)
                        }
                        // App bundle has different package task
                        packageBundleTaskProvider?.configure {
                            this.dependsOn(generateUuidTask)
                        }
                    } else {
                        //Minification is disabled
                        logger.warn("当前构建 minify 为关闭")

                    }

                    // only debug symbols of  on debuggable code should be uploaded (aka release builds).
                    // uploadNativeSymbols task will only be executed after the assemble task
                    // and also only if `uploadNativeSymbols` is enabled, as this is an opt-in feature.
                    if (!isDebuggable && extension.autoUploadNativeSymbols.get()){
                        //TODO upload NativeSymbols
                        project.tasks.register(
                            "uploadApmNativeSymbolsFor$taskSuffix",
                            UploadNativeSymbolsTask::class.java
                        ){
                            this.buildDir.set(project.buildDir)
                            this.autoUploadNativeSymbol.set(extension.autoUploadNativeSymbols)
                            this.variantName.set(variantName)
                        }
                    }
                }
            })


        }
    }



    private fun isVariantAllowed(
        extension: ApmPluginExtension,
        variantName: String,
        flavorName: String?,
        buildType: String?
    ): Boolean {
        return variantName !in extension.ignoredVariants.get() &&
                flavorName !in extension.ignoredFlavors.get() &&
                buildType !in extension.ignoredBuildTypes.get()
    }

}