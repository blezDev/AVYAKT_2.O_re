package com.gietu.avyakt2o.repositories

import android.content.Context
import android.util.Log

import com.gietu.avyakt2o.`interface`.UserAPI
import com.gietu.avyakt2o.data.Login
import com.gietu.avyakt2o.data.Register
import com.gietu.avyakt2o.utils.Constants.RE_TAG

class UserRepository(private val userAPI: UserAPI) {
   suspend  fun registerUser(register: Register,context: Context){
        val response  = userAPI.registerUser(register)
       Log.e(RE_TAG,response.toString())
    }
    suspend fun loginUser(login: Login)
    {
        val response = userAPI.login(login)
        Log.e(RE_TAG,response.toString())
    }

}