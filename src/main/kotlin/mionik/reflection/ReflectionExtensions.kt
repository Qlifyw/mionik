package mionik.reflection

import mionik.errors.ReflectionError
import mionik.utils.Result.Companion.failure
import mionik.utils.Result.Companion.success
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter

internal fun <T> KFunction<T>.tryCall() =
    try {
        success(this.call())
    } catch (exception: Exception) {
        failure(ReflectionError(exception.message.orEmpty(), exception))
    }

internal fun <T, R : Any?> KFunction<T>.tryCallBy(args: Map<KParameter, R>) =
    try {
        success(this.callBy(args))
    } catch (exception: Exception) {
        failure(ReflectionError(exception.message.orEmpty(), exception))
    }