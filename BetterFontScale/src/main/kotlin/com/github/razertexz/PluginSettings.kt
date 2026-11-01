package com.github.razertexz

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

        addView(TextView(requireContext(), null, 0, R.i.UiKit_Settings_Item_Header).apply {
            text = "NOTE: 0 = Use Default Value"
            typeface = context.resources.getFont(0x7F090007)
        })

        addTextInput("Messages", "messagesFontScale")
        addTextInput("Chatbox", "chatBoxFontScale")
        addTextInput("Username", "userNameFontScale")
        addTextInput("About Me", "aboutMeFontScale")
        addTextInput("Game Status", "gameStatusFontScale")
        addTextInput("Profile Status", "profileStatusFontScale")
        addTextInput("Timestamp", "timestampFontScale")
        addTextInput("Channel Category Name", "channelCategoryName")
        addTextInput("Channel Name", "channelName")
        addTextInput("Voice Channel Name", "voiceChannelName")
    }

    private fun addTextInput(hint: CharSequence, settingName: String) {
        addView(TextInput(requireContext(), hint, settings.getFloat(settingName, 0.0f).toString(), object : TextWatcher {
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