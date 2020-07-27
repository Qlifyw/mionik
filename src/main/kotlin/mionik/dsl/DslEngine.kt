package mionik.dsl

import mionik.dsl.model.ConstraintsContext
import mionik.dsl.model.PropertyContainer
import java.util.concurrent.ThreadLocalRandom
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.createType

object configuration {
    operator fun invoke(init: ConfigurationComposer.Builder.() -> Unit): ConfigurationComposer {
        val context = ConfigurationComposer.Builder()
        context.init()
        return context.build()
    }
}

class ConfigurationComposer private constructor(
    val constraintsContext: ConstraintsContext
) {
    val random = ThreadLocalRandom.current()

    class Builder {
        private val boundariesContext = ConstraintsContext()

        fun restriction(init: ConstraintsContext.() -> Unit) =
            apply { boundariesContext.init() }

        fun properties(init: ConstraintsContext.() -> Unit) =
            apply { boundariesContext.init() }

        fun <T : Any> ConstraintsContext.bound(clazz: KClass<T>, transform: () -> T) {
            this.typeValueConstraints.put(clazz, transform())
        }

        inline fun <reified T : Any?, reified R : Any> ConstraintsContext.bound(
            property: KProperty1<R, T>,
            transform: () -> T
        ) {
            val propertyContainer = PropertyContainer(
                parent = R::class.createType(),
                self = T::class.createType(),
                name = property.name
            )
            this.propertyConstraints.put(propertyContainer, transform()!!)
        }

        fun build() = ConfigurationComposer(
            constraintsContext = boundariesContext
        )
    }
}
