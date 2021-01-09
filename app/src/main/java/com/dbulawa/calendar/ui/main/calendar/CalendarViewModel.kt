package com.dbulawa.calendar.ui.main.calendar

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.dbulawa.calendar.repository.EventRepository
import com.dbulawa.calendar.repository.data.Event
import com.kizitonwose.calendarview.model.CalendarDay
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.temporal.TemporalQueries.localDate
import java.util.*


class CalendarViewModel @ViewModelInject constructor(
    @Assisted private val stateHandle: SavedStateHandle,
    var eventRepository: EventRepository
): ViewModel() {
    var activeDay : MutableLiveData<CalendarDay?> = MutableLiveData();
    var events : LiveData<List<Event>> = eventRepository.events

    suspend fun getEventsByActiveDay(date: Date) : LiveData<List<Event>>{
        viewModelScope.launch {
            eventRepository.getEventsByDay(date)
        }
        return events
    }

    fun getAll() : List<Event>{
        return eventRepository.getAll()
    }

}
