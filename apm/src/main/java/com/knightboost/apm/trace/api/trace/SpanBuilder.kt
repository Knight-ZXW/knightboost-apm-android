package com.knightboost.apm.trace.api.trace

/**
 *  {@link SpanBuilder} is used to construct {@link Span} instances
 */
interface SpanBuilder:ChildSpanBuilder {
    fun setParent(spanContext: SpanContext): SpanBuilder
    fun setParent(span: Span): SpanBuilder
}

interface ChildSpanBuilder{
    fun startSpan(): Span

}