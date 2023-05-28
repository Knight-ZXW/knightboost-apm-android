import org.gradle.api.initialization.resolve.RepositoriesMode.FAIL_ON_PROJECT_REPOS

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenLocal()
        mavenCentral()
        maven(url = "https://maven.aliyun.com/repository/public")
        maven(url = "https://maven.aliyun.com/repository/google")
        maven(url = "https://artifact.bytedance.com/repository/Volcengine/")
        maven(url = "https://artifact.bytedance.com/repository/byteX/")
        google()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenLocal()
        mavenCentral()
        maven(url = "https://maven.aliyun.com/repository/public")
        maven(url = "https://maven.aliyun.com/repository/google")
        maven(url = "https://jitpack.io")
        google()
    }
}
rootProject.name = "moonlight-apm-android"
rootProject.buildFileName = "build.gradle.kts"
include(
    "app",
    "apm",
    "plugin",
    "transport-api",
    "transport-tuntime",
)
