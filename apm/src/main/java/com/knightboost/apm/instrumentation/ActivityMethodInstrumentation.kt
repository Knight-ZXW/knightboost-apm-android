package com.knightboost.apm.instrumentation

import android.app.Activity

object ActivityMethodInstrumentation {
    fun activityOnCreate(activity: Activity, beginMicros: Long, endMicros: Long) {

    }
    fun activityOnStartMethod(activity: Activity, beginMicros: Long, endMicros: Long) {}
    fun activityOnResumeMethod(activity: Activity, beginMicros: Long, endMicros: Long) {}
}