package com.knightboost.apm.pagestartup

import android.app.Activity
import android.app.Application
import android.os.Build
import android.os.Bundle
import android.view.FrameMetrics
import android.view.Window
import com.knightboost.apm.common.util.Clock
import com.knightboost.apm.model.DepercatedSpan
import com.knightboost.apm.util.ApmExecutors

class ActivityStartupTracer(private val activity: Activity) : PageStartupTracer(), Application.ActivityLifecycleCallbacks {

    var onActivityCreatedCallTime: Long = 0
    var onActivityStartedCallTime: Long = 0
    var onActivityResumeCallTime: Long = 0
    var createToStartCostTime: Long = 0
    var startToResumeCostTime: Long = 0

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        onActivityCreatedCallTime = Clock.getCurrentTimestampMicros()
    }

    override fun onActivityStarted(activity: Activity) {
        //trace from create to started metric
        if (onActivityStartedCallTime == 0L) {
            onActivityStartedCallTime = Clock.getCurrentTimestampMicros()
            createToStartCostTime = onActivityCreatedCallTime - onActivityCreatedCallTime
            val span = DepercatedSpan("createToStart", createToStartCostTime)
            addSubSpan(span)
        }
    }

    override fun onActivityResumed(activity: Activity) { //first frame
        //trace from started to begin resumed metric
        if (onActivityResumeCallTime == 0L) {
            onActivityCreatedCallTime = Clock.getCurrentTimestampMicros()
            startToResumeCostTime = onActivityCreatedCallTime - onActivityCreatedCallTime
            val span = DepercatedSpan("startToResume", startToResumeCostTime)
            addSubSpan(span)
        }

        //add Frame Callback
        // trace from resume to firstDraw callback
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                activity.window.addOnFrameMetricsAvailableListener(object : Window.OnFrameMetricsAvailableListener {
                    override fun onFrameMetricsAvailable(window: Window?, frameMetrics: FrameMetrics?, dropCountSinceLastInvocation: Int) {

                        window?.removeOnFrameMetricsAvailableListener(this)
                        this@ActivityStartupTracer.activityStartupFinish()
                    }
                }, ApmExecutors.createBackgroundHandler())
            } else {
                val decorView = activity.window.peekDecorView()
                decorView.post {
                    //end
                    this.end()
                }
            }

        } catch (e: Exception) {

        }
    }

    private fun activityStartupFinish() {
        if (startToResumeCostTime > 1000) {
            AndroidPageStartupTracer.removeTracerOfTarget(activity)
            return
        }
        this.end()

    }

    override fun onActivityPaused(activity: Activity) {
        AndroidPageStartupTracer.removeTracerOfTarget(activity)
    }

    override fun onActivityStopped(activity: Activity) {
        AndroidPageStartupTracer.removeTracerOfTarget(activity)
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        AndroidPageStartupTracer.removeTracerOfTarget(activity)
    }

}