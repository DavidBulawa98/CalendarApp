package com.dbulawa.calendar.wsclient.dto

import com.google.api.client.util.DateTime
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class DateInfoDTO(
    var date: Date? = null,
    var dateTime : Date? = null,
    var timeZone : String? = null
)