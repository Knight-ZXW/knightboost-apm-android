import com.android.build.api.dsl.ApplicationBuildType

plugins {
    id("com.android.application")
    kotlin("android")
}


//apply plugin: 'knightboost.apm.android.gradle'
//
//KnightBoostAPM {
//    autoUploadProguardMapping = true
//}

android {
    compileSdk = Config.Android.compileSdkVersion

    defaultConfig {
        applicationId = "com.knightboost.apm.demo"
        targetSdk = Config.Android.targetSdkVersion
        minSdk = Config.Android.minSdkVersionNdk
        testInstrumentationRunner = Config.TestLibs.androidJUnitRunner
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        getByName("debug"){
            isDebuggable = true
        }
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"),"proguard-rules.pro")
        }

    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

dependencies {

    implementation(Config.Libs.androidxCoreKtx)
    implementation(Config.Libs.kotlinStdLib)
    implementation(Config.Libs.appCompat)

    implementation(Config.Libs.material)
    implementation(Config.Libs.constraintLayout)
//    //apm性能组件
//    implementation 'com.volcengine:apm_insight:1.4.5.cn'
//    //apm稳定性组件
//    implementation 'com.volcengine:apm_insight_crash:1.4.3'
//    testImplementation 'junit:junit:4.+'
    androidTestImplementation(Config.TestLibs.androidxJunit)
    androidTestImplementation(Config.TestLibs.espressoCore)
//    implementation(project(path":apm"))
    implementation(project(path = ":apm"))
    implementation(project(path = ":sliver"))
    implementation(project(path = ":free-android"))

    implementation("io.hexhacking.xcrash:xcrash-android-lib:3.0.0")

    // define a BOM and its version
    implementation(platform(Config.Libs.okhttpBom))
    implementation(Config.Libs.okhttp)
}