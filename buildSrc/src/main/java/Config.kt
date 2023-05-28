object Config {
    val kotlinVersion = "1.6.21"
    val kotlinStdLib = "stdlib-jdk8"
    val okHttpVersion = "4.9.2"
    object BuildPlugins {
        val androidGradle = "com.android.tools.build:gradle:7.2.0"
        val kotlinGradlePlugin = "gradle-plugin"
        val buildConfig = "com.github.gmazzo.buildconfig"
        val buildConfigVersion = "3.0.3"
        val gradleMavenPublishPlugin = "com.vanniktech:gradle-maven-publish-plugin:0.18.0"
    }

    object Android {
        private val sdkVersion = 32

        val minSdkVersion = 21
        val minSdkVersionOkHttp = 21
        val minSdkVersionNdk = 16
        val minSdkVersionCompose = 21
        val targetSdkVersion = sdkVersion
        val compileSdkVersion = sdkVersion

        val abiFilters = listOf("x86", "armeabi-v7a", "x86_64", "arm64-v8a")

        fun shouldSkipDebugVariant(name: String): Boolean {
            return System.getenv("CI")?.toBoolean() ?: false && name == "debug"
        }
    }

    object Libs {
        val appCompat = "androidx.appcompat:appcompat:1.3.0"
        val timber = "com.jakewharton.timber:timber:4.7.1"
        val okhttpBom = "com.squareup.okhttp3:okhttp-bom:$okHttpVersion"
        val okhttp = "com.squareup.okhttp3:okhttp"
        val leakCanary = "com.squareup.leakcanary:leakcanary-android:2.8.1"
        val material = "com.google.android.material:material:1.4.0"
        val constraintLayout = "androidx.constraintlayout:constraintlayout:2.1.3"
        private val lifecycleVersion = "2.2.0"
        val lifecycleProcess = "androidx.lifecycle:lifecycle-process:$lifecycleVersion"
        val lifecycleCommonJava8 = "androidx.lifecycle:lifecycle-common-java8:$lifecycleVersion"
        val androidxCoreKtx = "androidx.core:core-ktx:1.7.0"
        val androidxCore = "androidx.core:core:1.3.2"
        val androidxRecylerView = "androidx.recyclerview:recyclerview:1.2.1"

        val slf4jApi = "org.slf4j:slf4j-api:1.7.30"
        val slf4jJdk14 = "org.slf4j:slf4j-jdk14:1.7.30"
        val logbackVersion = "1.2.9"
        val logbackClassic = "ch.qos.logback:logback-classic:$logbackVersion"

        val log4j2Version = "2.17.0"
        val log4j2Api = "org.apache.logging.log4j:log4j-api:$log4j2Version"
        val log4j2Core = "org.apache.logging.log4j:log4j-core:$log4j2Version"

        val aspectj = "org.aspectj:aspectjweaver"
        val servletApi = "javax.servlet:javax.servlet-api:3.1.0"
        val servletApiJakarta = "jakarta.servlet:jakarta.servlet-api:5.0.0"

        val apacheHttpClient = "org.apache.httpcomponents.client5:httpclient5:5.0.4"

        private val retrofit2Version = "2.9.0"
        private val retrofit2Group = "com.squareup.retrofit2"
        val retrofit2 = "$retrofit2Group:retrofit:$retrofit2Version"
        val retrofit2Gson = "$retrofit2Group:converter-gson:$retrofit2Version"

        val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.3"

        val fragment = "androidx.fragment:fragment-ktx:1.3.5"

        val kotlinReflect = "org.jetbrains.kotlin:kotlin-reflect"
        val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib"

        private  val gsonVersion = "2.8.0"
        val gson = "com.google.code.gson:gson:${gsonVersion}"
    }

    object TestLibs {
        private val androidxTestVersion = "1.4.0"
        private val espressoVersion = "3.4.0"

        val androidJUnitRunner = "androidx.test.runner.AndroidJUnitRunner"
        val kotlinTestJunit = "org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion"
        val androidxCore = "androidx.test:core:$androidxTestVersion"
        val androidxRunner = "androidx.test:runner:$androidxTestVersion"
        val androidxTestCoreKtx = "androidx.test:core-ktx:$androidxTestVersion"
        val androidxTestRules = "androidx.test:rules:$androidxTestVersion"
        val espressoCore = "androidx.test.espresso:espresso-core:$espressoVersion"
        val espressoIdlingResource = "androidx.test.espresso:espresso-idling-resource:$espressoVersion"
        val androidxTestOrchestrator = "androidx.test:orchestrator:1.4.1"
        val androidxJunit = "androidx.test.ext:junit:1.1.3"
        val androidxCoreKtx = "androidx.core:core-ktx:1.7.0"
        val robolectric = "org.robolectric:robolectric:4.7.3"
        val mockitoKotlin = "com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0"
        val mockitoInline = "org.mockito:mockito-inline:4.3.1"
        val awaitility = "org.awaitility:awaitility-kotlin:4.1.1"
        val mockWebserver = "com.squareup.okhttp3:mockwebserver:${okHttpVersion}"
        val mockWebserver4 = "com.squareup.okhttp3:mockwebserver:4.9.3"
        val jsonUnit = "net.javacrumbs.json-unit:json-unit:2.32.0"
        val hsqldb = "org.hsqldb:hsqldb:2.6.1"
        val javaFaker = "com.github.javafaker:javafaker:1.0.2"
    }
}