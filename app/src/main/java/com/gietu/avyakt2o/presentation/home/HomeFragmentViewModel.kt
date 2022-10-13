package com.gietu.avyakt2o.presentation.home

import androidx.lifecycle.ViewModel
import com.gietu.avyakt2o.`interface`.UserAPI
import com.gietu.avyakt2o.presentation.common.network.Retrofit

class HomeFragmentViewModel:ViewModel() {
    val retService: UserAPI = Retrofit.getRetrofitInstance().create(UserAPI::class.java)
}