package com.knightboost.android.gradle.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.*
import java.util.*

abstract class GenerateProguardUuidTask : DefaultTask() {
    init {
        outputs.upToDateWhen { false }
        description = "Generates a unique build ID to be used "+
                "when uploading the MoonLight mapping file"
    }

    @get:OutputDirectory abstract val outputDirectory: DirectoryProperty

    @get:Internal val outputFile: Provider<RegularFile>
        get() = outputDirectory.file(
            "knight-apm-debug-meta.properties"
        )

    @TaskAction fun generateProperties() {

        logger.info("KnightAPMGenerateProguardUuidTask - outputFile: ${outputFile.get()}")

        UUID.randomUUID().also {
            outputFile.get().asFile.parentFile.mkdirs()
            outputFile.get().asFile.writeText("apm.ProguardUuids=$it")
        }
    }
}