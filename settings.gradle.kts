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

include(
    ":AliucordStyleSheets",
    ":AttachmentRemover",
    ":AutoSpoilerAttachments",
    ":BetterFontScale",
    ":BetterPlusReacts",
    ":BlockThemCalls",
    ":CopyCodeBlocks",
    ":FullUsernames",
    ":MarkdownToolbar",
    ":PluginWeb",
    ":ReadAllButton",
    ":SearchScrollSaver",
    ":SilentMessages",
    ":SimpleMessageLogger",
    ":SwipeActions",
    ":SwipeToReply",
    ":ThemeWeb",
    ":Yoink"
)