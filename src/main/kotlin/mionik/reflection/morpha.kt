package mionik.reflection

import mionik.dsl.ConfigurationComposer
import mionik.dsl.configuration
import mionik.dsl.model.PropertyContainer
import mionik.errors.ReflectionError
import mionik.utils.Result
import mionik.utils.Result.Companion.failure
import mionik.utils.Result.Companion.success
import mionik.utils.Result.Failure
import mionik.utils.Result.Success
import mionik.utils.asSuccess
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.full.createType
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure


private val EMPTY_CONFIGURATION = configuration {}

/**
 * Do not call with system type like: UUID, BigDecimal etc
 * Call with class than has a primary constructor
 */
fun <T : Any> morph(target: KClass<T>, configuration: ConfigurationComposer = EMPTY_CONFIGURATION): Result<T, ReflectionError> {
    /**
     * for class like UUID, BigDecimal etc. return null, because they dont have primary constructor
     */
    val ctor = target.primaryConstructor

    val createdInstance = ctor?.let { _ctor ->
        if (_ctor.parameters.isEmpty())
            _ctor.tryCall()
                .orForwardFail { error -> return error }
        else {
            val args = prepareConstructorArguments(target, ctor, configuration)
                .orForwardFail { error -> return error }

            /**
             *  Set field, getter, setter  accessible
             *  Example: class Money has private constructor and you can take it,
             *  but can't call because it's private
             */
            _ctor.isAccessible = true

            return _ctor.tryCallBy(args = args)
                .orForwardFail { error -> return error }
                .asSuccess()
        }
    }
        ?: return failure(
            ReflectionError(description = "Cannot create instance of $target, because it have not primary constructor")
        )

    return success(createdInstance)

}

/**
 * @param target         target class that will be created
 * @param ctor           constructor of target class will be used for creation this class
 * @param configuration  configuration used for set behaviour for instance creation
 * @return               Map of pairs Parameter and Value created for this parameter
 */
private fun <T : Any> prepareConstructorArguments(
    target: KClass<T>,
    ctor: KFunction<T>,
    configuration: ConfigurationComposer
): Result<Map<KParameter, Any?>, ReflectionError> {

    val args = ctor.parameters
        .associate { parameter ->
            val definedValue = defineValue(parameter.name!!, target.createType(), parameter.type, configuration)
                .orForwardFail { error -> return error }

            parameter to definedValue
        }

    return success(args)
}

private val isNeedToGenerate: (KType, ConfigurationComposer) -> Boolean = { type, config -> !type.isMarkedNullable || config.constraintsContext.isNullableGenerated }

private fun defineValue(
    parameterName: String,
    parentType: KType,
    type: KType,
    configuration: ConfigurationComposer
): Result<Any?, ReflectionError> {
    val jvmErasure = type.jvmErasure

    if (!isNeedToGenerate(type, configuration))
        return success(null)

    val predefinedTypedValue = configuration.constraintsContext.typeValueConstraints[jvmErasure]
    if (predefinedTypedValue != null)
        return success(predefinedTypedValue)

    val paramContainer = PropertyContainer(parent = parentType, self = type, name = parameterName)
    val predifinedPropertyValue = configuration.constraintsContext.propertyConstraints[paramContainer]
    if (predifinedPropertyValue != null)
        return success(predifinedPropertyValue)

    val value = if (jvmErasure.isSubclassOf(Number::class)) {
        if (jvmErasure == BigDecimal::class) {
            BigDecimal.valueOf(configuration.random.nextDouble())
        } else if (jvmErasure == Integer::class) {
            configuration.random.nextInt()
        } else if (jvmErasure == Long::class) {
            configuration.random.nextLong()
        } else if (jvmErasure == Float::class) {
            configuration.random.nextFloat()
        } else {
            configuration.random.nextDouble()
        }
    } else if (jvmErasure.isSubclassOf(Boolean::class)) {
        configuration.random.nextBoolean()
    } else if (jvmErasure.isSubclassOf(Char::class)) {
        ('a'..'z').random()
    } else if (jvmErasure.isSubclassOf(String::class)) {
        jvmErasure.toString()
    } else if (jvmErasure.isSubclassOf(Enum::class)) {
        generateEnum(type)
            .orForwardFail { error -> return error }
    } else if (jvmErasure.isSubclassOf(Collection::class)) {
        val clazzResult = getClassByCollectionTypeParameter(type)
        when (clazzResult) {
            is Success -> {
                val itemsAmount = configuration.constraintsContext.itemsInCollection

                val values = (0 until itemsAmount).map {
                    defineValue(parameterName, type, clazzResult.get.createType(), configuration)
                        .orForwardFail { error -> return@map error }
                }

                if (jvmErasure.isSubclassOf(List::class))
                    values
                else if (jvmErasure.isSubclassOf(Set::class))
                    values.toSet()
                else
                    return failure(ReflectionError(description = "Cannot define behaviour for collection type '${jvmErasure}'."))
            }
            is Failure -> clazzResult.orForwardFail { error -> return error }
        }

    } else if (jvmErasure == LocalDateTime::class) {
        LocalDateTime.now()
    } else if (jvmErasure == UUID::class) {
        UUID.randomUUID()
    } else if (jvmErasure.isSealed) {
        val subSealed = jvmErasure.sealedSubclasses.first()
        defineValue(parameterName, type, subSealed.createType(), configuration)
            .orForwardFail { error -> return error }
    } else {
        morph(type.jvmErasure, configuration)
            .orForwardFail { error -> return error }
    }

    return value.asSuccess()
}
