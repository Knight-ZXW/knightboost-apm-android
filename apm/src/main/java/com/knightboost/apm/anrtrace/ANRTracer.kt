package com.knightboost.apm.anrtrace

import android.annotation.SuppressLint
import android.os.*
import com.knightboost.apm.util.ReflectUtils
import java.lang.reflect.Field

/**
 * ANR 监控
 */
@SuppressLint("DiscouragedPrivateApi")
class ANRTracer {

    companion object {
        @JvmField
        val MSG_HISTORY: Int = 1;
        @JvmField
        val MSG_CURRENT = 2;
        @JvmField
        val MSG_WAITING = 3

        @JvmField
        val MSG_TYPE_GROUP = 1
        @JvmField
        val MSG_TYPE_SPEICAL = 2

        @JvmField
        val MSG_TYPE_HANDLED_LONG_TIME = 3
        @JvmField
        val MSG_IDLE = 4

    }

    private lateinit var messageNextFiled: Field
    private lateinit var mQueueField: Field

    fun init() {
        mQueueField = Looper::class.java.getDeclaredField("mQueue")
        messageNextFiled = Message::class.java.getDeclaredField("next")
    }

    @SuppressLint("DiscouragedPrivateApi")
    fun obtainLooperWaitingMsg() {
        var messageQueue: MessageQueue? = null
        if (Build.VERSION.SDK_INT <= 23) {
            messageQueue = mQueueField.get(Looper.getMainLooper()) as MessageQueue
        } else {
            messageQueue = Looper.getMainLooper().queue
        }
        var mMessages = ReflectUtils.reflect(messageQueue).field("mMessages").get<Message>()
        while (mMessages != null) {

        }

    }

}