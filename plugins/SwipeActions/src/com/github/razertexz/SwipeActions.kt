package com.github.razertexz

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import android.content.res.Resources
import com.aliucord.annotations.AliucordPlugin
import com.aliucord.entities.Plugin
import com.aliucord.patcher.after
import com.discord.databinding.WidgetChatListBinding
import com.discord.panels.OverlappingPanelsLayout

@AliucordPlugin(requiresRestart = true)
class SwipeActions : Plugin() {

    override fun start(context: Context) {
        val scrollingSlopField =
            OverlappingPanelsLayout::class.java.getDeclaredField("scrollingSlopPx").also {
                it.isAccessible = true
            }
        val newScrollingSlopPx = (Resources.getSystem().displayMetrics.density * 8.0f * 3.5f + 0.5f).toInt().toFloat()

        patcher.after<OverlappingPanelsLayout>("initialize", AttributeSet::class.java) {
            scrollingSlopField.set(it.thisObject, newScrollingSlopPx)
        }

        val swipeHelper = SwipeHelper()

        patcher.after<WidgetChatListBinding>(
            RecyclerView::class.java,
            RecyclerView::class.java
        ) { param ->
            swipeHelper.attachToRecyclerView(param.args[0] as RecyclerView)
        }
    }

    override fun stop(context: Context) {
        patcher.unpatchAll()
    }
}
