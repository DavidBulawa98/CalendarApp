package com.dbulawa.calendar.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dbulawa.calendar.R
import com.dbulawa.calendar.adapter.DayOfTheWeekAdapter
import com.dbulawa.calendar.authorization.GoogleAuthService
import com.dbulawa.calendar.calendar.CalendarViewHelper
import com.dbulawa.calendar.calendar.DayViewContainer
import com.dbulawa.calendar.calendar.MonthViewContainer
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.kizitonwose.calendarview.CalendarView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import dagger.hilt.android.AndroidEntryPoint
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class CalendarFragment : Fragment() {

    @Inject lateinit var googleAuthService: GoogleAuthService
    @Inject lateinit var dayOfTheWeekAdapter: DayOfTheWeekAdapter
    @Inject lateinit var calendarViewHelper: CalendarViewHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.calendar_fragment, container, false)

        initViews(view)

        initObjects()

        initListeners(view)

        return view
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GoogleAuthService.RC_SIGN_IN) {
            this.googleAuthService.handleSignInResult()
        }
    }

    private fun initViews(view: View){
        val recyclerView : RecyclerView = view.findViewById(R.id.days_of_week)
        val calendarView : CalendarView = view.findViewById(R.id.calendarView)
        val monthTextView : TextView = view.findViewById(R.id.MonthText)
        val layoutManager = FlexboxLayoutManager(requireContext(), FlexDirection.ROW, FlexWrap.NOWRAP)

        recyclerView.suppressLayout(false)
        recyclerView.adapter = dayOfTheWeekAdapter;
        recyclerView.layoutManager = layoutManager

        calendarViewHelper.setupCalendarView(calendarView, monthTextView)
    }

    /**
     * Init Objects
     */
    private fun initObjects(){

    }

    /**
     *  Init Listeners
     */
    private fun initListeners(view: View){

    }

    private fun initSignInClient() : GoogleSignInClient{
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(GoogleAuthService.SCOPE_CALENDAR_EVENTS)
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(this.requireActivity(), gso)
    }
}
