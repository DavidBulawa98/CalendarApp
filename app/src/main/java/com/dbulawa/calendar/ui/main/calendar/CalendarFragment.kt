package com.dbulawa.calendar.ui.main.calendar

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.marginTop
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dbulawa.calendar.R
import com.dbulawa.calendar.adapter.DayOfTheWeekAdapter
import com.dbulawa.calendar.adapter.EventAdapter
import com.dbulawa.calendar.authorization.GoogleAuthService
import com.dbulawa.calendar.calendar.CalendarViewHelper
import com.dbulawa.calendar.databinding.CalendarFragmentBinding
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.kizitonwose.calendarview.CalendarView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class CalendarFragment : Fragment() {

    @Inject lateinit var googleAuthService: GoogleAuthService
    @Inject lateinit var dayOfTheWeekAdapter: DayOfTheWeekAdapter
    @Inject lateinit var calendarViewHelper: CalendarViewHelper
    @Inject lateinit var eventAdapter: EventAdapter
    lateinit var binding: CalendarFragmentBinding
    private val viewModel: CalendarViewModel by lazy { ViewModelProvider(this).get(CalendarViewModel::class.java) }
    lateinit var calendarView : CalendarView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.calendar_fragment, container, false)
        binding = DataBindingUtil.setContentView(requireActivity(), R.layout.calendar_fragment);
        initViews(view)

        initObjects()

        initListeners(view)

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GoogleAuthService.RC_SIGN_IN) {
            this.googleAuthService.handleSignInResult()
        }
    }

    private fun initViews(view: View){
        val recyclerView : RecyclerView = view.findViewById(R.id.days_of_week)
        val eventRecyclerView : RecyclerView = binding.eventsRecycler
        calendarView = view.findViewById(R.id.calendarView)
        val monthTextView : TextView = view.findViewById(R.id.MonthText)
        val layoutManager = FlexboxLayoutManager(requireContext(), FlexDirection.ROW, FlexWrap.NOWRAP)

        calendarViewHelper.setupCalendarView(calendarView, monthTextView)

        recyclerView.suppressLayout(false)
        recyclerView.adapter = dayOfTheWeekAdapter;
        recyclerView.layoutManager = layoutManager

        eventRecyclerView.adapter = eventAdapter
        eventRecyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)


        val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(Resources.getSystem().displayMetrics.heightPixels, View.MeasureSpec.AT_MOST);
        val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(Resources.getSystem().displayMetrics.widthPixels, View.MeasureSpec.AT_MOST);
        calendarView.measure(widthMeasureSpec, heightMeasureSpec)

        val params1: LinearLayout.LayoutParams =
            calendarView.layoutParams as LinearLayout.LayoutParams
        params1.height = (calendarView.daySize.height * 6.5).toInt()
        calendarView.layoutParams = params1

        val params: LinearLayout.LayoutParams =
            eventRecyclerView.layoutParams as LinearLayout.LayoutParams
        params.topMargin = (calendarView.daySize.height * 6.5).toInt()
        eventRecyclerView.layoutParams = params
    }

    /**
     * Init Objects
     */
    private fun initObjects(){

        viewModel.events.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            eventAdapter.events = it
            eventAdapter.notifyDataSetChanged()
        })
        viewModel.activeDay.observe(viewLifecycleOwner, androidx.lifecycle.Observer{
            it?.let {
                val date: Date = Date.from(it.date.atStartOfDay(ZoneId.systemDefault()).toInstant())
                lifecycleScope.launch {
                    viewModel.getEventsByActiveDay(date)
                }
            }
        })
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
