package com.dbulawa.calendar.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dbulawa.calendar.R
import javax.inject.Inject
import javax.inject.Named

class DayOfTheWeekAdapter @Inject constructor(
    val activity: Activity
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    @Inject @Named("DaysOfTheWeek") lateinit var daysOfTheWeek: Array<String>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val rootView: View =
            LayoutInflater.from(activity).inflate(R.layout.week_day_layout, parent, false)
        return DayOfTheWeekViewHolder(rootView)
    }

    override fun getItemCount(): Int {
        return daysOfTheWeek.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val holder = holder as DayOfTheWeekViewHolder
        holder.dayOfTheWeek.text = daysOfTheWeek[position]
    }

    class DayOfTheWeekViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayOfTheWeek: TextView

        init {
            dayOfTheWeek = itemView.findViewById(R.id.day_of_week)
        }
    }
}