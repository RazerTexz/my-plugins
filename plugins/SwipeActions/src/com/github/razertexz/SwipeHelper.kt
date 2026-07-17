package com.github.razertexz

import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.recyclerview.widget.RecyclerView
import com.discord.models.message.Message
import com.discord.stores.StoreStream
import com.discord.widgets.chat.list.actions.WidgetChatListActions
import com.discord.widgets.chat.list.adapter.WidgetChatListAdapter
import com.discord.widgets.chat.list.entries.*

class SwipeHelper {

    private var recyclerView: RecyclerView? = null
    private var touchSlop: Float = 0f
    private val swipeThreshold: Float = 0.25f

    private val widgetChatListActions = WidgetChatListActions()
    private val storeChannels = StoreStream.getChannels()
    private val myId: Long = StoreStream.getUsers().me.id


    private val onItemTouchListener = object : RecyclerView.OnItemTouchListener {
        private var initialX = 0f
        private var initialY = 0f
        private var message: Message? = null
        private var selectedView: View? = null

        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
            when (e.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    val child = rv.findChildViewUnder(e.x, e.y) ?: return false
                    val adapterPos = rv.getChildViewHolder(child).bindingAdapterPosition
                    if (adapterPos == RecyclerView.NO_POSITION) return false

                    val adapter = rv.adapter as? WidgetChatListAdapter ?: return false
                    val entry = adapter.data.list.getOrNull(adapterPos) ?: return false

                    message = getMessageFromEntry(entry) ?: return false
                    selectedView = child
                    initialX = e.x
                    initialY = e.y
                    return false
                }
                MotionEvent.ACTION_UP,
                MotionEvent.ACTION_CANCEL -> {
                    selectedView = null
                    return false
                }
                MotionEvent.ACTION_MOVE -> {
                    val view = selectedView ?: return false
                    if (rv.scrollState == RecyclerView.SCROLL_STATE_DRAGGING) return false

                    val diffX = Math.abs(e.x - initialX)
                    val diffY = Math.abs(e.y - initialY)
                    if (diffX > touchSlop && diffX > 3f * diffY) {
                        rv.requestDisallowInterceptTouchEvent(true)
                        return true
                    }
                    return false
                }
                else -> return false
            }
        }

        override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
            val view = selectedView ?: return

            when (e.actionMasked) {
                MotionEvent.ACTION_UP,
                MotionEvent.ACTION_CANCEL -> {
                    if (Math.abs(view.translationX) > view.width * swipeThreshold) {
                        onSwiped(Math.signum(view.translationX))
                    }
                    applyTranslationToMessageGroup(rv, 0f)
                    selectedView = null
                }
                MotionEvent.ACTION_MOVE -> {
                    val tx = e.x - initialX
                    applyTranslationToMessageGroup(rv, tx)
                }
            }
        }

        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

        private fun getMessageFromEntry(entry: Any): Message? {
            return when (entry) {
                is MessageEntry    -> entry.message
                is AttachmentEntry -> entry.message
                is StickerEntry    -> entry.message
                is EmbedEntry      -> entry.message
                is ReactionsEntry  -> entry.message
                else               -> null
            }
        }

        private fun applyTranslationToMessageGroup(rv: RecyclerView, tx: Float) {
            val swipedMessageId = message?.id ?: return
            val adapter = rv.adapter as? WidgetChatListAdapter ?: return

            for (i in 0 until rv.childCount) {
                val child = rv.getChildAt(i)
                val holder = rv.getChildViewHolder(child)
                val pos = holder.bindingAdapterPosition
                
                if (pos != RecyclerView.NO_POSITION && pos < adapter.itemCount) {
                    val entry = adapter.data.list.getOrNull(pos) ?: continue
                    val childMessage = getMessageFromEntry(entry)
                    if (childMessage != null && childMessage.id == swipedMessageId) {
                        child.translationX = tx
                    }
                }
            }
        }

        private fun onSwiped(dir: Float) {
            val msg = message ?: return
            when {
                dir == -1f -> {
                    val replyMethod = WidgetChatListActions::class.java.getDeclaredMethod(
                        "replyMessage",
                        Message::class.java,
                        com.discord.api.channel.Channel::class.java
                    )
                    replyMethod.isAccessible = true
                    replyMethod.invoke(
                        widgetChatListActions,
                        msg,
                        storeChannels.getChannel(msg.channelId)
                    )
                }
                dir == 1f && msg.author.id == myId -> {
                    val editMethod = WidgetChatListActions::class.java.getDeclaredMethod(
                        "editMessage",
                        Message::class.java
                    )
                    editMethod.isAccessible = true
                    editMethod.invoke(widgetChatListActions, msg)
                }
            }
        }
    }


    fun attachToRecyclerView(rv: RecyclerView) {
        val current = recyclerView
        if (current != null) {
            if (current == rv) return
            current.removeOnItemTouchListener(onItemTouchListener)
        }
        touchSlop = ViewConfiguration.get(rv.context).scaledTouchSlop.toFloat()
        recyclerView = rv
        rv.addOnItemTouchListener(onItemTouchListener)
    }
}
