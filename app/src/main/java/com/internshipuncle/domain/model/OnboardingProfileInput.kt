package com.internshipuncle.domain.model

data class OnboardingProfileInput(
    val name: String,
    val college: String,
    val degree: String,
    val graduationYear: Int,
    val targetRoles: List<String>
)
