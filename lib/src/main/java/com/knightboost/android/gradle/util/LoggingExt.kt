package com.knightboost.android.gradle.util

import org.slf4j.Logger

fun Logger.warn(throwable: Throwable? = null, message: () -> String) {
    warn("[KB_APM] ${message()}", throwable)
}

fun Logger.error(throwable: Throwable? = null, message: () -> String) {
    error("[KB_APM] ${message()}", throwable)
}

fun Logger.debug(throwable: Throwable? = null, message: () -> String) {
    debug("[KB_APM] ${message()}", throwable)
}

fun Logger.info(throwable: Throwable? = null, message: () -> String) {
    info("[KB_APM] ${message()}", throwable)
}
