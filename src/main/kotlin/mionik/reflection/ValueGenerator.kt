package mionik.reflection

import mionik.errors.ReflectionError
import mionik.utils.Result
import kotlin.reflect.KType

internal fun generateEnum(type: KType): Result<Any, ReflectionError> {
    val typeResult = getClassByParameterType(type)
        .orForwardFail { error -> return error }
    val enumConst = typeResult.enumConstants
    val enumItemsAmount =  typeResult.enumConstants.size - 1
    val position = (0..enumItemsAmount).random()

    return Result.success(enumConst[position])
}