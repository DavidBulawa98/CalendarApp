package com.dbulawa.calendar.calendar

import android.view.View
import android.widget.TextView
import com.dbulawa.calendar.R
import com.kizitonwose.calendarview.ui.ViewContainer

class DayViewContainer(view: View) : ViewContainer(view) {
    val dayTextView = view.findViewById<TextView>(R.id.calendarDayText)
}