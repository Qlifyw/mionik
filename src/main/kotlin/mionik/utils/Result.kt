package mionik.utils

fun <T, E> T.asSuccess(): Result<T, E> = Result.success(this)
fun <T, E> E.asFailure(): Result<T, E> = Result.failure(this)

sealed class Result<out T, out E> {
    companion object {
        fun <T> success(value: T) = Success(value)
        fun <E> failure(error: E) = Failure(error)
    }

    abstract val isSuccess: Boolean

    abstract val get: T
    abstract val error: E

    inline fun doOnError(block: (error: E) -> Unit): T {
        if (!this.isSuccess) block(this.error)
        return this.get
    }

    inline fun orForwardFail(failure: (Failure<E>) -> Nothing): T {
        return when (this) {
            is Success -> this.get
            is Failure -> failure(this)
        }
    }

    val orNull: T?
        get() = when (this) {
            is Success -> get
            is Failure -> null
        }

    infix fun <R : Exception> orThrow(block: (E) -> R): T = when (this) {
        is Success -> get
        is Failure -> throw block(this.error)
    }

    infix fun <R> map(transform: (T) -> R): Result<R, E> = when (this) {
        is Success -> Success(transform(this.get))
        is Failure -> this
    }

    infix fun <R> mapError(transform: (E) -> R): Result<T, R> = when (this) {
        is Success -> this
        is Failure -> Failure(transform(this.error))
    }

    class Success<out T> internal constructor(value: T) : Result<T, Nothing>() {
        override val isSuccess: Boolean = true
        override val get: T = value
        override val error: Nothing
            get() = throw NoSuchElementException("The result does not contain an error.")
    }

    class Failure<out E> internal constructor(value: E) : Result<Nothing, E>() {
        override val isSuccess: Boolean = false
        override val get: Nothing
            get() = throw NoSuchElementException("The result does not contain a value.")
        override val error: E = value
    }
}

infix fun <T, R, E> Result<T, E>.bind(function: (T) -> Result<R, E>): Result<R, E> = when (this) {
    is Result.Success -> function(this.get)
    is Result.Failure -> this
}
