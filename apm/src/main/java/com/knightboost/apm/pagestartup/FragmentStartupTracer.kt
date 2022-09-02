package com.knightboost.apm.pagestartup

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.knightboost.apm.common.util.Clock

/**
 * 对于Fragment来说，从Fragment创建 到 Fragment可见 并不一定是 连续的 (比如在ViewPager场景中)
 */
class FragmentStartupTracer(val fragment: Fragment) : PageStartupTracer() {
    var onFragmentAttachedTime = 0L
    var onFragmentPreCreatedTime = 0L
    var onFragmentCreatedTime = 0L
    var onFragmentViewCreatedTime = 0L
    var onFragmentStartedTime = 0L
    var onFragmentResumedTime = 0L

    val nonContinueStateMoveTimeThreshold = 1000L;

    fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
        onFragmentAttachedTime = Clock.getCurrentTimestampMicros()
    }

    fun onFragmentPreCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        onFragmentPreCreatedTime = Clock.getCurrentTimestampMicros()
        val attachedToPreCreateCost = onFragmentPreCreatedTime - onFragmentAttachedTime
    }

    fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        onFragmentViewCreatedTime = Clock.getCurrentTimestampMicros()
    }

    fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
        onFragmentViewCreatedTime = Clock.getCurrentTimestampMicros()
    }

    fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
        onFragmentStartedTime = Clock.getCurrentTimestampMicros()

    }

    fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        onFragmentResumedTime = Clock.getCurrentTimestampMicros()
        val view = f.view
        if (view == null) {
            //end
            this.fragmentStartupFinish()
        } else {
            view.post {
                this.fragmentStartupFinish()
            }
        }
    }

    fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
        AndroidPageStartupTracer.removeTracerOfTarget(fragment)
    }

    fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
        AndroidPageStartupTracer.removeTracerOfTarget(fragment)
    }

    private fun fragmentStartupFinish() {
        this.end()
    }

}