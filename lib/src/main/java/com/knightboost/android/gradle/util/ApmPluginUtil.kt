package com.knightboost.android.gradle.util

import com.android.build.gradle.api.ApplicationVariant
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.logging.Logger
import org.gradle.api.tasks.TaskProvider
import java.util.*

internal object ApmPluginUtil {
    fun withLogging(
        logger: Logger,
        varName: String,
        initializer: () -> TaskProvider<Task>?
    ) = initializer().also {
        logger.info { "$varName is ${it?.name}" }
    }

    fun String.capitalizeUS() = if (isEmpty()) {
        ""
    } else {
        substring(0, 1).toUpperCase(Locale.US) + substring(1)
    }

    fun isMinificationEnabled(
        project: Project,
        variant: ApplicationVariant,
    ):Boolean{
        return variant.buildType.isMinifyEnabled
    }
}