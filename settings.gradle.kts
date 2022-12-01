import org.gradle.api.initialization.resolve.RepositoriesMode.FAIL_ON_PROJECT_REPOS

pluginManagement {
    repositories {
        gradlePluginPortal()

        mavenLocal()
        maven(url = "https://maven.aliyun.com/repository/public")
        maven(url = "https://maven.aliyun.com/repository/google")
        maven(url = "https://artifact.bytedance.com/repository/Volcengine/")
        maven(url = "https://artifact.bytedance.com/repository/byteX/")
        mavenCentral()
        google()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenLocal()
        maven(url = "https://maven.aliyun.com/repository/public")
        maven(url = "https://maven.aliyun.com/repository/google")
        maven(url = "https://jitpack.io")
        mavenCentral()
        google()
    }
}
rootProject.name = "moonlight-apm-android"
rootProject.buildFileName = "build.gradle.kts"
include(
    "app",
    "apm",
    "fake-android",
    "free-android",
    "plugin",
    "transport-api",
    "transport-tuntime",
)
include(":sliver")
