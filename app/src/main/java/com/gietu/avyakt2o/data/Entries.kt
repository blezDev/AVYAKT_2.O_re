package com.gietu.avyakt2o.data

import com.google.gson.annotations.SerializedName

data class Entries(
  /*  @SerializedName("email")
    val auth_email : String,*/
    @SerializedName("token")
    val token : String,
    @SerializedName("name")
    val name : List<String>,
    @SerializedName("email")
    val email : List<String>,
    @SerializedName("rollno")
    val rollno : List<String>,
    @SerializedName("teamName")
    val teamName : Any?,
    @SerializedName("eventName")
    val eventName : String,
    @SerializedName("phone")
    val phone : List<String>,
    @SerializedName("type")
    val type : String
)
data class GameEntries(
    /*  @SerializedName("email")
      val auth_email : String,*/
    @SerializedName("token")
    val token : String,
    @SerializedName("name")
    val name : List<String>,
    @SerializedName("email")
    val email : List<String>,
    @SerializedName("rollno")
    val rollno : List<String>,
    @SerializedName("teamName")
    val teamName : Any?,
    @SerializedName("eventName")
    val eventName : String,
    @SerializedName("phone")
    val phone : List<String>,
    @SerializedName("category")
    val category : String,
    @SerializedName("type")
    val type : String
)


data class SarEntries(
    /*  @SerializedName("email")
      val auth_email : String,*/
    @SerializedName("token")
    val token : String,
    @SerializedName("name")
    val name : List<String>,
    @SerializedName("email")
    val email : List<String>,
    @SerializedName("rollno")
    val rollno : List<String>,
    @SerializedName("teamName")
    val teamName : Any?,
    @SerializedName("eventName")
    val eventName : String,
    @SerializedName("phone")
    val phone : List<String>,
    @SerializedName("category")
    val category : String,
    @SerializedName("type")
    val type : String
)


data class TshirtsEntries(
    /*  @SerializedName("email")
      val auth_email : String,*/
    @SerializedName("token")
    val token : String,
    @SerializedName("name")
    val name : List<String>,
    @SerializedName("email")
    val email : List<String>,
    @SerializedName("rollno")
    val rollno : List<String>,
    @SerializedName("teamName")
    val teamName : Any?,
    @SerializedName("eventName")
    val eventName : String,
    @SerializedName("phone")
    val phone : List<String>,
    @SerializedName("size")
    val size : String,
    @SerializedName("type")
    val type : String
)

data class EntriesStatus(
        @SerializedName("message")
        val message: String
)


data class EntriesResponse(
    @SerializedName("data")
    val Entries: List<Entries>,
    @SerializedName("message")
    val message: String
)




