package com.procurement.notice.domain.model.enums

import com.fasterxml.jackson.annotation.JsonValue
import com.procurement.notice.exception.EnumException

enum class AwardStatusDetails(@JsonValue val value: String) {
    ACTIVE("active"),
    EMPTY("empty"),
    UNSUCCESSFUL("unsuccessful"),
    CONSIDERATION("consideration"),
    AWAITING("awaiting"),
    NO_OFFERS_RECEIVED("noOffersReceived"),
    LOT_CANCELLED("lotCancelled");

    override fun toString(): String = value

    companion object {
        private val elements: Map<String, AwardStatusDetails> = values().associateBy { it.value.toUpperCase() }

        fun fromString(value: String): AwardStatusDetails = elements[value.toUpperCase()]
            ?: throw EnumException(
                enumType = AwardStatusDetails::class.java.canonicalName,
                value = value,
                values = values().joinToString { it.value }
            )
    }
}
