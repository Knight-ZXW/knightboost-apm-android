package com.knightboost.apm.pagestartup

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.*

object AndroidPageStartupTracer {

    private var fragmentStartupTraceEnable = true;

    fun install(app: Application) {
        app.registerActivityLifecycleCallbacks(activityLifecycleCallbacks)
    }

    private val activityStartupTracerMap = mutableMapOf<Activity, ActivityStartupTracer>()
    private val fragmentStartupTracerMap = mutableMapOf<Fragment, FragmentStartupTracer>()

    fun removeTracerOfTarget(target: Activity) {
        activityStartupTracerMap.remove(target)
    }

    fun removeTracerOfTarget(target: Fragment) {
        fragmentStartupTracerMap.remove(target)
    }

//    private fun createActivityStartupTracer(activity: Activity):IPageStartupTracer{
//
//    }

    private val fragmentLifecycleCallbacks = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
            super.onFragmentAttached(fm, f, context)
            val fragmentStartupTracer = FragmentStartupTracer(f)
            fragmentStartupTracerMap[f] = fragmentStartupTracer
        }

        override fun onFragmentPreCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
            super.onFragmentPreCreated(fm, f, savedInstanceState)
            fragmentStartupTracerMap[f]?.onFragmentPreCreated(fm,f,savedInstanceState)
        }

        override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
            super.onFragmentCreated(fm, f, savedInstanceState)
            fragmentStartupTracerMap[f]?.onFragmentCreated(fm,f,savedInstanceState)

        }

        override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            fragmentStartupTracerMap[f]?.onFragmentViewCreated(fm,f,v,savedInstanceState)

        }

        override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
            super.onFragmentStarted(fm, f)
            fragmentStartupTracerMap[f]?.onFragmentStarted(fm,f)

        }

        override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
            super.onFragmentResumed(fm, f)
            fragmentStartupTracerMap[f]?.onFragmentResumed(fm,f)

        }

        override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
            super.onFragmentPaused(fm, f)
            fragmentStartupTracerMap[f]?.onFragmentPaused(fm,f)

        }

        override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
            super.onFragmentStopped(fm, f)
            fragmentStartupTracerMap[f]?.onFragmentStopped(fm,f)
        }


    }

    private val activityLifecycleCallbacks = object : Application.ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

            if (activity is FragmentActivity) {
                activity.supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentLifecycleCallbacks, true)
            }

            val activityStartupTracer = ActivityStartupTracer(activity)
            activityStartupTracerMap[activity] = activityStartupTracer
            activityStartupTracer.onActivityCreated(activity,savedInstanceState)

        }

        override fun onActivityStarted(activity: Activity) {
            activityStartupTracerMap[activity]?.onActivityStarted(activity)
        }

        override fun onActivityResumed(activity: Activity) {
            activityStartupTracerMap[activity]?.onActivityResumed(activity)

        }

        override fun onActivityPaused(activity: Activity) {
            activityStartupTracerMap[activity]?.onActivityPaused(activity)

        }

        override fun onActivityStopped(activity: Activity) {
            activityStartupTracerMap[activity]?.onActivityStopped(activity)

        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            activityStartupTracerMap[activity]?.onActivitySaveInstanceState(activity,outState);
        }

        override fun onActivityDestroyed(activity: Activity) {
            activityStartupTracerMap[activity]?.onActivityDestroyed(activity);

        }
    }

}