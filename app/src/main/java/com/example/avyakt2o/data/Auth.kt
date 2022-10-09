package com.example.avyakt2o.data

import com.google.gson.annotations.SerializedName

data class Auth(
    @SerializedName("email")
    val email : String,
    @SerializedName("token")
    val token : String
)
