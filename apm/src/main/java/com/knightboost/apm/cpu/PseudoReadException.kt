package com.knightboost.apm.cpu

import java.io.IOException
import java.lang.Exception

class PseudoReadException : IOException {
    constructor(path: String, message: String, e: Exception?) : super("read $path failed, $message", e) {}
    constructor(path: String, e: Exception?) : super("read $path failed", e) {}
    constructor(path: String) : super("read $path failed") {

    }
}