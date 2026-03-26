package com.buildndeploy.herflow.di

import com.buildndeploy.herflow.data.repository.CycleRepositoryImpl
import com.buildndeploy.herflow.domain.repository.CycleRepository
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
    abstract fun bindCycleRepository(impl: CycleRepositoryImpl): CycleRepository
}
