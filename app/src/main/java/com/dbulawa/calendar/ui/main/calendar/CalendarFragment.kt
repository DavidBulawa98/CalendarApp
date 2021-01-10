package com.dbulawa.calendar.ui.main.calendar

import android.content.Intent
import android.content.res.Resources
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dbulawa.calendar.R
import com.dbulawa.calendar.adapter.EventAdapter
import com.dbulawa.calendar.authorization.GoogleAuthService
import com.dbulawa.calendar.authorization.GoogleAuthService.Companion.RC_SIGN_IN
import com.dbulawa.calendar.calendar.CalendarViewHelper
import com.dbulawa.calendar.databinding.CalendarFragmentBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.kizitonwose.calendarview.CalendarView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class CalendarFragment : Fragment() {
    @Inject lateinit var googleAuthService: GoogleAuthService
    @Inject lateinit var calendarViewHelper: CalendarViewHelper
    @Inject lateinit var eventAdapter: EventAdapter
    lateinit var binding: CalendarFragmentBinding
    private val viewModel: CalendarViewModel by lazy { ViewModelProvider(this).get(CalendarViewModel::class.java) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.calendar_fragment, container, false)
        binding = DataBindingUtil.setContentView(requireActivity(), R.layout.calendar_fragment);

        startActivityForResult(initSignInClient().signInIntent, RC_SIGN_IN);

        initViews(view)

        initObjects()

        initListeners(view)
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GoogleAuthService.RC_SIGN_IN) {
            AsyncTask.execute{
                googleAuthService.handleSignInResult()
            }
        }
    }

    private fun initViews(view: View){
        val eventRecyclerView : RecyclerView = binding.eventsRecycler
        val calendarView : CalendarView = view.findViewById(R.id.calendarView)
        val monthTextView : TextView = view.findViewById(R.id.MonthText)

        calendarViewHelper.setupCalendarView(calendarView, monthTextView)

        eventRecyclerView.adapter = eventAdapter
        eventRecyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        eventRecyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))

        val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(Resources.getSystem().displayMetrics.heightPixels, View.MeasureSpec.AT_MOST);
        val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(Resources.getSystem().displayMetrics.widthPixels, View.MeasureSpec.AT_MOST);
        calendarView.measure(widthMeasureSpec, heightMeasureSpec)

        val params: LinearLayout.LayoutParams = eventRecyclerView.layoutParams as LinearLayout.LayoutParams
        params.topMargin = calendarView.daySize.height * 6 //CalendarView wrong calculate height. So we need to margin events tab manually.
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
