package com.internshipuncle.core.network

import com.internshipuncle.BuildConfig
import javax.inject.Inject

interface AppConfig {
    val supabaseUrl: String
    val supabasePublicKey: String
    val isSupabaseConfigured: Boolean
}

class BuildConfigAppConfig @Inject constructor() : AppConfig {
    override val supabaseUrl: String = BuildConfig.SUPABASE_URL.trim()
    override val supabasePublicKey: String = BuildConfig.SUPABASE_PUBLIC_KEY.trim()
    override val isSupabaseConfigured: Boolean = BuildConfig.SUPABASE_CONFIGURED
}
