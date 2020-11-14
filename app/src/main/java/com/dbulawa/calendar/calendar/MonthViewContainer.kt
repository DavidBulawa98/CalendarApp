package com.dbulawa.calendar.calendar

import android.view.View
import android.widget.TextView
import com.dbulawa.calendar.R
import com.kizitonwose.calendarview.ui.ViewContainer

class MonthViewContainer(view : View) : ViewContainer(view) {
    var monthTextView = view.findViewById<TextView>(R.id.MonthText)

    // With ViewBinding
    // val textView = CalendarDayLayoutBinding.bind(view).calendarDayText

}