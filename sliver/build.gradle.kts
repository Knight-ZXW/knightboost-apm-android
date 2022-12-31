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
        externalNativeBuild {
            cmake {
                cppFlags += "-std=c++14"
            }
        }
    }

    buildTypes {
        getByName("debug")
        getByName("release") {
            consumerProguardFiles("proguard-rules.pro")
        }
    }
    buildFeatures {
        prefab = true
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
    externalNativeBuild {
        cmake {
            path = file("CMakeLists.txt")
            version = "3.18.1"
        }
    }
    packagingOptions {
        jniLibs.excludes.add("**/libbytehook.so")
        jniLibs.excludes.add("**/libxdl.so")
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

    implementation("com.bytedance:bytehook:1.0.5")
    implementation("io.hexhacking:xdl:1.2.1")
}