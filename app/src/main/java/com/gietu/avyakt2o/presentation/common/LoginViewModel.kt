package com.gietu.avyakt2o.presentation.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gietu.avyakt2o.`interface`.UserAPI
import com.gietu.avyakt2o.data.LoginStatus
import com.gietu.avyakt2o.presentation.common.network.Retrofit
import com.gietu.avyakt2o.utils.NetworkResult

class LoginViewModel :ViewModel() {
    val retService = Retrofit.getRetrofitInstance().create(UserAPI::class.java)
    private val _userResponseLiveData = MutableLiveData<NetworkResult<LoginStatus>>()
    val userResponseLiveData : LiveData<NetworkResult<LoginStatus>>
    get() = _userResponseLiveData
}