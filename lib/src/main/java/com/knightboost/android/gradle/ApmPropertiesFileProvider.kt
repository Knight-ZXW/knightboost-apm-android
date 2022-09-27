package com.knightboost.android.gradle

import com.android.build.gradle.api.ApplicationVariant
import com.knightboost.android.gradle.util.info
import org.gradle.api.Project
import org.jetbrains.kotlin.konan.file.File

internal object ApmPropertiesFileProvider {
    private const val FILENAME = "knight-apm.properties"

    /**
     * Find knightAPM.properties and return path to the file
     */
    fun getPropertiesFilePath(project: Project,variant:ApplicationVariant):String?{
        val flavorName = variant.flavorName
        val buildTypeName = variant.buildType.name

        val projDir = project.projectDir
        val rootDir = project.rootDir

        val sep = File.separator

        // Local Project dirs
        val possibleFiles = mutableListOf(
            "${projDir}${sep}src${sep}${buildTypeName}${sep}$FILENAME"
        )
        if (flavorName.isNotBlank()) {
            possibleFiles.add(
                "${projDir}${sep}src${sep}${buildTypeName}${sep}$flavorName${sep}$FILENAME"
            )
            possibleFiles.add(
                "${projDir}${sep}src${sep}${flavorName}${sep}${buildTypeName}${sep}$FILENAME"
            )
            possibleFiles.add("${projDir}${sep}src${sep}${flavorName}${sep}$FILENAME")
        }

        possibleFiles.add("${projDir}${sep}$FILENAME")

        // Other flavors dirs
        possibleFiles.addAll(
            variant.productFlavors.map { "${projDir}${sep}src${sep}${it.name}${sep}$FILENAME" }
        )

        // Root project dirs
        possibleFiles.add("${rootDir}${sep}src${sep}${buildTypeName}${sep}$FILENAME")
        if (flavorName.isNotBlank()) {
            possibleFiles.add("${rootDir}${sep}src${sep}${flavorName}${sep}$FILENAME")
            possibleFiles.add(
                "${rootDir}${sep}src${sep}${buildTypeName}${sep}${flavorName}${sep}$FILENAME"
            )
            possibleFiles.add(
                "${rootDir}${sep}src${sep}${flavorName}${sep}${buildTypeName}${sep}$FILENAME"
            )
        }

        possibleFiles.add("${rootDir}${sep}$FILENAME")
        return possibleFiles.distinct().asSequence()
            .onEach { project.logger.info { "Looking for $FILENAME at: $it" } }
            .firstOrNull { java.io.File(it).exists() }
            ?.also { project.logger.info { "Found $FILENAME at: $it" } }
    }
}