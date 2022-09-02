package com.knightboost.apm.memory

import android.content.Context

class MemoryUsageMonitor(val context: Context) {

    fun run(){
        //memoryInfo 信息有效度依赖于采样间隔，如果采样间隔过短，则值可能不变
        val memoryInfo = MemoryUsageUtil.getMemoryInfo(context)
        val totalMemory = Runtime.getRuntime().totalMemory()
        val freeMemory = Runtime.getRuntime().freeMemory()

        val heapUsedMemory= totalMemory-freeMemory
        //TODO 前后台状态

        //Java内存是否即将触顶
        //TODO  获取前台页面名称



    }
}