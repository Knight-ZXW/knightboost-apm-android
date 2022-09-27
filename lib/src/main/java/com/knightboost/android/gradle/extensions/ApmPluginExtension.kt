package com.knightboost.android.gradle.extensions

import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import javax.inject.Inject

abstract class ApmPluginExtension @Inject constructor(project: Project) {
    private val objects = project.objects

    /**
     * Enable or Disable the handling of Proguard mapping for APM System.
     * If enabled the plugin will generate a UUID and will take card of
     * uploading the mapping to APM System. If disabled, all the logic
     * related to proguard mappng will be excluded.
     * Default is enabled.
     */
    val includeProguardMapping: Property<Boolean> = objects.property(Boolean::class.java).convention(true)

    /**
     * Whether the plugin should attempt to auto-upload the mapping file to APM Server or not.
     * If disabled the plugin will run a dry-run.
     * Default is enabled.
     */
    val autoUploadProguardMapping:Property<Boolean> = objects.property(Boolean::class.java).convention(true)

    /**
     * Whether the plugin should attempt to auto-upload the Native Symbols file to APM Server or not.
     * If disabled the plugin will run a dry-run.
     * Default is enabled.
     */
    val autoUploadNativeSymbols: Property<Boolean> =
        objects.property(Boolean::class.java).convention(true)

    /** List of Android build variants that should be ignored by the APM plugin. */
    val ignoredVariants: SetProperty<String> = objects.setProperty(String::class.java)
        .convention(emptySet())

    /** List of Android build types that should be ignored by the APM plugin. */
    val ignoredBuildTypes: SetProperty<String> = objects.setProperty(String::class.java)
        .convention(emptySet())

    /** List of Android build flavors that should be ignored by the APM plugin. */
    val ignoredFlavors: SetProperty<String> = objects.setProperty(String::class.java)
        .convention(emptySet())

    /**
     * Experimental flag to turn on support for GuardSquare's tools integration (Dexguard and External Proguard).
     * If enabled, the plugin will try to consume and upload the mapping file
     * produced by Dexguard and External Proguard.
     * Default is disabled.
     */
    val experimentalGuardsquareSupport: Property<Boolean> = objects
        .property(Boolean::class.java).convention(false)
}