package com.dbulawa.calendar.wsclient.dto

import com.google.api.client.util.DateTime
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class GoogleEventDTO (
    var id: String? = null,
    var summary: String? = null,
    var description: String? = null,
    var start: DateInfoDTO? = null,
    var end: DateInfoDTO? = null
)

