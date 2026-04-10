package com.internshipuncle.core.model

sealed interface RepositoryStatus {
    data object Success : RepositoryStatus
    data object NotConfigured : RepositoryStatus
    data object BackendNotReady : RepositoryStatus
    data class Failure(
        val message: String,
        val cause: Throwable? = null
    ) : RepositoryStatus
}
