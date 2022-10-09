package com.example.avyakt2o.repositories

import android.content.Context
import android.util.Log
import android.widget.Toast

import com.example.avyakt2o.`interface`.UserAPI
import com.example.avyakt2o.data.Login
import com.example.avyakt2o.data.Register
import com.example.avyakt2o.utils.Constants.RE_TAG
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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