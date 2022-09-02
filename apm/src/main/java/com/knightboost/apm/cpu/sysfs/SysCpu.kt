package com.knightboost.apm.cpu.sysfs


import com.knightboost.apm.cpu.PseudoException
import java.io.File
import java.io.FilenameFilter
import java.util.regex.Pattern

object SysCpu {
    private val policyPattern = Pattern.compile("policy[0-9]")
    private val cpuPattern = Pattern.compile("cpu[0-9]")


    private val cpus: List<Cpu> by lazy {
        val cpus = mutableListOf<Cpu>()
        val cpuFilter = FilenameFilter { _, name
            ->
            return@FilenameFilter cpuPattern.matcher(name).matches()
        }
        val cpuFiles = File("/sys/devices/system/cpu/").listFiles(cpuFilter)
            ?: throw PseudoException("couldn't find cpu pseudo file in '/sys/devices/system/cpu/' dir",null)
        for (cpuFile in cpuFiles) {
            cpus.add(Cpu(cpuFile.name.substring(3, cpuFile.name.length).toInt()))
        }
        cpus.sortWith { o1, o2 -> o1.cpuIndex - o2.cpuIndex }
        return@lazy cpus;
    }

    val maxFrequency by lazy {
        var totalMaxFrequency = 0L
        for (cpuPseudo in cpus) {
            val maxFreq = cpuPseudo.cpuFreq.maxFreq()
            totalMaxFrequency+=maxFreq
        }
        return@lazy totalMaxFrequency
    }

    fun cpuCount(): Int {
        return cpus.size
    }

    fun cpus(): List<Cpu> {
        return cpus
    }

    fun cpuClusters(): List<CpuPolicy> {
        val cpuClusters = mutableListOf<CpuPolicy>()
        val cpuFreqFile = File("/sys/devices/system/cpu/cpufreq")
        val policyFilter = FilenameFilter { _, name
            ->
            return@FilenameFilter policyPattern.matcher(name).matches()
        }
        val policyFiles:Array<File> = cpuFreqFile.listFiles(policyFilter)?:return cpuClusters
        for (policyFile in policyFiles) {
            val cpuPolicy = CpuPolicy(policyFile)
            cpuClusters.add(cpuPolicy)
        }
        return cpuClusters
    }

}