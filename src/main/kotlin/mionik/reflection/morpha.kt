package mionik.reflection

import mionik.dsl.ConfigurationComposer
import mionik.dsl.configuration
import mionik.dsl.model.PropertyContainer
import mionik.errors.ReflectionError
import mionik.utils.Result
import mionik.utils.Result.Companion.failure
import mionik.utils.Result.Companion.success
import mionik.utils.asFailure
import mionik.utils.asSuccess
import mionik.utils.bind
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.ThreadLocalRandom
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
private val charRange = ('a'..'z')

/**
 * Do not call with system type like: UUID, BigDecimal etc
 * Call with class than has a primary constructor
 */
fun <T : Any> morph(target: KClass<T>, configuration: ConfigurationComposer = EMPTY_CONFIGURATION): Result<T, ReflectionError> {
    /**
     * for class like UUID, BigDecimal etc. return null, because they dont have primary constructor
     */
    /**
     * for class like UUID, BigDecimal etc. return null, because they dont have primary constructor
     */
    val ctor = target.primaryConstructor
        ?: return ReflectionError(description = "Cannot create instance of $target, because it have not primary constructor").asFailure()

    val createdInstance =
        if (ctor.parameters.isEmpty())
            ctor
                .tryCall()
                .orForwardFail { return it }
        else {
            /**
             *  Set field, getter, setter  accessible
             *  Example: class Money has private constructor and you can take it,
             *  but can't call because it's private
             */
            ctor.isAccessible = true

            return prepareConstructorArguments(target, ctor, configuration)
                .bind { args -> ctor.tryCallBy(args = args) }
                .orForwardFail { return it }
                .asSuccess()
        }

    return success(createdInstance)
}

/**
 * @param target         target class that will be created
 * @param ctor           constructor of target class will be used for creation this class
 * @param configuration  configuration used for set behaviour for instance creation
 * @return               Map of pairs Parameter and Value created for this parameter
 */
internal fun <T : Any> prepareConstructorArguments(
    target: KClass<T>,
    ctor: KFunction<T>,
    configuration: ConfigurationComposer
): Result<Map<KParameter, Any?>, ReflectionError> = ctor.parameters
    .associateWith { parameter ->
        val classMetadata = ClassMetadata(parameter.name!!, target.createType(), parameter.type)
        defineValue(classMetadata, configuration).orForwardFail { return it }
    }
    .asSuccess()

internal fun isNeedToGenerate(type: KType, config: ConfigurationComposer): Boolean =
    !type.isMarkedNullable || config.constraintsContext.isNullableGenerated

internal fun defineValue(classMetadata: ClassMetadata, configuration: ConfigurationComposer): Result<Any?, ReflectionError> {
    val parameterName: String = classMetadata.parameterName
    val parentType: KType = classMetadata.parentType
    val type: KType = classMetadata.type

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

    val value = when {
        jvmErasure.isSubclassOf(Number::class)  -> defineNumber(jvmErasure, configuration.random)
        jvmErasure.isSubclassOf(Boolean::class) -> configuration.random.nextBoolean()
        jvmErasure.isSubclassOf(Char::class)    -> charRange.random()
        jvmErasure.isSubclassOf(String::class)  -> jvmErasure.toString()
        jvmErasure.isSubclassOf(Enum::class)    -> generateEnum(type).orForwardFail { return it }

        jvmErasure.isSubclassOf(Collection::class) -> {
            val clazzResult = getClassByCollectionTypeParameter(type).orForwardFail { return it }
            val itemsAmount = configuration.constraintsContext.itemsInCollection
            val values = (0 until itemsAmount).map {
                val metadata = ClassMetadata(parameterName, type, clazzResult.createType())
                defineValue(metadata, configuration).orForwardFail { return@map it }
            }

            when {
                jvmErasure.isSubclassOf(List::class) -> values
                jvmErasure.isSubclassOf(Set::class)  -> values.toSet()
                else ->
                    failure(ReflectionError(description = "Cannot define behaviour for collection type '${jvmErasure}'."))
            }
        }

        jvmErasure == LocalDateTime::class -> LocalDateTime.now()
        jvmErasure == UUID::class          -> UUID.randomUUID()

        jvmErasure.isSealed -> jvmErasure.sealedSubclasses.first()
                .let { subSealed -> ClassMetadata(parameterName, type, subSealed.createType()) }
                .let { metadata -> defineValue(metadata, configuration) }
                .orForwardFail { return it }

        else -> morph(type.jvmErasure, configuration).orForwardFail { return it }
    }

    return value.asSuccess()
}

internal fun defineNumber(jvmErasure: KClass<*>, random: ThreadLocalRandom): Number =
     when {
        jvmErasure == BigDecimal::class -> BigDecimal.valueOf(random.nextDouble())
        jvmErasure == Integer::class    -> random.nextInt()
        jvmErasure == Long::class       -> random.nextLong()
        jvmErasure == Float::class      -> random.nextFloat()
        else                            -> random.nextDouble()
    }

internal data class ClassMetadata(
    val parameterName: String,
    val parentType: KType,
    val type: KType
)