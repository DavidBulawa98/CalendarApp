package com.dbulawa.calendar.wsclient

import com.dbulawa.calendar.wsclient.dto.GoogleEventDTO
import com.dbulawa.calendar.wsclient.dto.GoogleEventsContainer
import com.google.api.client.util.DateTime
import retrofit2.Call
import retrofit2.http.*

interface GoogleEventService {

    @POST("/calendar/v3/calendars/primary/events")
    suspend fun insertEvent(@Body googleEventDTO: GoogleEventDTO) : GoogleEventDTO

    @GET("/calendar/v3/calendars/primary/events")
    suspend fun getEvents(@Query("timeMin") timeMin: DateTime, @Query("timeMax") timeMax: DateTime) : GoogleEventsContainer

    @DELETE("/calendar/v3/calendars/primary/events/{eventId}")
    suspend fun deleteEvent(@Path("eventId") eventId: String) : GoogleEventDTO
}
