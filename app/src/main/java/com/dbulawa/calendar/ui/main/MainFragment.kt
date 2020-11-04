package com.dbulawa.calendar.ui.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.dbulawa.calendar.R
import com.dbulawa.calendar.authorization.GoogleAuthService
import com.dbulawa.calendar.calendar.DayViewContainer
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.kizitonwose.calendarview.CalendarView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.ui.DayBinder
import dagger.hilt.android.AndroidEntryPoint
import java.time.YearMonth
import java.time.temporal.TemporalQueries.localDate
import java.time.temporal.WeekFields
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class MainFragment : Fragment() {

    @Inject lateinit var googleAuthService: GoogleAuthService
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.main_fragment, container, false)

        initViews(view)

        initObjects()

        initListeners(view)

        return view
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GoogleAuthService.RC_SIGN_IN) {
            this.googleAuthService.handleSignInResult()
        }
    }

    private fun initViews(view: View){
       val calendarView : CalendarView = view.findViewById(R.id.calendarView)
        val currentMonth = YearMonth.now()
        val firstMonth = currentMonth.minusMonths(10)
        val lastMonth = currentMonth.plusMonths(10)
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        calendarView.setup(firstMonth, lastMonth, firstDayOfWeek)
        calendarView.scrollToMonth(currentMonth)
        calendarView.dayBinder = object : DayBinder<DayViewContainer> {
            // Called only when a new container is needed.
            override fun create(view: View) = DayViewContainer(view)

            // Called every time we need to reuse a container.
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.textView.text = day.day.toString()
            }
        }
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
//        val button : Button = view.findViewById(R.id.button)
//        button.setOnClickListener {
//            startActivityForResult(this.googleAuthService.signInClient.signInIntent, GoogleAuthService.RC_SIGN_IN)
//        }
    }

    private fun initSignInClient() : GoogleSignInClient{
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(GoogleAuthService.SCOPE_CALENDAR_EVENTS)
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(this.requireActivity(), gso)
    }
}
