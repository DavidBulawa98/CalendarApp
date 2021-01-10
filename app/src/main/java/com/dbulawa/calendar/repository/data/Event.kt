package com.dbulawa.calendar.repository.data

import java.sql.Time
import java.util.*

data class Event(
    var id: String? = null,
    var date: Date? = null,
    var time : Time? = null,
    var title: String? = null,
    var desc: String? = null
)