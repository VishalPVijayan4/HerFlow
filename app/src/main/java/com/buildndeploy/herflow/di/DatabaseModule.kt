package com.buildndeploy.herflow.di

import android.content.Context
import androidx.room.Room
import com.buildndeploy.herflow.data.local.CycleDao
import com.buildndeploy.herflow.data.local.HerFlowDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): HerFlowDatabase =
        Room.databaseBuilder(
            context,
            HerFlowDatabase::class.java,
            "herflow.db"
        )
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideCycleDao(database: HerFlowDatabase): CycleDao = database.cycleDao()
}
