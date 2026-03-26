package com.buildndeploy.herflow.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [CycleEntryEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(LocalDateConverter::class)
abstract class HerFlowDatabase : RoomDatabase() {
    abstract fun cycleDao(): CycleDao
}
