package com.example.demo.ktor

sealed class ApiStatus<out T : Any> {
    val <T> T.exhaustive: T
    get() = this
    object Empty : ApiStatus<Nothing>()
    data class Success<out T : Any>(val list: T) : ApiStatus<T>()
    data class Error(val message: String, val cause: Exception? = null) : ApiStatus<Nothing>()
    object Loading : ApiStatus<Nothing>()
}
