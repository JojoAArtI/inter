package com.internshipuncle.data.repository

import com.internshipuncle.core.network.AppConfig
import com.internshipuncle.data.dto.JobDto
import com.internshipuncle.data.dto.MockSessionDto
import com.internshipuncle.data.dto.ResumeDto
import com.internshipuncle.data.dto.SavedJobDto
import com.internshipuncle.data.remote.SupabaseTables
import com.internshipuncle.domain.model.DashboardSnapshot
import io.github.jan.supabase.postgrest.Postgrest
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface DashboardRepository {
    fun snapshot(): Flow<DashboardSnapshot>
}

class SupabaseDashboardRepository @Inject constructor(
    private val appConfig: AppConfig,
    private val postgrest: Postgrest
) : DashboardRepository {

    override fun snapshot(): Flow<DashboardSnapshot> = flow {
        emit(buildSnapshot())
    }

    private suspend fun buildSnapshot(): DashboardSnapshot {
        if (!appConfig.isSupabaseConfigured) {
            return DashboardSnapshot(
                readinessScore = null,
                savedJobsCount = 0,
                resumesCount = 0,
                mockSessionsCount = 0,
                nextDeadline = null,
                isConfigured = false
            )
        }

        return try {
            val savedJobs = postgrest.from(SupabaseTables.SAVED_JOBS)
                .select()
                .decodeList<SavedJobDto>()
            val resumes = postgrest.from(SupabaseTables.RESUMES)
                .select()
                .decodeList<ResumeDto>()
            val mockSessions = postgrest.from(SupabaseTables.MOCK_SESSIONS)
                .select()
                .decodeList<MockSessionDto>()
            val jobs = postgrest.from(SupabaseTables.JOBS)
                .select {
                    filter {
                        eq("is_active", true)
                    }
                }
                .decodeList<JobDto>()

            DashboardSnapshot(
                readinessScore = null,
                savedJobsCount = savedJobs.size,
                resumesCount = resumes.size,
                mockSessionsCount = mockSessions.size,
                nextDeadline = jobs.mapNotNull(JobDto::deadline).sorted().firstOrNull(),
                isConfigured = true
            )
        } catch (_: Exception) {
            DashboardSnapshot(
                readinessScore = null,
                savedJobsCount = 0,
                resumesCount = 0,
                mockSessionsCount = 0,
                nextDeadline = null,
                isConfigured = true
            )
        }
    }
}
