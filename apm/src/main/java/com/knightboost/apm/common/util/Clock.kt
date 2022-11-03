package com.knightboost.apm.common.util

import java.util.concurrent.TimeUnit

class Clock {

    companion object {
        private val clock: Clock = Clock()

        private val anchor = AnchoredClock.create()

        @JvmStatic fun default(): Clock {
            return clock
        }

        /** Returns the number of nanoseconds since the epoch (00:00:00, 01-Jan-1970, GMT). */
        @JvmStatic fun now(): Long {
            return TimeUnit.MILLISECONDS.toNanos(System.currentTimeMillis());
        }

        @JvmStatic fun nanoTime(): Long {
            return System.nanoTime();
        }

        @JvmStatic fun getCurrentTimestampMicros(): Long {
            return anchor.now()
        }

        @JvmStatic fun getCurrentTimestampMs(): Long {
            return anchor.now() / 1000;
        }

        /**
         * Return a Timer object that has current time from Android System.
         *
         * @return Timer object
         */
        @JvmStatic fun getTimer(): Timer {
            return Timer();
        }

    }

}