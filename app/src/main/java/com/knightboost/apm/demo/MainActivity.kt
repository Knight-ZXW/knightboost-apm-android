package com.knightboost.apm.demo

import android.os.Bundle
import android.os.Looper
import android.os.Message
import android.util.Log
import android.util.Xml
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.knightboost.apm.cpu.monitor.CpuUsageMonitor

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var parser = resources.getLayout(R.layout.activity_main)
        parser.next()
        val attributeSet = Xml.asAttributeSet(parser)


        val config = CpuUsageMonitor.Config()
        val cpuUsageMonitor = CpuUsageMonitor(config)
        findViewById<View>(R.id.btn_cpu_test)
            .setOnClickListener {
                Log.e("CPUM"," is \n"+Test.printCpu())
                cpuUsageMonitor.start()

            }


        findViewById<View>(R.id.btn_start).setOnClickListener {
            Thread {
                while (true){
                    Thread.sleep(1)
                    var i =0
                    while (i<100_0000_0000L){
                        i++
                    }
                }
            }.start()
        }
        findViewById<View>(R.id.btn_sliver_test).setOnClickListener {
            Thread{}.start()
            Thread {
            }.start()
        }

    }

    fun getCpuUsageByCmd(name:String){
    }
}