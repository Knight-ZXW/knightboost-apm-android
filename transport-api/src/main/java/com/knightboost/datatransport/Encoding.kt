package com.knightboost.datatransport

/**
 * Represents encodings
 */
class Encoding private constructor(val name: String) {

    companion object {

        @JvmStatic fun of(name: String): Encoding {
            return Encoding(name)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other !is Encoding) {
            return false
        }
        return this.name == other.name
    }

    override fun hashCode(): Int {
        var h = 1000003
        h = h xor name.hashCode()
        return h
    }

    override fun toString(): String {
        return "Encoding{name=\"${name}}\""
    }
}