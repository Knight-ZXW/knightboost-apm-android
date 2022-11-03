package com.knightboost.apm.trace

import androidx.annotation.RestrictTo
import com.knightboost.apm.common.util.AnchoredClock
import com.knightboost.apm.trace.api.trace.*
import org.jetbrains.annotations.TestOnly
import java.util.concurrent.atomic.AtomicBoolean

class SdkSpan : Span {

    /** The Span name */
    private var name: String

    private var spanContext: SpanContext

    // The start time of the span.
    private var startEpochNanos: Long? = null

    private val finished = AtomicBoolean(false)

    // The end time of the span.
    private var endEpochNanos: Long? = null

    private val transaction: SpanTracer
    private var clock: AnchoredClock;

    constructor(
        traceId: String, parentSpanId: String?, spanTracer: SpanTracer, operation: String, clock: AnchoredClock, startEpochNanos: Long
    ) : this(
        SpanContext(
            traceId, IdGenerator.defaultInstance().generateSpanId(), parentSpanId
        ), spanTracer, operation, clock, startEpochNanos
    )

    constructor(
        spanContext: SpanContext, spanTracer: SpanTracer, operation: String,
        clock: AnchoredClock, startEpochNanos: Long
    ) {
        this.spanContext = spanContext
        this.name = operation
        this.transaction = spanTracer
        this.startEpochNanos = startEpochNanos
        this.clock = clock
        this.name = operation
    }

    override fun childSpan(operation: String): SpanBuilder {
        return transaction.childSpan(this,operation)
    }

    override fun finish() {
        endInternal()
    }

    override fun setOperation(operation: String) {
        if (finished.get()){
            return
        }
        this.name = operation
    }

    private fun endInternal() {
        if (finished.get()) {
            return
        }
        this.endEpochNanos = clock.now()
        finished.set(true)
    }

    //todo Add api finish(long timestamp,TimeUnit unit)

    override fun getOperation(): String {
        return this.name
    }

    override fun setThrowable(throwable: Throwable?) {
    }

    override fun getThrowable(): Throwable? {
        return null
    }

    override fun setTag(key: String, value: String) {
    }

    override fun getTag(key: String) {
    }

    override fun isFinished(): Boolean {
        return finished.get()
    }

    override fun setData(key: String, value: Any) {
    }

    override fun getData(key: String): Any? {
        return null
    }

    override fun getSpanContext(): SpanContext {
        return spanContext
    }

    fun getTraceId(): String {
        return spanContext.traceId;
    }

    internal fun getStartEpoch(): Long? {
        return startEpochNanos
    }

    internal fun getEndEpoch(): Long? {
        return endEpochNanos
    }

    @TestOnly
    @RestrictTo(RestrictTo.Scope.LIBRARY)
    fun costMs():Long{
        return (endEpochNanos!!-startEpochNanos!!)/1000_000;
    }

}