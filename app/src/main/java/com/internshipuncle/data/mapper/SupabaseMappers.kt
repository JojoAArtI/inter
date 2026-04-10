package com.internshipuncle.data.mapper

import com.internshipuncle.data.dto.JobAnalysisDto
import com.internshipuncle.data.dto.JobDto
import com.internshipuncle.data.dto.MockSessionDto
import com.internshipuncle.data.dto.ProfileDto
import com.internshipuncle.data.dto.ProfileUpsertDto
import com.internshipuncle.data.dto.ResumeDto
import com.internshipuncle.data.dto.ResumeRoastDto
import com.internshipuncle.domain.model.InterviewSessionSummary
import com.internshipuncle.domain.model.JobAnalysis
import com.internshipuncle.domain.model.JobCard
import com.internshipuncle.domain.model.JobDetail
import com.internshipuncle.domain.model.OnboardingProfileInput
import com.internshipuncle.domain.model.ResumeRoastSummary
import com.internshipuncle.domain.model.ResumeSummary
import com.internshipuncle.domain.model.UserProfile

fun ProfileDto.toDomainModel(): UserProfile {
    return UserProfile(
        id = id,
        email = email,
        name = name ?: email?.substringBefore("@").orEmpty(),
        college = college.orEmpty(),
        degree = degree.orEmpty(),
        graduationYear = graduationYear,
        targetRoles = targetRoles.map(String::trim).filter(String::isNotBlank)
    )
}

fun OnboardingProfileInput.toUpsertDto(
    userId: String,
    email: String?
): ProfileUpsertDto {
    return ProfileUpsertDto(
        id = userId,
        name = name.trim(),
        email = email?.trim(),
        college = college.trim(),
        degree = degree.trim(),
        graduationYear = graduationYear,
        targetRoles = targetRoles.map(String::trim).filter(String::isNotBlank)
    )
}

fun JobDto.toDomainModel(): JobCard {
    return JobCard(
        id = id,
        title = title,
        company = company,
        location = location?.takeIf(String::isNotBlank),
        stipend = stipend?.takeIf(String::isNotBlank),
        workMode = workMode?.takeIf(String::isNotBlank),
        deadline = deadline,
        tags = tags.map(String::trim).filter(String::isNotBlank),
        isFeatured = isFeatured
    )
}

fun JobDto.toDetailModel(): JobDetail {
    return JobDetail(
        id = id,
        title = title,
        company = company,
        location = location?.takeIf(String::isNotBlank),
        workMode = workMode?.takeIf(String::isNotBlank),
        employmentType = employmentType?.takeIf(String::isNotBlank),
        stipend = stipend?.takeIf(String::isNotBlank),
        applyUrl = applyUrl?.takeIf(String::isNotBlank),
        deadline = deadline,
        description = descriptionClean?.takeIf(String::isNotBlank)
            ?: descriptionRaw?.takeIf(String::isNotBlank),
        tags = tags.map(String::trim).filter(String::isNotBlank),
        isFeatured = isFeatured
    )
}

fun JobAnalysisDto.toDomainModel(): JobAnalysis {
    return JobAnalysis(
        summary = summary.orEmpty(),
        roleReality = roleReality.orEmpty(),
        requiredSkills = requiredSkills.map(String::trim).filter(String::isNotBlank),
        preferredSkills = preferredSkills.map(String::trim).filter(String::isNotBlank),
        topKeywords = topKeywords.map(String::trim).filter(String::isNotBlank),
        likelyInterviewTopics = likelyInterviewTopics.map(String::trim).filter(String::isNotBlank),
        difficulty = difficulty?.takeIf(String::isNotBlank)
    )
}

fun ResumeDto.toDomainModel(): ResumeSummary {
    return ResumeSummary(
        id = id,
        fileName = fileName,
        latestScore = latestScore,
        createdAt = createdAt
    )
}

fun ResumeRoastDto.toDomainModel(): ResumeRoastSummary {
    return ResumeRoastSummary(
        resumeId = resumeId,
        overallScore = overallScore,
        atsScore = atsScore,
        relevanceScore = relevanceScore,
        clarityScore = clarityScore,
        formattingScore = formattingScore
    )
}

fun MockSessionDto.toDomainModel(): InterviewSessionSummary {
    return InterviewSessionSummary(
        id = id,
        roleName = roleName,
        difficulty = difficulty,
        mode = mode,
        overallScore = overallScore,
        createdAt = createdAt
    )
}
