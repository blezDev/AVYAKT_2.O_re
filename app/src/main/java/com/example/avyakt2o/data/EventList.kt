package com.example.avyakt2o.data

import com.google.gson.annotations.SerializedName



data class ShowEvent(
    val eventsData: List<EventList>,
    val message: String
)

data class EventList(
    val CoordinatorName: List<String>,
    val EventName: String,
    val EventType : String,
    val Number: List<String>,
    val PosterUrl: String,
    val RollNo: List<String>,
    val __v: Int,
    val _id: String
)
