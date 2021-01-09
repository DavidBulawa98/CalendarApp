package com.dbulawa.calendar.db

import androidx.room.*
import com.dbulawa.calendar.db.converters.Converters
import com.dbulawa.calendar.db.dao.EventDao
import com.dbulawa.calendar.db.entity.EventEntity

@Database(entities = [EventEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class DatabaseManager : RoomDatabase() {
    abstract fun eventDao() : EventDao
}