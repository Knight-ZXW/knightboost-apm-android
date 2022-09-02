package com.knightboost.apm.cpu.util

import android.util.Log
import com.knightboost.apm.cpu.sysfs.Cpu

/**
 *  该类提供获取系统idle状态 差值的功能
 *
 *  由于 /sys/devices/system/cpu/cpu${cpuIndex}/cpuidle/state${state} 值并是实时更新的，
 *  可能出现两种情况 导致在多个采样周期内 不更新:
 *      1. 系统整体CPU使用率较低，该CPU 长期(几秒以上)进入休眠状态， cpuidle/state 值会持续不更新，
 *          并且下次更新时，会一次性加上这阶段的值。
 *      2. 存在CPU使用率极高的进程，该CPU长时间处于 慢频状态下运行 并且不进入idle 状态。
 *
 *  该类主要针对上述两种情况，根据当前的采样间隔 进行适当的数值调整，劲量趋近真实的值。
 */
class SysCpuIdleTimeCalculator(private val sampleIntervalMills: Int) {
    private val lastCpuIdleTimes = mutableMapOf<Int, Long>()

    private var allowReadScalingMaxFeqFile = true;
    companion object{
        const val TAG ="SysCpuIdleTime"
    }

    /**
     * 系统idle 时间计算
     * @param allCpu 参与计算的cpu
     * @param 实际采样间隔
     * @param 当前系统频率利用率
     */
    public fun getSysIdleDeltaTime(
        allCpu: List<Cpu>,
        intervalMills: Long,
    ): Long {
        //采样间隔 微妙
        val realSampleIntervalMicros = intervalMills * 1000L;
        // 返回的 采样周期内的 idle时间
        var totalIdleDeltaTime = 0L
        for (cpu in allCpu) {
            //获取cpu当前最新的idle时间
            val nowIdleTime = cpu.idleTime()
            //获取上一次记录的该cpu idle时间
            val lastIdleTime = lastCpuIdleTimes[cpu.cpuIndex]
            lastCpuIdleTimes[cpu.cpuIndex] = nowIdleTime
            //第一次调用，只更新数据，直接跳过
            if (lastIdleTime == null) {
                continue
            }
            var deltaIdleTime = (nowIdleTime - lastIdleTime)

            if (deltaIdleTime == 0L) { //间隔采样区间内idle时间为0, 判断是CPU 100% use 还是 100% idle
                //判断当前CPU是否处于基本满频运行
                var maxFreq = 0L
                //当前调频频率
                val scalingCurFreq = cpu.cpuFreq.scalingCurFreq()
                if (!allowReadScalingMaxFeqFile) {
                    maxFreq = cpu.cpuFreq.maxFreq()
                } else {
                    try {
                        //读取当前的频率
                        maxFreq = cpu.cpuFreq.scalingMaxFreq()
                    } catch (e: Exception) {
                        //部分机型出现过读取失败的问题，未确认原因
                        allowReadScalingMaxFeqFile = false
                        maxFreq = cpu.cpuFreq.maxFreq()
                    }
                }

                //当前是否运行在最高频
                val isRunningAtMaxFreq =  maxFreq == scalingCurFreq
                if (!isRunningAtMaxFreq) {
                    Log.e(TAG,"${cpu.cpuIndex} idle 为0，不是最高频率 ${scalingCurFreq} ${maxFreq}")
                    deltaIdleTime = realSampleIntervalMicros
                }else{
                    Log.e(TAG,"${cpu.cpuIndex} idle 为0，运行在最高频率 ${scalingCurFreq} ${maxFreq}")
                }
            } else if ((deltaIdleTime) > realSampleIntervalMicros) {
                //通常idle时间过长 基本是刚从idle状态退出，此时只能进行计算
                Log.e(TAG,"${cpu.cpuIndex} idle 时间过长 ${deltaIdleTime}")
                deltaIdleTime = realSampleIntervalMicros
            }
            totalIdleDeltaTime += deltaIdleTime

        }
        return totalIdleDeltaTime / 1000;
    }
}