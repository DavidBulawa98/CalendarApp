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
    var events : MutableLiveData<List<Event>> = MutableLiveData()
    suspend fun insertEvent(event: Event){
        eventDao.insertEvent(convert(event))
    }

     suspend fun getEvent(id : String) : LiveData<Event>{
         return convert(eventDao.get(id))
    }

    suspend fun getEventsByDay(date : Date){
        withContext(Dispatchers.IO) {
            events.postValue(eventDao.getEventsByDay(date).map { convert(it) })
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
        return Transformations.switchMap(livedata) {
            val events : List<Event> = it.map { convert(it) }
            val result: MutableLiveData<List<Event>> = MutableLiveData()
            result.postValue(events)
            result
        }
    }

    private fun convert(event: Event) : EventEntity{
        if(event.id == null){
            return EventEntity(title = event.title!!, date = event.date!!, time = event.time!!, desc = event.desc)
        }else{
            return EventEntity(event.id!!, title = event.title!!, date = event.date!!, time = event.time!!,  desc = event.desc)
        }

    }

    private fun convert(event: EventEntity) : Event{
        return Event(id = event.id, title = event.title, date = event.date, time = event.time, desc = event.desc)
    }
//
//    private fun dateTime(date: Date?, time: Date?): Date {
//        val dateCal = Calendar.getInstance()
//        val timeCal = Calendar.getInstance()
//        val dateTimeCal = Calendar.getInstance()
//        dateCal.time = date!!
//        timeCal.time = time!!
//        dateTimeCal[Calendar.DAY_OF_MONTH] = dateCal[Calendar.DAY_OF_MONTH]
//        dateTimeCal[Calendar.MONTH] = dateCal[Calendar.MONTH]
//        dateTimeCal[Calendar.YEAR] = dateCal[Calendar.YEAR]
//        dateTimeCal[Calendar.HOUR] = timeCal[Calendar.HOUR]
//        dateTimeCal[Calendar.MINUTE] = timeCal[Calendar.MINUTE]
//        dateTimeCal[Calendar.SECOND] = timeCal[Calendar.SECOND]
//        return dateTimeCal.time
//    }
}