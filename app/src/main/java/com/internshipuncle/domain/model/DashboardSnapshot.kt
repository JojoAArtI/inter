package com.internshipuncle.domain.model

data class DashboardSnapshot(
    val readinessScore: Int?,
    val savedJobsCount: Int,
    val resumesCount: Int,
    val mockSessionsCount: Int,
    val nextDeadline: String?,
    val isConfigured: Boolean
)
