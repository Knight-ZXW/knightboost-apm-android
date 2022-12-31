package com.knightboost.apm.demo

import android.os.Bundle
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.knightboost.apm.cpu.monitor.CpuUsageMonitor
import com.knightboost.freeandroid.LooperMessageObserver
import com.knightboost.freeandroid.LooperUtil
import com.knightboost.sliver.Sliver

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val config = CpuUsageMonitor.Config()
        val cpuUsageMonitor = CpuUsageMonitor(config)
        findViewById<View>(R.id.btn_cpu_test)
            .setOnClickListener {
                Log.e("CPUM"," is \n"+Test.printCpu())
                cpuUsageMonitor.start()
                LooperUtil.setObserver(object:LooperMessageObserver{
                    override fun messageDispatchStarting(): Any {
                        Log.e("looper","线程id ${Thread.currentThread().id} -> messageDispatchStarting")
                        return  ""
                    }

                    override fun messageDispatched(token: Any?, msg: Message) {
                        Log.e("looper","线程id ${Thread.currentThread().id} messageDispatched ${msg}")
                    }

                    override fun dispatchingThrewException(token: Any?, msg: Message?, exception: Exception?) {
                        Log.e("looper","线程id ${Thread.currentThread().id} dispatchingThrewException")
                    }

                })

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
            Sliver.init()
            Thread {
                Sliver.getSackTrace(Looper.getMainLooper().thread)
            }.start()
        }

    }

    fun getCpuUsageByCmd(name:String){
    }
}