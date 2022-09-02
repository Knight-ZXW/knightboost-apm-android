package com.knightboost.apm.common.util

class Clock {

    /**
     * Return a Timer object that has current time from Android System.
     *
     * @return Timer object
     */
    fun getTime(): Timer {
        return Timer()
    }

    companion object {
        private val clock: Clock = Clock()

        private val anchor = clock.getTime()

        @JvmStatic
        fun default(): Clock {
            return clock
        }

        @JvmStatic
        fun getCurrentTimestampMicros():Long {
            return  anchor.currentTimestampMicros
        }

        @JvmStatic
        fun getCurrentTimestampMs():Long{
            return anchor.currentTimestampMicros/1000;
        }

    }
}