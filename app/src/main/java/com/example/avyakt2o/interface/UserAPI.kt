package com.example.avyakt2o.`interface`

import com.example.avyakt2o.data.*
import retrofit2.http.Body
import retrofit2.http.POST

interface UserAPI {
    //login and register routes
    @POST("/register")
    fun registerUser(@Body info: Register): retrofit2.Call<RegisterStatus>
    @POST("/login")
    fun login(@Body info: Login) : retrofit2.Call<LoginStatus>
    @POST("/sendotp")
    fun getOTP(@Body info: OTP ) : retrofit2.Call<OtpStatus>
    @POST("/verifyotp")
    fun verifyOTP(@Body token: VerifyOTP) : retrofit2.Call<OtpStatus>
    @POST("/reset-password")
    fun restPassword(@Body token: ResetOTP) : retrofit2.Call<OtpStatus>

    // Show Events
    @POST("/show-events")
    fun showEvents(@Body auth: Auth ) : retrofit2.Call<ShowEvent>

    // All tech events...
    @POST("/android-app-development")
    fun postAndroidDevelopmentEvent(@Body entries: Entries) : retrofit2.Call<EntriesStatus>
    @POST("/hack-for-giet")
    fun hackforgietEvent(@Body entries: Entries) : retrofit2.Call<EntriesStatus>
    @POST("/code-soccer")
    fun codesoccerEvent(@Body entries: Entries) : retrofit2.Call<EntriesStatus>
    @POST("/blind-coding")
    fun blindcodingEvent(@Body entries: Entries) : retrofit2.Call<EntriesStatus>
    @POST("/code-debugging")
    fun codedebuggingEvent(@Body entries: Entries) : retrofit2.Call<EntriesStatus>
    @POST("/web-puzzle")
    fun webpuzzleEvent(@Body entries: Entries) : retrofit2.Call<EntriesStatus>
    @POST("/poster-making-competition")
    fun postermakingEvent(@Body entries: Entries) : retrofit2.Call<EntriesStatus>
    @POST("/robotics-event")
    fun roboticsEvent(@Body entries: Entries) : retrofit2.Call<EntriesStatus>
    @POST("/idea-representation")
    fun ideapresentationEvent(@Body entries: Entries) : retrofit2.Call<EntriesStatus>
    @POST("/workshop")
    fun workshopEvent(@Body entries: Entries) : retrofit2.Call<EntriesStatus>
    @POST("/guest-talks")
    fun guestTalksEvent(@Body entries: Entries) : retrofit2.Call<EntriesStatus>
    @POST("/innovative-idea-poster-presentation")
    fun innovativeIdeaPosterPresentationEvent(@Body entries: Entries) : retrofit2.Call<EntriesStatus>
    @POST("/quiz")
    fun quizEvent(@Body entries: Entries) : retrofit2.Call<EntriesStatus>

    //  Non Tech Events
    @POST("/gaming")
    fun gamingEvent(@Body entries: Entries) : retrofit2.Call<EntriesStatus>
    @POST("/group-discussion")
    fun groupDiscussionEvent(@Body entries: Entries) : retrofit2.Call<EntriesStatus>
    @POST("/craft-making")
    fun craftMakingEvent(@Body entries: Entries) : retrofit2.Call<EntriesStatus>
    @POST("/treasure-hunt")
    fun treasureHuntEvent(@Body entries: Entries) : retrofit2.Call<EntriesStatus>
    @POST("/rangoli")
    fun rangoliEvent(@Body entries: Entries) : retrofit2.Call<EntriesStatus>
    @POST("/musical-chair")
    fun musicalChairEvent(@Body entries: Entries) : retrofit2.Call<EntriesStatus>
    @POST("/eureka")
    fun eurekaEvent(@Body entries: Entries) : retrofit2.Call<EntriesStatus>
    @POST("/gk-quiz")
    fun gkQuizEvent(@Body entries: Entries) : retrofit2.Call<EntriesStatus>
    @POST("/cse-got-tallent")
    fun cseGotTallentEvent(@Body entries: Entries) : retrofit2.Call<EntriesStatus>
    @POST("/on-the-spot-painting")
    fun onTheSpotPaintingEvent(@Body entries: Entries) : retrofit2.Call<EntriesStatus>
    @POST("/cartooning")
    fun cartooningEvent(@Body entries: Entries) : retrofit2.Call<EntriesStatus>

    // Cultural events
    @POST("/song")
    fun songEvent(@Body entries: Entries) : retrofit2.Call<EntriesStatus>
    @POST("/dance")
    fun danceEvent(@Body entries: Entries) : retrofit2.Call<EntriesStatus>
    @POST("/fashion-show")
    fun fashionShowEvent(@Body entries: Entries) : retrofit2.Call<EntriesStatus>
    @POST("/mimicry")
    fun mimicryEvent(@Body entries: Entries) : retrofit2.Call<EntriesStatus>
    @POST("/drama")
    fun dramaEvent(@Body entries: Entries) : retrofit2.Call<EntriesStatus>
    @POST("/tshirts")
    fun tshirtsEvent(@Body entries: Entries) : retrofit2.Call<EntriesStatus>
    @POST("/stall")
    fun stallEvent(@Body entries: Entries) : retrofit2.Call<EntriesStatus>

//    //contact form info routes.
//    @POST("/contact")
//    fun contactFormController(@Body entries: Entries) : retrofit2.Call<ResponseBody>
//    @POST("/ViewContact")
//    fun viewContacts(@Body entries: Entries) : retrofit2.Call<ResponseBody>










}