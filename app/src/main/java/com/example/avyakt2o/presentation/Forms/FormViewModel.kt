package com.example.avyakt2o.presentation.Forms

import androidx.lifecycle.ViewModel
import com.example.avyakt2o.`interface`.UserAPI
import com.example.avyakt2o.network.Retrofit

class FormViewModel : ViewModel() {
    val retService = Retrofit.getRetrofitInstance().create(UserAPI::class.java)
}