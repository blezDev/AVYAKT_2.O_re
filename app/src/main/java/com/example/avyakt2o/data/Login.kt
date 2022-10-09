package com.example.avyakt2o.data

import com.google.gson.annotations.SerializedName

data class Login(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)
data class LoginStatus(
    @SerializedName("message")
    val message : String,
    @SerializedName("token")
    val token : String
)
