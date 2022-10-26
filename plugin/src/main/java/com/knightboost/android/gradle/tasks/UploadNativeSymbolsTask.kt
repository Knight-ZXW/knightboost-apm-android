package com.knightboost.android.gradle.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*

abstract class UploadNativeSymbolsTask :DefaultTask(){

    @get:InputDirectory
    abstract val buildDir: DirectoryProperty

    @get:Input
    abstract val autoUploadNativeSymbol: Property<Boolean>

    @get:Input
    abstract val includeNativeSources: Property<Boolean>

    @get:Internal
    abstract val variantName: Property<String>




}