package com.knightboost.apm.cpu.util

import android.os.Process
import com.knightboost.apm.cpu.PseudoException
import com.knightboost.apm.cpu.data.ProcStatSummary
import java.io.File

class ProcPseudoUtil {

    companion object {
       private var myPid: Int = 0

        @JvmStatic
        fun myPid(): Int {
            if (myPid == 0) {
                myPid = Process.myPid()
                return myPid
            }
            return myPid
        }

        fun mainThreadTid(): Int {
            return myPid()
        }

        @JvmStatic
        fun readProcStatSummary(statFile: File): ProcStatSummary {
            val procStatSummary = ProcStatSummary()
            val statInfo = statFile.readText()
            val segments = StringUtil.splitWorker(statInfo, ' ', false)
            procStatSummary.pid = segments[0]
            if (segments[1].endsWith(")")) {
                procStatSummary.name = segments[1].substring(1, segments[1].length - 1)
            }
            procStatSummary.state = segments[2]
            procStatSummary.utime = segments[13].toLong()
            procStatSummary.stime = segments[14].toLong()
            procStatSummary.cutime = segments[15].toLong()
            procStatSummary.cstime = segments[16].toLong()
            procStatSummary.nice = segments[18]
            procStatSummary.numThreads = segments[19].toInt()
            procStatSummary.vsize = segments[22].toLong()
            return procStatSummary
        }


    }
}