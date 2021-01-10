package com.dbulawa.calendar.binding

import android.widget.TextView
import androidx.databinding.InverseMethod
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*


class Converters {
    companion object{
        @JvmStatic
        @InverseMethod("dateToString")
        fun stringToDate(source: String?): Date? {
            return source?.let { SimpleDateFormat.getDateInstance().parse(it) }
        }

        @JvmStatic
        fun dateToString(source: Date?): String? {
            return source?.let { SimpleDateFormat.getDateInstance().format(it) }
        }

        @JvmStatic
        @InverseMethod("timeToString")
        fun stringToTime(source: String?): Time? {
            return source?.let {  Time.valueOf(it + ":00")}
        }

        @JvmStatic
        fun timeToString(source: Time?): String? {
            return source?.let { SimpleDateFormat("HH:mm").format(it) }
        }
    }

}