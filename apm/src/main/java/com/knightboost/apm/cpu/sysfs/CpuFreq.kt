package com.knightboost.apm.cpu.sysfs

import com.knightboost.apm.cpu.CpuKtExtensions.readLong
import java.io.File

class CpuFreq(val cpuIndex:Int) {
    val pseudoFile: File = File("/sys/devices/system/cpu/cpu${cpuIndex}/cpufreq")

    private val scalingMaxFreqFile by lazy {
        return@lazy File(pseudoFile, "scaling_max_freq")
    }

    private val scalingMinFreqFile by lazy {
        return@lazy File(pseudoFile, "scaling_min_freq")
    }

    private val scalingCurFreqFile by lazy {
        return@lazy File(pseudoFile, "scaling_cur_freq")
    }



    private val maxFreqFile by lazy {
        return@lazy File(pseudoFile, "cpuinfo_max_freq")
    }

    private val minFreqFile by lazy {
        return@lazy File(pseudoFile, "cpuinfo_min_freq")
    }

    fun scalingMaxFreq(): Long {
        return scalingMaxFreqFile.readLong()
    }

    fun scalingMinFreq(): Long {
        return scalingMinFreqFile.readLong()
    }

    fun scalingCurFreq(): Long {
        return scalingCurFreqFile.readLong()
    }

    fun maxFreq(): Long {
        return maxFreqFile.readLong()
    }

    fun minFreq():Long{
        return minFreqFile.readLong()
    }
}