package com.knightboost.apm.cpu.util

import android.os.Build
import android.system.Os
import android.system.OsConstants
import com.knightboost.apm.cpu.PseudoException
import com.knightboost.apm.cpu.PseudoReadException
import com.knightboost.apm.cpu.data.TimeInState
import java.io.File
import java.io.FileNotFoundException

class CpuPseudoUtil {

    companion object {
        @JvmStatic
        fun readCpuTimeInState(cpuTimeInStateFile: File, cpuCount: Int): TimeInState {
            if (cpuTimeInStateFile.exists().not()) {
                throw FileNotFoundException(cpuTimeInStateFile.absolutePath)
            }
            val timeInState = TimeInState()
            val lines = cpuTimeInStateFile.readLines()
            if (lines.isEmpty()) {
                throw PseudoReadException(cpuTimeInStateFile.absolutePath, null)
            }
            lines.forEach { line ->
                val timeInstatePair = StringUtil.splitWorker(line, ' ', false)
                val frequency = timeInstatePair[0].toLong()
                val time = timeInstatePair[1].toLong()
                timeInState.setTime(frequency, time * cpuCount)
            }
            return timeInState
        }


        fun readCpuTimeFromTimeInstateFile(timeInstateFile:File): Long {
            val lines = timeInstateFile.readLines()
            var totalTime = 0L
            if (lines.isEmpty()){
                throw PseudoException("timeInstateFile:${timeInstateFile.path} content is empty",null)
            }
            for (line in lines) {
                val timeInstateTuple = StringUtil.splitWorker(line, ' ')
                val time = timeInstateTuple[1].toLong()
                totalTime+=time
            }
            return totalTime

        }

        val clockTicksPerSeconds by lazy {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                return@lazy Os.sysconf(OsConstants._SC_CLK_TCK)
            } else {
                100
            }
        }

        /**
         * 每节拍对应的毫秒数
         */
        val millSecondsPerTicks by lazy {
            return@lazy 1000 / clockTicksPerSeconds
        }
    }

}