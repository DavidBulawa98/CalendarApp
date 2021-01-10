package com.dbulawa.calendar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.dbulawa.calendar.db.dao.EventDao
import com.dbulawa.calendar.db.entity.EventEntity
import com.dbulawa.calendar.repository.data.Event
import com.dbulawa.calendar.wsclient.GoogleEventService
import com.dbulawa.calendar.wsclient.dto.DateInfoDTO
import com.dbulawa.calendar.wsclient.dto.GoogleEventDTO
import com.google.api.client.util.DateTime
import kotlinx.coroutines.*
import java.io.IOException
import java.lang.Exception
import java.sql.Time
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.inject.Inject

class EventRepository @Inject constructor(
    private val eventDao: EventDao,
    private val eventService: GoogleEventService
) {
    var activeDay: MutableLiveData<Date> = MutableLiveData()
    var events : LiveData<List<Event>> = Transformations.switchMap(activeDay) {
        if(it != null) {
            convertList(eventDao.getEventsByDay(it))
        }else{
            null
        }
    }
    val handler = CoroutineExceptionHandler { _, exception ->
        println("Exception handled $exception")
    }

    suspend fun insertEvent(event: Event){
        GlobalScope.launch(handler){
            val entity = convertToEntity(event)
            withContext(Dispatchers.IO) { eventDao.insertEvent(entity)}
            withContext(Dispatchers.IO) {
                eventService.insertEvent(convertToDTO(entity))
                entity.sended = true
                eventDao.update(entity)
            }
        }
        activeDay.postValue(activeDay.value)
    }

    suspend fun deleteEvent(id : String){
        GlobalScope.launch(handler) {
            eventDao.deleteById(id)
            withContext(Dispatchers.IO)  { eventService.deleteEvent(id) }
        }
        activeDay.postValue(activeDay.value)
    }

    suspend fun getEventsByDay(date : Date){
        GlobalScope.launch(handler){
            val response = eventService.getEvents(DateTime(date), DateTime(incrementDay(date)))
            launch { eventDao.insertAllEvents(response.items.map { convert(it) })}
            launch { eventDao.deleteByIdsNotIn(response.items.map { it.id!! })}
            withContext(Dispatchers.IO)  {
                eventDao.getEventsByDayNotSended(date).forEach {
                    eventService.insertEvent(convertToDTO(it))
                    it.sended = true
                    eventDao.update(it)
                }
            }
        }
        activeDay.postValue(date)
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

    private fun convertToEntity(event: Event) : EventEntity{
        val id = UUID.randomUUID().toString().replace("-", "")
        if(event.id == null){
            return EventEntity(id, title = event.title!!, date = event.date!!, time = event.time!!, desc = event.desc, sended = false)
        }else{
            return EventEntity(event.id!!, title = event.title!!, date = event.date!!, time = event.time!!,  desc = event.desc, sended = false)
        }

    }

    private fun convertToDTO(event: Event) : GoogleEventDTO{
        val dateInfoDTO = DateInfoDTO(dateTime = integrateDate(event.date!!, event.time!!))
        val id = UUID.randomUUID().toString().replace("-", "")
        if(event.id == null){
            return GoogleEventDTO(id = id, summary = event.title!!, start = dateInfoDTO, end = dateInfoDTO, description = event.desc)
        }else{
            return GoogleEventDTO(id = event.id, summary = event.title!!, start = dateInfoDTO, end = dateInfoDTO, description = event.desc)
        }
    }

    private fun convertToDTO(event: EventEntity) : GoogleEventDTO{
        val dateInfoDTO = DateInfoDTO(dateTime = integrateDate(event.date, event.time))
        return GoogleEventDTO(id = event.id, summary = event.title, start = dateInfoDTO, end = dateInfoDTO, description = event.desc)
    }

    private fun convert(event: EventEntity) : Event{
        return Event(id = event.id, title = event.title, date = event.date, time = event.time, desc = event.desc)
    }

    private fun convert(event: GoogleEventDTO) : EventEntity{
        val title = if (event.summary != null) event.summary!! else ""
        val date = if (event.start!!.dateTime != null) separateDate(event.start!!.dateTime!!) else event.start!!.date!!
        val time = if (event.start!!.dateTime != null) separateTime(event.start!!.dateTime!!) else Time(0)
        return EventEntity(id = event.id!!, title = title, date = date, time = time, desc = event.description, sended = true)
    }

    private fun separateTime(date: Date) : Time {
        val timeFormatter = SimpleDateFormat("HH:mm")
        val timeAsString: String = timeFormatter.format(date)
        return Time(timeFormatter.parse(timeAsString).time)
    }

    private fun separateDate(date: Date) : Date {
        val dateFormatter = SimpleDateFormat.getDateInstance()
        val dateAsString: String = dateFormatter.format(date)
        return dateFormatter.parse(dateAsString)
    }

    private fun incrementDay(date: Date) : Date{
        val cal = Calendar.getInstance()
        cal.time = date
        cal.add(Calendar.DATE, 1)
        return cal.time
    }

    private fun integrateDate(date: Date, time: Time) : Date{
        val localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        val localTime = time.toInstant().atZone(ZoneId.systemDefault()).toLocalTime()
        val localDateTime = LocalDateTime.of(localDate, localTime)
        val dateTime = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        return dateTime
    }
}