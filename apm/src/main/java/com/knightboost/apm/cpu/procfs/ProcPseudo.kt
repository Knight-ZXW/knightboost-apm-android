package com.knightboost.apm.cpu.procfs

import com.knightboost.apm.cpu.Pseudo
import com.knightboost.apm.cpu.data.ProcStatSummary
import com.knightboost.apm.cpu.util.ProcPseudoUtil
import com.knightboost.apm.cpu.util.StringUtil
import java.io.File

class ProcPseudo(private val procPseudoDir:File):Pseudo{

    companion object{

        @JvmStatic
        fun create(path: File):ProcPseudo{
            return ProcPseudo(path)
        }

        private val myProcPseudo :ProcPseudo by lazy {
            return@lazy ProcPseudo(File("/proc/${ProcPseudoUtil.myPid()}"))
        }

        private val myMainThreadProcPseudo :ProcPseudo by lazy {
            return@lazy ProcPseudo(File("/proc/${ProcPseudoUtil.myPid()}/task/${ProcPseudoUtil.myPid()}"))
        }

        @JvmStatic
        fun  myProcPseudo(): ProcPseudo {
            return myProcPseudo
        }
        @JvmStatic
        fun myMainThreadTaskPseudo():ProcPseudo{
            return myMainThreadProcPseudo
        }
    }


    val statFile by lazy {
        File(procPseudoDir, "stat")
    }

    /**
     *
     * stat 时间定义文件: https://github.com/torvalds/linux/blob/master/include/linux/kernel_stat.h
     *      linux文档见： https://www.kernel.org/doc/Documentation/filesystems/proc.txt
     *                   http://brokestream.com/procstat.html
     *
     */
    fun readProcStatSummary(): ProcStatSummary {
        return ProcPseudoUtil.readProcStatSummary(statFile)
    }


}