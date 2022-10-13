package com.gietu.avyakt2o.data

import com.google.gson.annotations.SerializedName

data class OTP(
    @SerializedName("email")
    val email: String
)
data class VerifyOTP(
     @SerializedName("email")
    val email: String,
     @SerializedName("otp")
     val opt : Int

)
data class OtpStatus(
    @SerializedName("message")
    val message : String
)

data class ResetOTP(
    @SerializedName("email")
    val email: String,
    @SerializedName("newpassword")
    val password : String,
    @SerializedName("otp")
    val opt : Int
)