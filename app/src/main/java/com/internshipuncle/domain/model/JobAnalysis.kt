package com.internshipuncle.domain.model

data class JobAnalysis(
    val summary: String,
    val roleReality: String,
    val requiredSkills: List<String>,
    val preferredSkills: List<String>,
    val topKeywords: List<String>,
    val likelyInterviewTopics: List<String>,
    val difficulty: String?
)
