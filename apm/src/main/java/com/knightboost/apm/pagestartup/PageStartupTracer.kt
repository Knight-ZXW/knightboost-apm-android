package com.knightboost.apm.pagestartup

import com.knightboost.apm.common.util.Clock
import com.knightboost.apm.model.DepercatedSpan

open class PageStartupTracer
    : IPageStartupTracer {

    private var startTime: Long = 0L;

    private var endTime: Long = 0L

    private lateinit var rootSpan: DepercatedSpan

    override fun begin() {
        if (startTime != 0L) {
            // already begin
        } else {
            startTime = Clock.getCurrentTimestampMicros()
            rootSpan = DepercatedSpan("pageStartup", startTime)
        }
    }

    override fun end() {
        if (endTime != 0L) {// already ended
            return
        }
        endTime = Clock.getCurrentTimestampMicros()
        rootSpan.end(endTime)
        //上报信息
    }

    override fun addSubSpan(span: DepercatedSpan) {
        rootSpan.addSubSpan(span)
    }

    override fun beginSubSpan(name: String, begin: Long): DepercatedSpan {
        return rootSpan.startSubSpan(name, begin)
    }
}