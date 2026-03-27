package com.buildndeploy.herflow.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [CycleEntryEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(LocalDateConverter::class)
abstract class HerFlowDatabase : RoomDatabase() {
    abstract fun cycleDao(): CycleDao
}
