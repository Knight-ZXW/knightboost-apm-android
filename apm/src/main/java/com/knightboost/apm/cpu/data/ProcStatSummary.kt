package com.knightboost.apm.cpu.data

import com.knightboost.apm.cpu.util.CpuPseudoUtil

/**
 * todo: support object pool cache
 */
class ProcStatSummary {
    var sampleWallTime =0L
    var pid: String = ""
    var name: String = ""
    var state: String = ""

//    //该进程/线程 不需要从硬盘拷数据而发生的缺页(次缺页)的次数
//    var minFlt =0
//    //累计的该任务的所有的waited-for进程曾经发生的次缺页的次数目
//    var cminFlt = 0
//    //该任务需要从硬盘拷数据而发生的缺页(主缺页)的次数
//    var majFlt=0
//    //累计的该任务的所有的waited-for进程曾经发生的主缺页的次数目
//    var cmajFlt =0

    /**
     * 在用户态运行的时间，单位为jiffies
     */
    var utime: Long = 0
    /**
     * 在内核态运行的时间，单位为jiffies
     */
    var stime: Long = 0
    var cutime: Long = 0
    var cstime: Long = 0
    /**
     * 线程的静态优先级
     */
    var nice = ""
    var numThreads = 0
    var vsize: Long = 0

    val totalUsedCpuTime:Long by lazy {
        return@lazy utime+stime
    }

    val totalUsedCpuTimeMs:Long by lazy{
        return@lazy totalUsedCpuTime* CpuPseudoUtil.millSecondsPerTicks
    }

    override fun toString(): String {
        return "ProcStatSummary(sampleWallTime=$sampleWallTime, pid='$pid', name='$name', state='$state', utime=$utime, stime=$stime, cutime=$cutime, cstime=$cstime, nice='$nice', numThreads=$numThreads, vsize=$vsize, totalUsedCpuTime=$totalUsedCpuTime, totalUsedCpuTimeMs=$totalUsedCpuTimeMs)"
    }

}