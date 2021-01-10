package com.dbulawa.calendar.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.dbulawa.calendar.R
import com.dbulawa.calendar.ui.main.eventdetail.EventDetailActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        initFab()
    }

    private fun initFab(){
        val fab: View = findViewById(R.id.fab)
        fab.setOnClickListener {
            startActivity(Intent(this, EventDetailActivity::class.java))
        }
    }
}
