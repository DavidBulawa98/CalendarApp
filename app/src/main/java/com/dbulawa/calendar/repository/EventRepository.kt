package com.dbulawa.calendar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.dbulawa.calendar.db.dao.EventDao
import com.dbulawa.calendar.db.entity.EventEntity
import com.dbulawa.calendar.repository.data.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class EventRepository @Inject constructor(
    private val eventDao: EventDao
) {
    var activeDay: MutableLiveData<Date> = MutableLiveData()
    var events : LiveData<List<Event>> = Transformations.switchMap(activeDay) {
        convertList(eventDao.getEventsByDay(it))
    }

    suspend fun insertEvent(event: Event){
        withContext(Dispatchers.IO) {
            eventDao.insertEvent(convert(event))
            activeDay.postValue(activeDay.value)
        }
    }

     suspend fun getEvent(id : String) : LiveData<Event>{
         return convert(eventDao.get(id))
    }

    suspend fun deleteEvent(id : Int){
        withContext(Dispatchers.IO) {
            eventDao.deleteById(id)
            activeDay.postValue(activeDay.value)
        }
    }

    suspend fun getEventsByDay(date : Date){
        withContext(Dispatchers.IO) {
            activeDay.postValue(date)
        }
    }

    fun getAll() : List<Event>{
        return eventDao.getEventsNoLive().map { convert(it) }
    }

    // ===== Auxiliarry methods


    private fun convert(livedata : LiveData<EventEntity>) : LiveData<Event>{
        return Transformations.switchMap(livedata) {
            val event = convert(it)
            val result: MutableLiveData<Event> = MutableLiveData()
            result.postValue(event)
            result
        }
    }

    private fun convertList(livedata : LiveData<List<EventEntity>>) : LiveData<List<Event>>{
        return Transformations.map(livedata) {
            val events : List<Event> = it.map { convert(it) }
            events
        }
    }

    fun convert(event: Event) : EventEntity{
        if(event.id == null){
            return EventEntity(title = event.title!!, date = event.date!!, time = event.time!!, desc = event.desc)
        }else{
            return EventEntity(event.id!!, title = event.title!!, date = event.date!!, time = event.time!!,  desc = event.desc)
        }

    }

    private fun convert(event: EventEntity) : Event{
        return Event(id = event.id, title = event.title, date = event.date, time = event.time, desc = event.desc)
    }
}