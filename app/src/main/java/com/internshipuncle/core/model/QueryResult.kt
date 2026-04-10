package com.internshipuncle.core.model

sealed interface QueryResult<out T> {
    data object Loading : QueryResult<Nothing>
    data object NotConfigured : QueryResult<Nothing>
    data object BackendNotReady : QueryResult<Nothing>
    data class Success<T>(val data: T) : QueryResult<T>
    data class Failure(
        val message: String,
        val cause: Throwable? = null
    ) : QueryResult<Nothing>
}
