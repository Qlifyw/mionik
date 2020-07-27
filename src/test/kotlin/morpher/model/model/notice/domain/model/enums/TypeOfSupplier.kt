package com.procurement.notice.domain.model.enums

import com.fasterxml.jackson.annotation.JsonValue
import com.procurement.notice.exception.EnumException

enum class TypeOfSupplier(@JsonValue val value: String) {
    COMPANY("company"),
    INDIVIDUAL("individual");

    override fun toString(): String = value

    companion object {
        private val elements: Map<String, TypeOfSupplier> = values().associateBy { it.value.toUpperCase() }

        fun fromString(value: String): TypeOfSupplier = elements[value.toUpperCase()]
            ?: throw EnumException(
                enumType = TypeOfSupplier::class.java.canonicalName,
                value = value,
                values = values().joinToString { it.value }
            )
    }
}
