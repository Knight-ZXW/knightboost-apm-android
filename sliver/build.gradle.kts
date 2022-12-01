import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.library")
    kotlin("android")
}


android {
    compileSdk = Config.Android.compileSdkVersion

    defaultConfig {
        targetSdk = Config.Android.targetSdkVersion
        minSdk = Config.Android.minSdkVersionNdk
        testInstrumentationRunner = Config.TestLibs.androidJUnitRunner
    }

    buildTypes {
        getByName("debug")
        getByName("release") {
            consumerProguardFiles("proguard-rules.pro")
        }
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

}

configure<JavaPluginExtension> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {

    testImplementation(kotlin(Config.kotlinStdLib, KotlinCompilerVersion.VERSION))
    testImplementation(Config.TestLibs.robolectric)
    testImplementation(Config.TestLibs.kotlinTestJunit)
}