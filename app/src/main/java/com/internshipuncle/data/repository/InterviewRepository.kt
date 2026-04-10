package com.internshipuncle.data.repository

import com.internshipuncle.core.model.RepositoryStatus
import com.internshipuncle.core.network.AppConfig
import com.internshipuncle.data.dto.MockSessionDto
import com.internshipuncle.data.mapper.toDomainModel
import com.internshipuncle.data.remote.SupabaseTables
import com.internshipuncle.domain.model.InterviewSessionSummary
import io.github.jan.supabase.functions.Functions
import io.github.jan.supabase.postgrest.Postgrest
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface InterviewRepository {
    fun recentSessions(): Flow<List<InterviewSessionSummary>>
    suspend fun requestMockSession(
        targetJobId: String?,
        roleName: String,
        difficulty: String,
        mode: String
    ): RepositoryStatus
}

class SupabaseInterviewRepository @Inject constructor(
    private val appConfig: AppConfig,
    private val postgrest: Postgrest,
    private val functions: Functions
) : InterviewRepository {

    override fun recentSessions(): Flow<List<InterviewSessionSummary>> = flow {
        emit(fetchSessions())
    }

    override suspend fun requestMockSession(
        targetJobId: String?,
        roleName: String,
        difficulty: String,
        mode: String
    ): RepositoryStatus {
        if (!appConfig.isSupabaseConfigured) {
            return RepositoryStatus.NotConfigured
        }

        if (roleName.isBlank() || difficulty.isBlank() || mode.isBlank()) {
            return RepositoryStatus.Failure("Mock session request requires role, difficulty, and mode.")
        }

        return RepositoryStatus.BackendNotReady
    }

    private suspend fun fetchSessions(): List<InterviewSessionSummary> {
        if (!appConfig.isSupabaseConfigured) {
            return emptyList()
        }

        return try {
            postgrest.from(SupabaseTables.MOCK_SESSIONS)
                .select()
                .decodeList<MockSessionDto>()
                .map(MockSessionDto::toDomainModel)
        } catch (_: Exception) {
            emptyList()
        }
    }
}
