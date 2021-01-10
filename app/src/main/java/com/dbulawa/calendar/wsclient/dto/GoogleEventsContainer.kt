package com.dbulawa.calendar.wsclient.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GoogleEventsContainer(val items: List<GoogleEventDTO>)
