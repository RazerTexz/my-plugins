package com.github.razertexz

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView

import com.aliucord.Utils
import com.aliucord.api.SettingsAPI
import com.aliucord.fragments.SettingsPage
import com.aliucord.utils.DimenUtils
import com.aliucord.views.TextInput

import com.lytefast.flexinput.R

class PluginSettings(private val settings: SettingsAPI) : SettingsPage() {
    override fun onViewBound(view: View) {
        super.onViewBound(view)

        setActionBarTitle("Better Font Scale")
        setPadding(0)

        addView(TextView(view.context, null, 0, R.i.UiKit_Settings_Item_Header).apply {
            text = "NOTE: 0 = Use Default Value"
            typeface = context.resources.getFont(0x7F090007)
        })

        addTextInput(view.context, "Messages", "messagesFontScale")
        addTextInput(view.context, "Chatbox", "chatBoxFontScale")
        addTextInput(view.context, "Username", "userNameFontScale")
        addTextInput(view.context, "About Me", "aboutMeFontScale")
        addTextInput(view.context, "Game Status", "gameStatusFontScale")
        addTextInput(view.context, "Profile Status", "profileStatusFontScale")
        addTextInput(view.context, "Timestamp", "timestampFontScale")
        addTextInput(view.context, "Channel Category Name", "channelCategoryName")
        addTextInput(view.context, "Channel Name", "channelName")
        addTextInput(view.context, "Voice Channel Name", "voiceChannelName")
        addTextInput(view.context, "Channel Thread Name", "channelThreadName")
    }

    private fun addTextInput(context: Context, hint: CharSequence, settingName: String) {
        addView(TextInput(context, hint, settings.getFloat(settingName, 0.0f).toString(), object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                s.toString().toFloatOrNull()?.let {
                    settings.setFloat(settingName, it)
                    Utils.promptRestart()
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        }).apply {
            editText.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                setMargins(DimenUtils.defaultPadding, 8, DimenUtils.defaultPadding, 8)
            }
        })
    }
}