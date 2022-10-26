package com.knightboost.datatransport.runtime

import com.knightboost.datatransport.Encoding

class EncodedPayload(
    val encoding: Encoding, val bytes: Array<Byte>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EncodedPayload

        if (encoding != other.encoding) return false
        if (!bytes.contentEquals(other.bytes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = encoding.hashCode()
        result = 31 * result + bytes.contentHashCode()
        return result
    }

}