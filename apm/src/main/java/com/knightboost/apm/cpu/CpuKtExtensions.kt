package com.knightboost.apm.cpu

import java.io.File

internal object CpuKtExtensions {
    fun File.readLong(): Long {
        return this.readText().trim().toLong()
    }
}