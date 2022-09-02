package com.knightboost.apm.cpu.sysfs

import com.knightboost.apm.cpu.CpuKtExtensions.readLong
import java.io.File

class CpuIdleState(val cpuIndex:Int,val state:Int) {
    val pseudoPath = "/sys/devices/system/cpu/cpu${cpuIndex}/cpuidle/state${state}"

    val name by lazy {
        return@lazy File(pseudoPath,"name").readText()
    }

    private val timeFile by lazy {
        return@lazy File(pseudoPath, "time")
    }
    private val usageFile by lazy {
        return@lazy File(pseudoPath, "usage")
    }


    fun time(): Long {
        return timeFile.readLong()
    }

    // cpu idle进入的次数
    fun usage(): Long {
        return usageFile.readLong()
    }


}