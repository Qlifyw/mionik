package mionik.dsl.model

import kotlin.reflect.KClass


class ConstraintsContext {
    val typeValueConstraints: MutableMap<KClass<*>, Any> = mutableMapOf()
    val propertyConstraints: MutableMap<PropertyContainer, Any> = mutableMapOf()
    var itemsInCollection: Int = 2
    var isNullableGenerated: Boolean = false
}