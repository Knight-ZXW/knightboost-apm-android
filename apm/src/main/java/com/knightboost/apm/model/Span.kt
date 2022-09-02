package com.knightboost.apm.model

import com.knightboost.apm.common.util.Clock

class Span {

    private val name: String

    /**
     * span begin time in microseconds
     */
    private var startTime: Long?

    /**
     * span end time in microseconds
     */
    private var endTime: Long? = null

    private var subSpans = mutableListOf<Span>()


    constructor(
        name: String,
        startTime: Long
    ) {
        this.name = name
        this.startTime = startTime
    }

    fun start() {
        if (this.startTime == null) {
            this.startTime = Clock.getCurrentTimestampMicros()
        }
    }

    fun end() {
        if (this.endTime == null) {
            this.endTime = Clock.getCurrentTimestampMicros()
        }
    }

    fun end(time:Long){
        if (this.endTime == null) {
            this.endTime = time
        }
    }

    fun startSubSpan(name: String, beginTime: Long):Span {
        val subSpan = Span(name, beginTime)
        subSpans.add(subSpan)
        return subSpan
    }

    fun addSubSpan(subSpan: Span){
        subSpans.add(subSpan)
    }

    fun setTag(name: String, value: String) {

    }

}