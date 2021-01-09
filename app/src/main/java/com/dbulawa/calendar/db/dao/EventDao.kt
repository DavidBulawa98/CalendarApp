package com.dbulawa.calendar.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dbulawa.calendar.db.entity.EventEntity
import java.util.*

@Dao
interface EventDao {

    @Query("SELECT * from event WHERE id=:id ")
    fun get(id : String) : LiveData<EventEntity>

    @Query("SELECT * FROM event WHERE date=:date")
    fun getEventsByDay(date: Date): List<EventEntity>

    @Query("SELECT * FROM event")
    fun getEvents(): LiveData<List<EventEntity>>

    @Query("SELECT * FROM event")
    fun getEventsNoLive(): List<EventEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(eventEntity: EventEntity)

    @Delete
    suspend fun deleteEvent(eventEntity: EventEntity)
}