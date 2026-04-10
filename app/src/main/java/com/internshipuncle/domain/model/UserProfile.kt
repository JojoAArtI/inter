package com.internshipuncle.domain.model

data class UserProfile(
    val id: String? = null,
    val email: String? = null,
    val name: String = "",
    val college: String = "",
    val degree: String = "",
    val graduationYear: Int? = null,
    val targetRoles: List<String> = emptyList()
) {
    val isComplete: Boolean
        get() = name.isNotBlank() &&
            college.isNotBlank() &&
            degree.isNotBlank() &&
            graduationYear != null &&
            targetRoles.isNotEmpty()
}
