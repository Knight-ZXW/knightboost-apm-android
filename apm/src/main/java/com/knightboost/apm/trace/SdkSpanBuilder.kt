package com.knightboost.apm.trace

import com.knightboost.apm.trace.api.trace.Span
import com.knightboost.apm.trace.api.trace.SpanBuilder
import com.knightboost.apm.trace.api.trace.SpanContext
import java.util.concurrent.TimeUnit

class SdkSpanBuilder : SpanBuilder {
    val spanName: String
    var startEpochNanos: Long? = null
    var parentSpanContext: SpanContext? = null
    val spanTracer: SpanTracer;

    constructor(spanName: String, spanTracer: SpanTracer) {
        this.spanName = spanName
        this.spanTracer = spanTracer
    }

    override fun startSpan(): Span {
        val idGenerator = IdGenerator.defaultInstance()
        val traceId = parentSpanContext?.traceId ?: idGenerator.generateTraceId()
        val spanId = idGenerator.generateSpanId()
        val sdkSpan = SdkSpan(
            SpanContext(
                traceId, spanId,
                parentSpanContext?.spanId
            ),
            spanTracer, spanName,
            spanTracer.clock,
            spanTracer.clock.now()
        )
        spanTracer.addChild(sdkSpan)
        return sdkSpan
    }

    override fun setParent(spanContext: SpanContext): SpanBuilder {
        this.parentSpanContext = spanContext
        return this
    }

    override fun setParent(span: Span): SpanBuilder {
        this.parentSpanContext = span.getSpanContext()
        return this
    }

    fun setStartTimestamp(startTimestamp: Long, timeUnit: TimeUnit): SpanBuilder {
        if (startTimestamp < 0) {
            return this
        }
        this.startEpochNanos = startTimestamp
        return this
    }

}