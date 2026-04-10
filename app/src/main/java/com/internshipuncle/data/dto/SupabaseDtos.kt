package com.internshipuncle.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class ProfileDto(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String? = null,
    @SerialName("email")
    val email: String? = null,
    @SerialName("college")
    val college: String? = null,
    @SerialName("degree")
    val degree: String? = null,
    @SerialName("graduation_year")
    val graduationYear: Int? = null,
    @SerialName("target_roles")
    val targetRoles: List<String> = emptyList(),
    @SerialName("created_at")
    val createdAt: String? = null,
    @SerialName("updated_at")
    val updatedAt: String? = null
)

@Serializable
data class ProfileUpsertDto(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("email")
    val email: String? = null,
    @SerialName("college")
    val college: String,
    @SerialName("degree")
    val degree: String,
    @SerialName("graduation_year")
    val graduationYear: Int,
    @SerialName("target_roles")
    val targetRoles: List<String>
)

@Serializable
data class JobDto(
    @SerialName("id")
    val id: String,
    @SerialName("title")
    val title: String,
    @SerialName("company")
    val company: String,
    @SerialName("location")
    val location: String? = null,
    @SerialName("work_mode")
    val workMode: String? = null,
    @SerialName("employment_type")
    val employmentType: String? = null,
    @SerialName("stipend")
    val stipend: String? = null,
    @SerialName("apply_url")
    val applyUrl: String? = null,
    @SerialName("deadline")
    val deadline: String? = null,
    @SerialName("description_raw")
    val descriptionRaw: String? = null,
    @SerialName("description_clean")
    val descriptionClean: String? = null,
    @SerialName("tags")
    val tags: List<String> = emptyList(),
    @SerialName("is_featured")
    val isFeatured: Boolean = false,
    @SerialName("is_active")
    val isActive: Boolean = true,
    @SerialName("created_by")
    val createdBy: String? = null,
    @SerialName("created_at")
    val createdAt: String? = null,
    @SerialName("updated_at")
    val updatedAt: String? = null
)

@Serializable
data class JobAnalysisDto(
    @SerialName("id")
    val id: String,
    @SerialName("job_id")
    val jobId: String,
    @SerialName("summary")
    val summary: String? = null,
    @SerialName("role_reality")
    val roleReality: String? = null,
    @SerialName("required_skills")
    val requiredSkills: List<String> = emptyList(),
    @SerialName("preferred_skills")
    val preferredSkills: List<String> = emptyList(),
    @SerialName("top_keywords")
    val topKeywords: List<String> = emptyList(),
    @SerialName("likely_interview_topics")
    val likelyInterviewTopics: List<String> = emptyList(),
    @SerialName("difficulty")
    val difficulty: String? = null
)

@Serializable
data class ResumeDto(
    @SerialName("id")
    val id: String,
    @SerialName("user_id")
    val userId: String,
    @SerialName("file_url")
    val fileUrl: String? = null,
    @SerialName("file_name")
    val fileName: String? = null,
    @SerialName("parsed_text")
    val parsedText: String? = null,
    @SerialName("parsed_sections")
    val parsedSections: JsonElement? = null,
    @SerialName("latest_score")
    val latestScore: Int? = null,
    @SerialName("created_at")
    val createdAt: String? = null,
    @SerialName("updated_at")
    val updatedAt: String? = null
)

@Serializable
data class ResumeRoastDto(
    @SerialName("id")
    val id: String,
    @SerialName("user_id")
    val userId: String,
    @SerialName("resume_id")
    val resumeId: String,
    @SerialName("target_job_id")
    val targetJobId: String? = null,
    @SerialName("overall_score")
    val overallScore: Int? = null,
    @SerialName("ats_score")
    val atsScore: Int? = null,
    @SerialName("relevance_score")
    val relevanceScore: Int? = null,
    @SerialName("clarity_score")
    val clarityScore: Int? = null,
    @SerialName("formatting_score")
    val formattingScore: Int? = null,
    @SerialName("roast_result")
    val roastResult: JsonElement? = null,
    @SerialName("created_at")
    val createdAt: String? = null
)

@Serializable
data class SavedJobDto(
    @SerialName("id")
    val id: String,
    @SerialName("user_id")
    val userId: String,
    @SerialName("job_id")
    val jobId: String,
    @SerialName("status")
    val status: String,
    @SerialName("created_at")
    val createdAt: String? = null
)

@Serializable
data class SavedJobUpsertDto(
    @SerialName("user_id")
    val userId: String,
    @SerialName("job_id")
    val jobId: String,
    @SerialName("status")
    val status: String = "saved"
)

@Serializable
data class MockSessionDto(
    @SerialName("id")
    val id: String,
    @SerialName("user_id")
    val userId: String,
    @SerialName("target_job_id")
    val targetJobId: String? = null,
    @SerialName("role_name")
    val roleName: String? = null,
    @SerialName("difficulty")
    val difficulty: String? = null,
    @SerialName("mode")
    val mode: String? = null,
    @SerialName("overall_score")
    val overallScore: Int? = null,
    @SerialName("created_at")
    val createdAt: String? = null,
    @SerialName("updated_at")
    val updatedAt: String? = null
)
