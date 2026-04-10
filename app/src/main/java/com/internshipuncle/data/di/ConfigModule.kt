package com.internshipuncle.data.di

import com.internshipuncle.core.network.AppConfig
import com.internshipuncle.core.network.BuildConfigAppConfig
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ConfigModule {

    @Binds
    @Singleton
    abstract fun bindAppConfig(
        impl: BuildConfigAppConfig
    ): AppConfig
}
