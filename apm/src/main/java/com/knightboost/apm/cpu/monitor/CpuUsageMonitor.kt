package com.knightboost.apm.cpu.monitor

import android.os.SystemClock
import android.util.Log
import com.knightboost.apm.common.util.Clock
import com.knightboost.apm.cpu.data.CpuUsageStat
import com.knightboost.apm.cpu.data.ProcStatSummary
import com.knightboost.apm.cpu.procfs.ProcPseudo
import com.knightboost.apm.cpu.sysfs.SysCpu
import com.knightboost.apm.cpu.util.SysCpuIdleTimeCalculator
import java.text.DecimalFormat
import java.util.concurrent.*

class CpuUsageMonitor(config: Config) {

    companion object {
        const val TAG = "CpuUsageMonitor"
        val df = DecimalFormat("#.###")
    }

    private var scheduler: ScheduledExecutorService

    val sampleIntervalMs: Int = config.sampleIntervalMs

    val profileCpuFrequencyUsage: Boolean = config.profileCpuFrequencyUsage

    val profileMainThreadCpuUsage: Boolean = config.profileMainThreadCpuUsage

    val sysCpuIdleTimeCalculator: SysCpuIdleTimeCalculator = SysCpuIdleTimeCalculator(sampleIntervalMs)

    init {
        val scheduler = config.scheduler
        if (scheduler == null) {
            this.scheduler = Executors.newScheduledThreadPool(1)
        } else {
            this.scheduler = scheduler
        }
    }

    class Config {
        val profileCpuFrequencyUsage: Boolean = true
        var profileMainThreadCpuUsage: Boolean = true
        /**
         * 监控采样间隔
         */
        var sampleIntervalMs: Int = 1000
        var scheduler: ScheduledExecutorService? = null
    }


    var started = false
    var lastSampleTime = 0L
    var workFuture: Future<*>? = null

    //stat
    var lastTotalCpuTime = 0L
    var lastProcStatSummary: ProcStatSummary? = null
    var lastMainThreadStatSummary: ProcStatSummary? = null

    @Synchronized
    fun start() {
        workFuture = scheduler.scheduleAtFixedRate(
            {
                sampleAndCalculate()
            }, 0,
            sampleIntervalMs.toLong(),
            TimeUnit.MILLISECONDS
        )
    }

    private fun sampleAndCalculate() {
        //获取cputime
        val begin = SystemClock.elapsedRealtime()
        val wallTime = Clock.getCurrentTimestampMs()
        val sampleIntervalMs = this.sampleIntervalMs

        val cpuClusters = SysCpu.cpuClusters()

        var cpuTime: Long = 0
        var totalMaxFrequency = 0L
        var totalCurFrequency = 0L
        for (cpuCluster in cpuClusters) {
            cpuTime += cpuCluster.readCpuTime() * cpuCluster.affectedCpuCount()
            if (profileCpuFrequencyUsage) {
                totalMaxFrequency += cpuCluster.scalingMaxFreq() * cpuCluster.affectedCpuCount()
                totalCurFrequency += cpuCluster.scalingCurFreq() * cpuCluster.affectedCpuCount()
            }
        }
        val deltaIdleTime = sysCpuIdleTimeCalculator.getSysIdleDeltaTime(SysCpu.cpus(), sampleIntervalMs.toLong())


        val myProcPseudo = ProcPseudo.myProcPseudo()
        val nowProcStatSummary = myProcPseudo.readProcStatSummary()
        var nowMainThreadStatSummary: ProcStatSummary? = null
        if (profileMainThreadCpuUsage) {
            nowMainThreadStatSummary = ProcPseudo.myMainThreadTaskPseudo().readProcStatSummary()
        }

        //calculate delta
        if (lastTotalCpuTime > 0) {
            val deltaCpuTime = cpuTime - lastTotalCpuTime
            val deltaProcCpuTime = nowProcStatSummary.totalUsedCpuTimeMs - lastProcStatSummary!!.totalUsedCpuTimeMs
            val cpuUsageStat = CpuUsageStat(
                wallTime = wallTime,
                interval = sampleIntervalMs,
                cpuTime = deltaCpuTime,
                idleTime = deltaIdleTime,
                maxFreq = totalMaxFrequency,
                curFreq = totalCurFrequency,
                procCpuTime = deltaProcCpuTime,
                mainThreadCpuTime = if (profileMainThreadCpuUsage)
                    (nowMainThreadStatSummary!!.totalUsedCpuTimeMs - lastMainThreadStatSummary!!.totalUsedCpuTimeMs)
                else
                    (-1)
            )

            Log.e(
                TAG, "系统CPU使用率 " + df.format(cpuUsageStat.sysCpuUsagePercent)
                        + " 进程CPU使用率 " + df.format(cpuUsageStat.procCpuUsagePercent)
                        + " 主线程运行百分比" + df.format(cpuUsageStat.mainThreadRunningPercent)
            )
        }

        //
        lastTotalCpuTime = cpuTime
        lastProcStatSummary = nowProcStatSummary
        lastMainThreadStatSummary = nowMainThreadStatSummary

    }

    @Synchronized
    fun isStarted(): Boolean {
        return started
    }

    @Synchronized
    fun stop() {
        workFuture?.cancel(false)
        started = false

    }
}
