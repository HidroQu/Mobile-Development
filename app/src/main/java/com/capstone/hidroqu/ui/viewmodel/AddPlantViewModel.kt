package com.capstone.hidroqu.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.hidroqu.nonui.api.HidroQuApiConfig
import com.capstone.hidroqu.nonui.api.HidroQuApiService
import com.capstone.hidroqu.nonui.data.PlantResponse
import com.capstone.hidroqu.nonui.data.PlantResponseWrapper
import com.capstone.hidroqu.nonui.data.SharedPreferencesHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
//
//class AddPlantViewModel : ViewModel() {
//    private val apiService: HidroQuApiService = HidroQuApiConfig.retrofit.create(HidroQuApiService::class.java)
//
//    private val _plants = MutableLiveData<List<PlantResponse>>()
//    val plants: LiveData<List<PlantResponse>> get() = _plants
//
//    private val _isLoading = MutableLiveData<Boolean>()
//    val isLoading: LiveData<Boolean> get() = _isLoading
//
//    private val _errorMessage = MutableLiveData<String?>()
//    val errorMessage: LiveData<String?> get() = _errorMessage
//
//
//}
