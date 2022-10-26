plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    `kotlin-dsl`
    id("com.vanniktech.maven.publish")
}


java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    implementation(kotlin("gradle-plugin", "1.4.32"))
    implementation("com.android.tools.build:gradle:3.4.3")
    implementation("com.google.code.gson:gson:2.8.9")
}

gradlePlugin {
   plugins {
       register("KnightBoostApmPlugin") {
           id = "knightboost.apm.android.gradle"
           implementationClass = "com.knightboost.android.gradle.ApmPlugin"
       }
   }
}

