package mionik.reflection

import mionik.errors.ReflectionError
import mionik.utils.Result
import mionik.utils.Result.Companion.failure
import mionik.utils.asFailure
import mionik.utils.asSuccess
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.jvm.javaType
import kotlin.reflect.jvm.jvmErasure


internal fun getClassByCollectionTypeParameter(type: KType): Result<KClass<*>, ReflectionError> {
    val collectionType = type.arguments.first().type?.javaType?.typeName
    return if (collectionType != null)
        Class.forName(collectionType).kotlin
            .asSuccess()
    else
        ReflectionError("Cannot define parameter type for collection: $type. Collection type classifier: ${type.javaType.typeName}.")
            .asFailure()
}

internal fun getClassByParameterType(type: KType): Result<Class<*>, ReflectionError> =
    type.classifier
        ?.let { classifier -> Result.success(classifier.starProjectedType.jvmErasure.java) }
        ?: failure(ReflectionError(description = "For type '${type}' cannot define java class."))

