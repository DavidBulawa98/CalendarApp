package com.dbulawa.calendar.db.entity

import androidx.databinding.Bindable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Time
import java.util.Date

@Entity(tableName = "event")
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var date : Date,
    var time : Time,
    var title: String,
    var desc: String?
) {
}