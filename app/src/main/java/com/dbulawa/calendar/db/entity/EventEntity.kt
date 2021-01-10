package com.dbulawa.calendar.db.entity

import androidx.databinding.Bindable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Time
import java.util.*

@Entity(tableName = "event")
data class EventEntity(
    @PrimaryKey
    var id: String ,
    var date : Date,
    var time : Time,
    var title: String,
    var desc: String?,
    var sended: Boolean
) {
}