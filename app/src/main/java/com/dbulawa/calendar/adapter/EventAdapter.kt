package com.dbulawa.calendar.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dbulawa.calendar.databinding.EventItemBinding
import com.dbulawa.calendar.repository.data.Event
import java.sql.Time
import java.util.*
import javax.inject.Inject


class EventAdapter @Inject constructor(
    val activity: Activity
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>(){

    var events : List<Event> = mutableListOf<Event>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding: EventItemBinding = EventItemBinding.inflate(layoutInflater, parent, false)
        return EventViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return events.size
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.bind(event)
    }

    class EventViewHolder(val binding : EventItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event) {
            binding.event = event
        }
    }
}