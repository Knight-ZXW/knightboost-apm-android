package com.knightboost.apm.looper

import android.os.SystemClock
import com.knightboost.apm.common.util.Clock

/**
 * Looper Message Tracer
 */
class LooperMessageRecorder {

    fun start(){


    }

    var curMessageBeginThreadTime =0L

    var curMessageBeginWallTime =0L;

    /**
     * 无法Hook Looper sObserver情况下的消息处理回调
     */
    fun onMessageBegin(message:String){
        curMessageBeginThreadTime = SystemClock.currentThreadTimeMillis()
        curMessageBeginWallTime = Clock.getCurrentTimestampMs()
    }

    fun onMessageEnd(message: String){
        val nowThreadTime = SystemClock.currentThreadTimeMillis()
        var costCpuTime = nowThreadTime- curMessageBeginThreadTime
        var costWallTime =  Clock.getCurrentTimestampMs() -curMessageBeginWallTime


    }

}