package com.dbulawa.calendar.calendar

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dbulawa.calendar.R
import com.dbulawa.calendar.ui.main.calendar.CalendarViewModel
import com.kizitonwose.calendarview.CalendarView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.ui.DayBinder
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class CalendarViewHelper @Inject constructor(
    val fragment: Fragment
) {

    @Inject @Named("MonthsOfTheYear") lateinit var monthsOfTheYear: Array<String>

    private val viewModel: CalendarViewModel by lazy { ViewModelProvider(fragment).get(
        CalendarViewModel::class.java) }
    val currentMonth = YearMonth.now()
    val firstMonth = currentMonth.minusMonths(10)
    val lastMonth = currentMonth.plusMonths(10)
    val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
    var selectedDayView: View? = null;


    fun setupCalendarView(calendarView: CalendarView, monthTextView : TextView) {
        calendarView.setup(firstMonth, lastMonth, firstDayOfWeek)
        calendarView.scrollToMonth(currentMonth)

        calendarView.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)

            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.dayTextView.text = day.day.toString()
                container.view.setOnClickListener {
                    if(viewModel.activeDay.value == null){
                        viewModel.activeDay.postValue(day)
                        selectedDayView = container.view;
                        container.view.setBackgroundColor(fragment.resources.getColor(R.color.colorAccent, fragment.activity?.theme));
                    }else if(viewModel.activeDay.value!! == day){
                        viewModel.activeDay.postValue(null)
                        container.view.setBackgroundColor(fragment.resources.getColor(R.color.colorWhite, fragment.activity?.theme));
                    }else{
                        selectedDayView?.setBackgroundColor(fragment.resources.getColor(R.color.colorWhite, fragment.activity?.theme));
                        selectedDayView = container.view;
                        viewModel.activeDay.postValue(day)
                        container.view.setBackgroundColor(fragment.resources.getColor(R.color.colorAccent, fragment.activity?.theme));
                    }
                }
            }
        }
        calendarView.monthScrollListener = {
            monthTextView.text = monthsOfTheYear[it.month - 1] + "  " +  it.year //Array indexed from 0, but months begin with 1
        }

        viewModel.activeDay.observe(fragment, androidx.lifecycle.Observer {
            Log.i("DAY_SELECTED", "User selected ${it.toString()}")
        })

    }
}