package com.knightboost.apm.cpu.sysfs

import com.knightboost.apm.cpu.CpuKtExtensions.readLong
import com.knightboost.apm.cpu.util.CpuPseudoUtil
import com.knightboost.apm.cpu.util.StringUtil
import java.io.File

class CpuPolicy(private val policyFile: File) {

    private val timeInStateFile: File by lazy {
        return@lazy File(policyFile, "stats/time_in_state")
    }

    private val maxFreqFile: File by lazy {
        return@lazy File(policyFile, "cpuinfo_max_freq")
    }

    private val minFreqFile: File by lazy {
        return@lazy File(policyFile, "cpuinfo_min_freq")
    }


    private val scalingMinFreqFile: File by lazy {
        return@lazy File(policyFile, "scaling_min_freq")
    }

    private val scalingMaxFreqFile: File by lazy {
        return@lazy File(policyFile, "scaling_max_freq")
    }

    private val scalingCurFreqFile: File by lazy {
        return@lazy File(policyFile, "scaling_cur_freq")
    }

    val maxFreq: Long by lazy {
        maxFreqFile.readLong()
    }

    val minFreq: Long by lazy {
        minFreqFile.readLong()
    }

    fun scalingAvailableFrequencies(): MutableList<Long> {
        val lines = File(
            policyFile.absolutePath
                    + "/scaling_available_frequencies"
        ).readLines()
        val _frequencies = mutableListOf<Long>()
        for (line in lines) {
            if (line.isEmpty()) continue
            val frequenciesArray = line.split(" ")
            for (frequency in frequenciesArray) {
                if (frequency.isNotEmpty()) {
                    _frequencies.add(frequency.toLong())
                }
            }
        }
        return _frequencies
    }

    private var _affectedCpus = listOf<Cpu>()

    fun affectedCpus(): List<Cpu> {
        if (_affectedCpus.isEmpty()){
            val _cpus = mutableListOf<Cpu>()
            for (lineText in File(policyFile.absolutePath + "/affected_cpus").readLines()) {
                if (lineText.isNotEmpty()){
                    //todo use StringTokenizer replaced splitWorker
                    for (cpuIndex in StringUtil.splitWorker(lineText, ' ').map(String::toInt)) {
                        _cpus.add(Cpu(cpuIndex))
                    }
                }
            }
            _affectedCpus = _cpus
        }
        return _affectedCpus
    }

    fun affectedCpuIndex(): IntArray {
        return affectedCpus().map { i->i.cpuIndex }.toIntArray()
    }

    fun affectedCpuCount():Int{
        return  affectedCpus().size
    }

    fun scalingMaxFreq(): Long {
        return scalingMaxFreqFile.readLong()
    }

    fun scalingCurFreq(): Long {
        return scalingCurFreqFile.readLong()
    }

    fun scalingMinFreq(): Long {
        return scalingMinFreqFile.readLong()
    }
    /**
     * 获取cpu时间, 单位ms
     * 该API内部是通过读取 stats/time_in_state 来实现的
     */
    fun readCpuTime():Long{
            return  CpuPseudoUtil.readCpuTimeFromTimeInstateFile(timeInStateFile)*10
    }

}