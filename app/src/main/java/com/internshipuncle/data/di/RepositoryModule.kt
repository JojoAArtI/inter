package com.internshipuncle.data.di

import com.internshipuncle.data.repository.AuthRepository
import com.internshipuncle.data.repository.DashboardRepository
import com.internshipuncle.data.repository.InterviewRepository
import com.internshipuncle.data.repository.JobsRepository
import com.internshipuncle.data.repository.ResumeRepository
import com.internshipuncle.data.repository.SupabaseAuthRepository
import com.internshipuncle.data.repository.SupabaseDashboardRepository
import com.internshipuncle.data.repository.SupabaseInterviewRepository
import com.internshipuncle.data.repository.SupabaseJobsRepository
import com.internshipuncle.data.repository.SupabaseResumeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: SupabaseAuthRepository
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindJobsRepository(
        impl: SupabaseJobsRepository
    ): JobsRepository

    @Binds
    @Singleton
    abstract fun bindResumeRepository(
        impl: SupabaseResumeRepository
    ): ResumeRepository

    @Binds
    @Singleton
    abstract fun bindInterviewRepository(
        impl: SupabaseInterviewRepository
    ): InterviewRepository

    @Binds
    @Singleton
    abstract fun bindDashboardRepository(
        impl: SupabaseDashboardRepository
    ): DashboardRepository
}
