package com.internshipuncle.data.di

import com.internshipuncle.core.network.AppConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.functions.Functions
import io.github.jan.supabase.functions.functions
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SupabaseModule {

    private const val FALLBACK_SUPABASE_URL = "https://example.supabase.co"
    private const val FALLBACK_SUPABASE_PUBLIC_KEY = "sb_publishable_placeholder"

    @Provides
    @Singleton
    fun provideSupabaseClient(
        appConfig: AppConfig
    ): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = appConfig.supabaseUrl.ifBlank { FALLBACK_SUPABASE_URL },
            supabaseKey = appConfig.supabasePublicKey.ifBlank { FALLBACK_SUPABASE_PUBLIC_KEY }
        ) {
            install(Postgrest)
            install(Auth)
            install(Storage)
            install(Functions)
        }
    }

    @Provides
    @Singleton
    fun provideSupabaseAuth(
        client: SupabaseClient
    ): Auth = client.auth

    @Provides
    @Singleton
    fun provideSupabasePostgrest(
        client: SupabaseClient
    ): Postgrest = client.postgrest

    @Provides
    @Singleton
    fun provideSupabaseStorage(
        client: SupabaseClient
    ): Storage = client.storage

    @Provides
    @Singleton
    fun provideSupabaseFunctions(
        client: SupabaseClient
    ): Functions = client.functions
}
