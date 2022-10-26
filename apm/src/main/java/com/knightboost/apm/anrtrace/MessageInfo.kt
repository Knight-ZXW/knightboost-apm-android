package com.knightboost.apm.anrtrace

import java.util.*

/**
 * Looper 消息记录信息
 */
class MessageInfo {

    companion object {

        private var sPool = LinkedList<MessageInfo>()

        @JvmStatic
        fun obtain(): MessageInfo {
            synchronized(sPool){
                if (sPool.size>0){
                    return sPool.pollFirst()!!
                }
            }
            return MessageInfo()
        }

        @JvmStatic
        fun recycle(messageInfo: MessageInfo) {
            messageInfo.msgState = 0
            messageInfo.msgType = 0
            messageInfo.what=0
            messageInfo.msgCounts=0
            messageInfo.cpuDuration=0
            messageInfo.wallDuration=0
            messageInfo.beginTime=0
            messageInfo.targetHandler=null
            messageInfo.callback=null
            messageInfo.wallDuration=0
            synchronized(sPool){
                sPool.add(messageInfo)
            }
        }

    }

    var msgState: Int = MsgState.MSG_STATE_UN_PROCESS;

    var msgType: Int = 0

    /**
     * The value of Message.what field
     */
    var what: Int = 0;

    /**
     * The total msg counts executed during this sampling period (only for MessageGroup)
     */
    var msgCounts: Int = 1

    /**
     * The total execution cpu time of the msg
     */
    var cpuDuration: Int = 0;

    /**
     * The total execution wall time of the msg
     */
    var wallDuration: Int = 0

    /**
     * 记录消息处理的开始时间，如果消息还处于队列中，则值为0
     */
    var beginTime: Long = 0

    /**
     * target handler of the message
     */
    var targetHandler: String? = null

    /**
     * callback Runnable of the message
     */

    var callback: String? = null

    /**
     * The message overdue time (only for pending msg)
     */
    var waitingTime: Long = 0

    //todo 和堆栈的Id关联

}