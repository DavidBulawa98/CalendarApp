package com.dbulawa.calendar.adapter

import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.dbulawa.calendar.R
import com.dbulawa.calendar.databinding.EventItemBinding
import com.dbulawa.calendar.repository.data.Event
import com.dbulawa.calendar.ui.main.calendar.CalendarFragment
import com.dbulawa.calendar.ui.main.calendar.CalendarViewModel
import com.dbulawa.calendar.ui.main.eventdetail.EventDetailViewModel
import javax.inject.Inject


class EventAdapter @Inject constructor(
    val fragment: Fragment
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>(){

    var events : List<Event> = mutableListOf<Event>()
    private val viewModel: CalendarViewModel by lazy { ViewModelProvider(fragment).get(
        CalendarViewModel::class.java) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding: EventItemBinding = EventItemBinding.inflate(layoutInflater, parent, false)
        return EventViewHolder(itemBinding, fragment.requireContext(), viewModel)
    }

    override fun getItemCount(): Int {
        return events.size
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.bind(event)
    }


    class EventViewHolder(val binding : EventItemBinding, private val context: Context, private val viewModel: CalendarViewModel) : RecyclerView.ViewHolder(binding.root), View.OnLongClickListener{

        init {
            itemView.setOnLongClickListener(this);
        }

        fun bind(event: Event) {
            binding.event = event
        }


        override fun onLongClick(v: View?): Boolean {
            val popup = PopupMenu(context, v, Gravity.END)
            popup.inflate(R.menu.event_popup_menu)
            popup.setOnMenuItemClickListener  { item->
                when(item.itemId)
                {
                    R.id.popup_delete->{
                        if(binding.event != null && binding.event!!.id != null){
                            viewModel.deleteEvent(binding.event!!.id!!)
                        }
                    }

                    R.id.popup_cancel->{
                        println("cancel")
                    }

                }
                true
            }
            popup.show()
            return true
        }
    }
}