package com.knightboost.apm.memory

import android.app.ActivityManager
import android.content.Context
import android.os.*
import com.knightboost.apm.cpu.util.StringUtil
import java.io.File

class MemoryUsageUtil {

    companion object {

        /**
         * 该值不会得到更新
         */
        fun getMemoryInfo(context: Context): Debug.MemoryInfo {
            val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            return activityManager.getProcessMemoryInfo(intArrayOf(android.os.Process.myPid()))[0]
        }

        /**
         *  Returns the maximum amount of memory that the Java virtual machine will attempt to use. If there is no inherent limit then the value Long.MAX_VALUE will be returned.
            Returns:
            the maximum amount of memory that the virtual machine will attempt to use, measured in bytes
         */
        fun maxMemory(): Long {
            return Runtime.getRuntime().maxMemory()
        }

        fun totalMemory(context: Context): Long {
            if (Build.VERSION.SDK_INT >= 16) {
                var memoryInfo = ActivityManager.MemoryInfo()
                val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
                activityManager.getMemoryInfo(memoryInfo)
                return memoryInfo.totalMem / 1024L
            } else {
                val bufferedReader = File("/proc/meminfo").bufferedReader()
                var line: String = ""

                try {
                    while (bufferedReader.readLine().also { line = it }.isNotEmpty()) {
                        if (line.contains("MemTotal")) {
                            //性能优化
                            return StringUtil.splitWorker(line.split(":")[1].trim(), ' ')[0].toLong()
                        }
                    }
                    return 0
                } finally {
                    bufferedReader.close()
                }

            }
        }
    }
}