package com.knightboost.apm.protocol

import java.util.*

class LogId {
    companion object {
        @JvmStatic val EMPTY_ID = LogId(UUID(0, 0))
    }

    private val uuid: UUID

    constructor(uuid: UUID = UUID.randomUUID()) {
        this.uuid = uuid
    }

    constructor(idString: String) {
        this.uuid = fromStringSentryId(idString)
    }

    override fun toString(): String {
        return uuid.toString().replace("-", "")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as LogId
        if (uuid != other.uuid) return false
        return true
    }

    override fun hashCode(): Int {
        return uuid.hashCode()
    }

    private fun fromStringSentryId(sentryIdString: String): UUID {
        var sentryIdString = sentryIdString
        if (sentryIdString.length == 32) {
            // expected format, SentryId is a UUID without dashes
            sentryIdString = StringBuilder(sentryIdString).insert(8, "-").insert(13, "-").insert(18, "-").insert(23, "-").toString()
        }
        require(sentryIdString.length == 36) {
            ("String representation of SentryId has either 32 (UUID no dashes) " + "or 36 characters long (completed UUID). Received: " + sentryIdString)
        }
        return UUID.fromString(sentryIdString)
    }

}