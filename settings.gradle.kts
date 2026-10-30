pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        maven("https://maven.aliucord.com/releases")
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.aliucord.com/releases")
    }
}

for (plugin in settingsDir.resolve("plugins").listFiles { it.isDirectory }) {
    include(":plugins:${plugin.name}")
}
