package com.internshipuncle.domain.model

data class InterviewSessionSummary(
    val id: String,
    val roleName: String?,
    val difficulty: String?,
    val mode: String?,
    val overallScore: Int?,
    val createdAt: String?
)
