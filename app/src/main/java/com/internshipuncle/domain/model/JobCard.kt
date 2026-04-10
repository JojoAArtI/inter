package com.internshipuncle.domain.model

data class JobCard(
    val id: String,
    val title: String,
    val company: String,
    val location: String?,
    val stipend: String?,
    val workMode: String?,
    val deadline: String?,
    val tags: List<String>,
    val isFeatured: Boolean
)
