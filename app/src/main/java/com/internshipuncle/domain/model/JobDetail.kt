package com.internshipuncle.domain.model

data class JobDetail(
    val id: String,
    val title: String,
    val company: String,
    val location: String?,
    val workMode: String?,
    val employmentType: String?,
    val stipend: String?,
    val applyUrl: String?,
    val deadline: String?,
    val description: String?,
    val tags: List<String>,
    val isFeatured: Boolean
)
