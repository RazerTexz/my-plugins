package com.github.razertexz

import android.content.Context
import android.widget.TextView

import com.aliucord.Utils
import com.aliucord.annotations.AliucordPlugin
import com.aliucord.entities.Plugin
import com.aliucord.patcher.before

@AliucordPlugin(requiresRestart = true)
class BetterFontScale : Plugin() {
    init {
        settingsTab = SettingsTab(PluginSettings::class.java).withArgs(settings)
    }

    override fun start(context: Context) {
        patcher.before<TextView>("setRawTextSize", Float::class.java, Boolean::class.java) {
            val newTextSize = when (id) {
                Utils.getResId("chat_list_adapter_item_text", "id") -> settings.getFloat("messagesFontScale", 0.0f)
                Utils.getResId("text_input", "id") -> settings.getFloat("chatBoxFontScale", 0.0f)
                Utils.getResId("username_text", "id"), Utils.getResId("chat_list_adapter_item_text_name", "id") -> settings.getFloat("userNameFontScale", 0.0f)
                Utils.getResId("about_me_text", "id") -> settings.getFloat("aboutMeFontScale", 0.0f)
                Utils.getResId("channel_members_list_item_game", "id") -> settings.getFloat("gameStatusFontScale", 0.0f)
                Utils.getResId("user_profile_header_custom_status", "id") -> settings.getFloat("profileStatusFontScale", 0.0f)
                Utils.getResId("chat_list_adapter_item_text_timestamp", "id") -> settings.getFloat("timestampFontScale", 0.0f)
                Utils.getResId("channels_item_category_name", "id") -> settings.getFloat("channelCategoryName", 0.0f)
                Utils.getResId("channels_item_channel_name", "id") -> settings.getFloat("channelName", 0.0f)
                Utils.getResId("channels_item_voice_channel_name", "id") -> settings.getFloat("voiceChannelName", 0.0f)
                else -> 0.0f
            }

            if (newTextSize != 0.0f) {
                it.args[0] = newTextSize * context.resources.displayMetrics.scaledDensity
            }
        }
    }

    override fun stop(context: Context) {
        patcher.unpatchAll()
    }
}