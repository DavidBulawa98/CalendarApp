package com.dbulawa.calendar.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dbulawa.calendar.db.entity.EventEntity
import java.util.*

@Dao
interface EventDao {

    @Query("SELECT * FROM event WHERE date=:date order by time ASC")
    fun getEventsByDay(date: Date): LiveData<List<EventEntity>>

    @Query("SELECT * FROM event WHERE date=:date AND sended = '0'")
    fun getEventsByDayNotSended(date: Date): List<EventEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(eventEntity: EventEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(eventEntity: EventEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllEvents(eventEntity: List<EventEntity>)

    @Query("DELETE FROM event WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM event WHERE id NOT IN(:ids) AND sended = 'true'")
    suspend fun deleteByIdsNotIn(ids: List<String>)
}