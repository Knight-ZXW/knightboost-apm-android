package com.knightboost.apm.trace;
import com.knightboost.apm.util.Supplier;

import java.util.Random;

enum RandomIdGenerator implements IdGenerator {
    INSTANCE;

    private static final long INVALID_ID = 0;
    private static final Supplier<Random> randomSupplier = new Supplier<Random>() {

        private Random  random = new Random();
        @Override
        public Random get() {
            return random;
        }
    };

    @Override
    public String generateSpanId() {
        long id;
        Random random = new Random();
        do {
            id = random.nextLong();
        } while (id == INVALID_ID);
        return SpanId.fromLong(id);
    }

    @Override
    public String generateTraceId() {
        Random random =new Random();
        long idHi = random.nextLong();
        long idLo;
        do {
            idLo = random.nextLong();
        } while (idLo == INVALID_ID);
        return TraceId.fromLongs(idHi, idLo);
    }

    @Override
    public String toString() {
        return "RandomIdGenerator{}";
    }
}