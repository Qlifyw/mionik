package mionik.dsl.model

import kotlin.reflect.KType

data class PropertyContainer(
    val parent: KType,
    val self: KType,
    val name: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PropertyContainer

        if (parent.classifier != other.parent.classifier) return false
        if (self.classifier != other.self.classifier) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = parent.classifier.hashCode()
        result = 31 * result + self.classifier.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }
}