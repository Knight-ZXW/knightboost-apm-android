package com.knightboost.apm.trace.api.trace

import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

interface Span {

    /**
     * Return a builder whose construction a child span that belongs to the current span
     * @param operation - new span operation name
     * @return a new transaction span
     */
    fun childSpan(@NotNull operation: String): ChildSpanBuilder

    /**
     * Marks the end of {@code Span} execution.
     *
     * <p>Only the timing of the first end call for a given {@code Span} will be recorded, and
     * implementations are free to ignore all further calls.
     */
    fun finish()

    /**
     * Sets the type of operation that the span is measuring
     */
    fun setOperation(operation: String)

    /**
     * Gets the type of operation that the span is measuring
     */
    fun getOperation():String

    /**
     * Sets the throwable that was thrown during the executing of the span
     */
    fun setThrowable(@Nullable throwable: Throwable?)

    /**
     * Gets the throwable that was thrown during the execution of the span
     */
    fun getThrowable(): Throwable?

    /**
     * Sets the tag on span or transaction
     */
    fun setTag(key: String, value: String)

    fun getTag(key: String)

    /**
     * Returns if span has finished
     */
    fun isFinished(): Boolean

    /**
     * Sets extra data on span or transaction
     */
    fun setData(key: String, value: Any)

    /**
     * Returns extra data from span or transaction
     */
    fun getData(key: String): Any?

    fun getSpanContext(): SpanContext

}