package com.knightboost.apm.instrumentation

import android.app.Activity

internal interface ActivityMethodTraceListener {
    fun activityOnCreateMethod(
        activity: Activity,
        beginTime: Long,
        endTime: Long
    )

    fun activityOnStartMethod(
        activity: Activity,
        beginTime: Long,
        endTime: Long
    )

    fun activityOnResumeMethod(
        activity: Activity,
        beginTime: Long,
        endTime: Long
    )
}