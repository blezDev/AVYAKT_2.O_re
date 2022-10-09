package com.example.avyakt2o.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.avyakt2o.R
import com.example.avyakt2o.utils.Constants.PREFS_TOKEN_FILE
import com.example.avyakt2o.utils.Constants.USER_EMAIL
import com.example.avyakt2o.utils.Constants.USER_TOKEN

class TokenManager(context: Context) {
    private var prefs : SharedPreferences = context.getSharedPreferences(PREFS_TOKEN_FILE,Context.MODE_PRIVATE)

 fun saveToken(token : String)
 {
     val editor = prefs.edit()
     editor.putString(USER_TOKEN,token)
     editor.apply()
 }
    fun saveEmail(email : String)
    {
        val editor = prefs.edit()
        editor.putString(USER_EMAIL,email)
        editor.apply()
    }
    fun getToken() : String?
    {
        return prefs.getString(USER_TOKEN,null)
    }
    fun getEmail() : String?{
        return prefs.getString(USER_EMAIL,null)
    }
    fun deteleCredit(){
        if(prefs.getString(USER_TOKEN,null) != null && prefs.getString(USER_EMAIL,null) != null)
        {
            val editor = prefs.edit()
            editor.clear()
            editor.apply()
        }

    }


}