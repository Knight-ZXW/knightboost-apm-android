package com.knightboost.apm.trace;


/** Interface used by the {@link SpanTracer} to generate new {@link SpanId}s and {@link TraceId}s. */
public interface IdGenerator {

    /**
     * Returns a {@link IdGenerator} that generates purely random IDs, which is the default for
     * OpenTelemetry.
     *
     * <p>The underlying implementation uses {@link java.util.concurrent.ThreadLocalRandom} for
     * randomness but may change in the future.
     */
    static IdGenerator defaultInstance() {
        // note: uses RandomHolder's platformDefault to account for android.
        return RandomIdGenerator.INSTANCE;
    }

    /**
     * Generates a new valid {@code SpanId}.
     *
     * @return a new valid {@code SpanId}.
     */
    String generateSpanId();

    /**
     * Generates a new valid {@code TraceId}.
     *
     * @return a new valid {@code TraceId}.
     */
    String generateTraceId();
}
