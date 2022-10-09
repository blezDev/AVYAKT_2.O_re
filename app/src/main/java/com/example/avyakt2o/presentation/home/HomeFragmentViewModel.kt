package com.example.avyakt2o.presentation.home

import androidx.lifecycle.ViewModel
import com.example.avyakt2o.`interface`.UserAPI
import com.example.avyakt2o.network.Retrofit

class HomeFragmentViewModel:ViewModel() {
    val retService: UserAPI = Retrofit.getRetrofitInstance().create(UserAPI::class.java)
}