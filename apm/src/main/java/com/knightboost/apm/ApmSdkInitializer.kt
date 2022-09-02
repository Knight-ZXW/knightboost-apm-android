package com.knightboost.apm

import android.content.Context
import androidx.startup.Initializer
import com.knightboost.apm.application.AppStateMonitor
import com.knightboost.apm.appstartup.AppStartupTracer
import com.knightboost.apm.common.util.Clock
import com.knightboost.apm.common.util.Timer

/**
 *
 */
internal class ApmSdkInitializer : Initializer<ApmSdkBaseInit> {
    init {
        APP_START_TIME = Clock().getTime()
    }

    companion object{
        private lateinit var  APP_START_TIME: Timer

        @JvmStatic
        fun getAppStartTime(): Timer {
            return APP_START_TIME
        }
    }

    override fun create(context: Context): ApmSdkBaseInit {
        AppStateMonitor.registerActivityLifecycleCallbacks(context)
        //todo 检测appStartup 从后台启动的场景
        AppStartupTracer.getInstance().registerActivityLifecycleCallbacks(context)
        return ApmSdkBaseInit()
    }

    override fun dependencies() = emptyList<Class<out Initializer<*>?>>()

}
class ApmSdkBaseInit {}