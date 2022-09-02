package com.knightboost.apm.cpu.sysfs

import com.knightboost.apm.cpu.CpuKtExtensions.readLong
import com.knightboost.apm.cpu.PseudoReadException
import java.io.File
import java.util.regex.Pattern

class Cpu (val cpuIndex:Int){
    private val basePath = "/sys/devices/system/cpu/cpu${cpuIndex}/"

    private val timeInStateFile by lazy {
        return@lazy File(basePath + "cpufreq/stats/time_in_state")
    }

    public val cpuCapacity by lazy {
        return@lazy File(basePath, "cpu_capacity").readLong()
    }

    val cpuFreq:CpuFreq by lazy {
        return@lazy CpuFreq(cpuIndex)
    }

    var idleStateReadError = false

    //todo 确认这个在运行过程中会不会发生变化
    private val cpuIdleStates by lazy {
        val idleStates = mutableListOf<CpuIdleState>()
        val file = File("${basePath}/cpuidle")
        val stateFiles = file.listFiles { _, name -> Pattern.matches("state[0-9]", name) }
        if (stateFiles ==null || stateFiles.isEmpty()){
            // 是否会发生这种情况
            // 异常全局捕获
            idleStateReadError = true
            return@lazy emptyList<CpuIdleState>()
        }
        for (cpuIdleFile in stateFiles) {
            val state = cpuIdleFile.name.replace("state", "").toInt()
            val cpuIdle = CpuIdleState(cpuIndex, state)
            idleStates.add(cpuIdle)
        }
        idleStates.sortBy(CpuIdleState::state)
        return@lazy idleStates
    }

    fun idleTime():Long{
        if (idleStateReadError){//抛出异常
            throw PseudoReadException("${basePath}/cpuidle","cpuIdle state is Empty",null)
        }
        var total = 0L;
        for (cpuIdleState in cpuIdleStates) {
            val time = cpuIdleState.time()
            total += time
        }
        return total
    }

    /**
     * 当前CPU 是否运行
     */
    fun online(): Boolean {
        return File(basePath, "online").readText() == "1"
    }


}