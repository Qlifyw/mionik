package mionik.errors

sealed class ErrorType (
    val description: String,
    val exception: Exception?
)

class ReflectionError(description: String, exception: Exception? = null)
    : ErrorType(description = description, exception = exception)
