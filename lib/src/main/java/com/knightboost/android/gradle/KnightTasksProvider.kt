package com.knightboost.android.gradle

import com.android.build.gradle.api.ApplicationVariant
import com.android.build.gradle.tasks.MergeSourceSetFolders
import com.android.build.gradle.tasks.PackageAndroidArtifact
import com.knightboost.android.gradle.util.ApmPluginUtil.capitalizeUS
import org.gradle.api.*
import org.gradle.api.tasks.TaskProvider
import java.io.File

internal object KnightTasksProvider {

    /**
     * Returns the transformer task for the given project and variant.
     * It could be either ProGuard or R8
     *
     * @return the task or null otherwise
     */
    @JvmStatic
    fun getTransformerTask(
        project: Project,
        variantName: String
    ): TaskProvider<Task>? {
        val taskList = mutableListOf<String>()
        // AGP 3.3 includes the R8 shrinker.
        taskList.add("minify${variantName.capitalized}WithR8")
        taskList.add("minify${variantName.capitalized}WithProguard")
        return project.findTask(taskList)
    }

    /**
     * Returns the pre bundle task for the given project and variant.
     *
     * @return the task or null otherwise
     */
    @JvmStatic
    fun getPreBundleTask(project: Project, variantName: String): TaskProvider<Task>? =
        project.findTask(listOf("build${variantName.capitalized}PreBundle"))

    /**
     * Returns the pre bundle task for the given project and variant.
     *
     * @return the task or null otherwise
     */
    @JvmStatic
    fun getBundleTask(project: Project, variantName: String): TaskProvider<Task>? =
        project.findTask(listOf("bundle${variantName.capitalized}"))

    /**
     * Returns the package bundle task (App Bundle only)
     *
     * @return the package task or null if not found
     */
    @JvmStatic
    fun getPackageBundleTask(project: Project, variantName: String): TaskProvider<Task>? =
        // for APK it uses getPackageProvider
        project.findTask(listOf("package${variantName.capitalized}Bundle"))

    /**
     * Returns the assemble task provider
     *
     * @return the provider if found or null otherwise
     */
    @JvmStatic
    fun getAssembleTaskProvider(variant: ApplicationVariant): TaskProvider<Task>? =
        variant.assembleProvider

    /**
     * Returns the merge asset provider
     *
     * @return the provider if found or null otherwise
     */
    @JvmStatic
    fun getMergeAssetsProvider(variant: ApplicationVariant): TaskProvider<MergeSourceSetFolders>? =
        variant.mergeAssetsProvider


    @JvmStatic
    fun getMappingFile(
        project: Project,
        variant: ApplicationVariant
    ): File? {
        return variant.mappingFile
    }

    /**
     * Returns the package provider
     *
     * @return the provider if found or null otherwise
     */
    @JvmStatic
    fun getPackageProvider(variant: ApplicationVariant):TaskProvider<PackageAndroidArtifact>? =
        // for App Bundle it uses getPackageBundleTask
        variant.packageApplicationProvider


    private fun Project.findTask(taskName: List<String>): TaskProvider<Task>? =
        taskName
            .mapNotNull {
                try {
                    project.tasks.named(it)
                } catch (e: UnknownTaskException) {
                    null
                }
            }
            .firstOrNull()

    internal val String.capitalized: String get() = this.capitalizeUS()

}