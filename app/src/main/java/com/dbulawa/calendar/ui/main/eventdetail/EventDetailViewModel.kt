package com.dbulawa.calendar.ui.main.eventdetail

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.dbulawa.calendar.db.entity.EventEntity
import com.dbulawa.calendar.repository.EventRepository
import com.dbulawa.calendar.repository.data.Event
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.util.*

class EventDetailViewModel @ViewModelInject constructor(
    @Assisted private val stateHandle: SavedStateHandle,
    var eventRepository: EventRepository
): ViewModel() {

    var success : MutableLiveData<Boolean> = MutableLiveData()
    var event : LiveData<Event>  = MutableLiveData<Event>().also { it.postValue(Event()) }

    suspend fun getEvent(id: String) {
        viewModelScope.launch {
            event = eventRepository.getEvent(id)
        }
    }

    fun insertEvent(){
            viewModelScope.launch {
                try {
                    eventRepository.insertEvent(event.value!!)
                    success.postValue(true)
                }catch (ex : KotlinNullPointerException){
                    success.postValue(false)
                }
            }
    }
}