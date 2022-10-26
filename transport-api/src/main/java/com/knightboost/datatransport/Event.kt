package com.knightboost.datatransport

public final class Event<T>
   private constructor(val code: Int?, val payload: T, priority: Priority) {

    companion object {
        @JvmStatic
        fun <T> ofData(code: Int, payload: T): Event<T> {
            return Event(code, payload, Priority.DEFAULT);
        }

        @JvmStatic
        fun <T> ofData(payload: T): Event<T> {
            return Event(null, payload, Priority.DEFAULT);
        }
    }
}