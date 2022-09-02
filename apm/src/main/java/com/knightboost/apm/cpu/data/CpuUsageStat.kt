package com.knightboost.apm.cpu.data

data class CpuUsageStat constructor(
    val wallTime: Long,
    val interval:Int,
    val cpuTime: Long,
    val idleTime: Long,
    val maxFreq:Long,
    val curFreq:Long,
    val procCpuTime:Long,
    val mainThreadCpuTime:Long =-1
) {
    /**
     *  该值表示采样任务实际调度的间隔
     */
    var realInterval = interval

    /**
     * 系统CPU 使用率
     */
    val  sysCpuUsagePercent:Float by lazy {
        return@lazy 1- (idleTime.toFloat()/cpuTime)
    }

    /**
     * 进程CPU使用率
     */
    val procCpuUsagePercent:Float by lazy {
        return@lazy procCpuTime.toFloat()/cpuTime
    }

    /**
     * 主线程处于CPU运行状态的占比
     */
    val mainThreadRunningPercent:Float by lazy {
        return@lazy procCpuTime.toFloat()/interval
    }

    override fun toString(): String {
        return "CpuUsageStat(wallTime=$wallTime, interval=$interval, cpuTime=$cpuTime, idleTime=$idleTime, maxFreq=$maxFreq, curFreq=$curFreq, procCpuTime=$procCpuTime, mainThreadCpuTime=$mainThreadCpuTime, realInterval=$realInterval, sysCpuUsagePercent=$sysCpuUsagePercent, procCpuUsagePercent=$procCpuUsagePercent, mainThreadRunningPercent=$mainThreadRunningPercent)"
    }

}