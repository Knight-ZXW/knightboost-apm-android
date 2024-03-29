import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
plugins {
    `java-library`
    kotlin("jvm") version "1.6.21"
    id("com.android.application") version "7.3.1" apply false
    id("com.android.library") version "7.2.0" apply false
    id("org.jetbrains.kotlin.android") version "1.7.20" apply false
}

buildscript {
//    repositories {
//        gradlePluginPortal()
//
//        mavenLocal()
//        maven(url = "https://maven.aliyun.com/repository/public")
//        maven(url = "https://maven.aliyun.com/repository/google")
//        maven(url = "https://artifact.bytedance.com/repository/Volcengine/")
//        maven(url = "https://artifact.bytedance.com/repository/byteX/")
//        mavenCentral()
//        google()
//    }
    dependencies {
        classpath(Config.BuildPlugins.androidGradle)
        classpath(kotlin(Config.BuildPlugins.kotlinGradlePlugin, version = Config.kotlinVersion))
        classpath(Config.BuildPlugins.gradleMavenPublishPlugin)
//        classpath("io.github.knight-zxw:android-apm-plugin:0.0.1")
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}