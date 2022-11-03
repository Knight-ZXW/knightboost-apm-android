package com.knightboost.apm.trace

import com.knightboost.apm.trace.api.trace.SpanContext

class TracerContext(
    val idGenerator: IdGenerator
) : SpanContext(
    idGenerator.generateTraceId(), idGenerator.generateSpanId(), null
)