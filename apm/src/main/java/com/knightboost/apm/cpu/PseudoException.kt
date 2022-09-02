package com.knightboost.apm.cpu

import java.io.IOException

class PseudoException : IOException {
    constructor (msg: String, e: Exception?) : super(msg, e)
}