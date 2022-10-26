package com.knightboost.android.gradle.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFile
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.*
import java.io.File

abstract class UploadProguardMappingsTask : DefaultTask() {

    @get:Input abstract val autoUploadProguardMapping: Property<Boolean>

    @get:Input abstract val asyncUpload: Property<Boolean>

    @get:InputDirectory
    abstract val uuidDirectory: DirectoryProperty

    @get:InputFile
    abstract val mappingFiles: RegularFileProperty

    @get:Internal
    val uuidFile: Provider<RegularFile>
        get() = uuidDirectory.file("knight-apm-debug-meta.properties")


    @TaskAction
    public fun upload():Boolean{
        logger.warn("开始上传mapping文件")
        val uuid = readUuidFromFile(uuidFile.get().asFile)
        val isPresent = mappingFiles.isPresent
        logger.warn("mapping文件存在，地址为 "+mappingFiles.asFile.get().absolutePath)
        return  true
    }

    companion object {
        private const val PROPERTY_PREFIX = "apm.ProguardUuids="

        internal fun readUuidFromFile(file: File): String {
            check(file.exists()) {
                "UUID properties file is missing"
            }
            val content = file.readText().trim()
            check(content.startsWith(PROPERTY_PREFIX)) {
                "io.sentry.ProguardUuids property is missing"
            }
            return content.removePrefix(PROPERTY_PREFIX)
        }
    }

}