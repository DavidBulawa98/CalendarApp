package com.dbulawa.calendar.ui.main.eventdetail

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.dbulawa.calendar.R
import com.dbulawa.calendar.authorization.GoogleAuthService
import com.dbulawa.calendar.databinding.CreateEventActivityBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EventDetailActivity : AppCompatActivity() {
    @Inject lateinit var googleAuthService: GoogleAuthService
    lateinit var viewModel: EventDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_event_activity)
        val createEventActivityBinding: CreateEventActivityBinding = DataBindingUtil.setContentView(this, R.layout.create_event_activity)
        val viewModel: EventDetailViewModel by viewModels()
        this.viewModel = viewModel
        createEventActivityBinding.viewModel = viewModel
        initSnackBar()
    }


    private fun initSnackBar(){
        val view : View = findViewById(android.R.id.content);
        var snackbar : Snackbar
        viewModel.success.observe(this, Observer {
            if(it){
                snackbar = Snackbar.make(view,"Sucessfull", Snackbar.LENGTH_LONG)
                this.finish()

            }else{
                snackbar = Snackbar.make(view,"Error", Snackbar.LENGTH_LONG)

            }
            snackbar.show()
        })
    }
}