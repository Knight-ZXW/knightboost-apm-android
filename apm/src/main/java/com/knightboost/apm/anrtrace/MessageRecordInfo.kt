package com.knightboost.apm.anrtrace

import java.util.*

/**
 * Looper 消息记录信息
 */
class MessageRecordInfo {

    companion object {

        private var sPool = LinkedList<MessageRecordInfo>()

        @JvmStatic
        fun obtain(): MessageRecordInfo {
            synchronized(sPool){
                if (sPool.size>0){
                    return sPool.pollFirst()!!
                }
            }
            return MessageRecordInfo()
        }

        @JvmStatic
        fun recycle(messageRecordInfo: MessageRecordInfo) {
            messageRecordInfo.msgState = 0
            messageRecordInfo.msgType = 0
            messageRecordInfo.what=0
            messageRecordInfo.msgCounts=0
            messageRecordInfo.cpuDuration=0
            messageRecordInfo.wallDuration=0
            messageRecordInfo.beginTime=0
            messageRecordInfo.targetHandler=null
            messageRecordInfo.callback=null
            messageRecordInfo.wallDuration=0
            synchronized(sPool){
                sPool.add(messageRecordInfo)
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