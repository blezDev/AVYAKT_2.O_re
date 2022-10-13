package com.gietu.avyakt2o.presentation.Forms

import androidx.lifecycle.ViewModel
import com.gietu.avyakt2o.`interface`.UserAPI
import com.gietu.avyakt2o.presentation.common.network.Retrofit

class FormViewModel : ViewModel() {
    val retService = Retrofit.getRetrofitInstance().create(UserAPI::class.java)
}