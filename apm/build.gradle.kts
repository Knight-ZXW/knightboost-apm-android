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


    // Import the BoM for the Firebase platform
//    implementation platform('com.google.firebase:firebase-bom:28.4.2')
//    // Declare the dependencies for the Crashlytics and Analytics libraries
//    // When using the BoM, you don't specify versions in Firebase library dependencies
//    implementation 'com.google.firebase:firebase-crashlytics'
//    implementation 'com.google.firebase:firebase-perf'
//    implementation 'com.google.firebase:perf-plugin'
//    implementation 'com.google.firebase:firebase-analytics'
    implementation("com.squareup.curtains:curtains:1.2.4")
    compileOnly(Config.Libs.fragment)
    implementation(Config.Libs.gson)
    implementation(Config.TestLibs.androidxTestCoreKtx)
    api("com.github.tiann:FreeReflection:3.1.0")
    api("io.github.windysha:bypassHiddenApiRestriction:1.1.0")
    implementation("android.arch.lifecycle:extensions:1.1.1")
    implementation("androidx.startup:startup-runtime:1.2.0-alpha01")
//    compileOnly project()
    //":free-android"
    compileOnly(project(path =":free-android"))
    implementation("io.github.knight-zxw:lancet-runtime:0.0.3")

    testImplementation(kotlin(Config.kotlinStdLib, KotlinCompilerVersion.VERSION))
    testImplementation(Config.TestLibs.robolectric)
    testImplementation(Config.TestLibs.kotlinTestJunit)
    testImplementation(Config.TestLibs.androidxCore)
    testImplementation(Config.TestLibs.androidxRunner)
    testImplementation(Config.TestLibs.androidxJunit)
    testImplementation(Config.TestLibs.androidxCoreKtx)
    testImplementation(Config.TestLibs.mockitoKotlin)
    testImplementation(Config.TestLibs.mockitoInline)
    testImplementation(Config.TestLibs.awaitility)
}