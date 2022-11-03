package com.knightboost.apm.trace.api.trace;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SpanContext {
    @NotNull
    private final String traceId;
    @NotNull
    private final String spanId;
    @Nullable
    private final String parentSpanId;

    public SpanContext(String traceId,
                       String spanId,
                       String parentSpanId) {
        this.spanId = spanId;
        this.traceId = traceId;
        this.parentSpanId = parentSpanId;
    }

    public String getTraceId() {
        return traceId;
    }

    public String getSpanId() {
        return spanId;
    }

    @Nullable
    public String getParentSpanId() {
        return parentSpanId;
    }
}
