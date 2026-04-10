package com.internshipuncle.data.repository

import com.internshipuncle.core.model.RepositoryStatus
import com.internshipuncle.core.network.AppConfig
import com.internshipuncle.data.dto.ResumeDto
import com.internshipuncle.data.dto.ResumeRoastDto
import com.internshipuncle.data.mapper.toDomainModel
import com.internshipuncle.data.remote.SupabaseTables
import com.internshipuncle.domain.model.ResumeRoastSummary
import com.internshipuncle.domain.model.ResumeSummary
import io.github.jan.supabase.functions.Functions
import io.github.jan.supabase.postgrest.Postgrest
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface ResumeRepository {
    fun resumes(): Flow<List<ResumeSummary>>
    fun roastSummary(resumeId: String): Flow<ResumeRoastSummary?>
    suspend fun requestResumeParsing(
        resumeId: String,
        fileUrl: String
    ): RepositoryStatus

    suspend fun requestResumeRoast(
        resumeId: String,
        targetJobId: String?,
        mode: String
    ): RepositoryStatus
}

class SupabaseResumeRepository @Inject constructor(
    private val appConfig: AppConfig,
    private val postgrest: Postgrest,
    private val functions: Functions
) : ResumeRepository {

    override fun resumes(): Flow<List<ResumeSummary>> = flow {
        emit(fetchResumes())
    }

    override fun roastSummary(
        resumeId: String
    ): Flow<ResumeRoastSummary?> = flow {
        emit(fetchRoastSummary(resumeId))
    }

    override suspend fun requestResumeParsing(
        resumeId: String,
        fileUrl: String
    ): RepositoryStatus {
        if (!appConfig.isSupabaseConfigured) {
            return RepositoryStatus.NotConfigured
        }

        if (resumeId.isBlank() || fileUrl.isBlank()) {
            return RepositoryStatus.Failure("Resume parse requires a resume id and file URL.")
        }

        return RepositoryStatus.BackendNotReady
    }

    override suspend fun requestResumeRoast(
        resumeId: String,
        targetJobId: String?,
        mode: String
    ): RepositoryStatus {
        if (!appConfig.isSupabaseConfigured) {
            return RepositoryStatus.NotConfigured
        }

        if (resumeId.isBlank() || mode.isBlank()) {
            return RepositoryStatus.Failure("Resume roast requires a resume id and mode.")
        }

        return RepositoryStatus.BackendNotReady
    }

    private suspend fun fetchResumes(): List<ResumeSummary> {
        if (!appConfig.isSupabaseConfigured) {
            return emptyList()
        }

        return try {
            postgrest.from(SupabaseTables.RESUMES)
                .select()
                .decodeList<ResumeDto>()
                .map(ResumeDto::toDomainModel)
        } catch (_: Exception) {
            emptyList()
        }
    }

    private suspend fun fetchRoastSummary(
        resumeId: String
    ): ResumeRoastSummary? {
        if (!appConfig.isSupabaseConfigured || resumeId.isBlank()) {
            return null
        }

        return try {
            postgrest.from(SupabaseTables.RESUME_ROASTS)
                .select {
                    filter {
                        eq("resume_id", resumeId)
                    }
                }
                .decodeList<ResumeRoastDto>()
                .firstOrNull()
                ?.toDomainModel()
        } catch (_: Exception) {
            null
        }
    }
}
