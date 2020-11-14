package com.dbulawa.calendar.ui.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kizitonwose.calendarview.model.CalendarDay

class MainViewModel @ViewModelInject constructor(): ViewModel() {
    var activeDay : MutableLiveData<CalendarDay?> = MutableLiveData();
}
