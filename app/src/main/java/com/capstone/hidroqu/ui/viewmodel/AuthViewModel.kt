package com.capstone.hidroqu.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.hidroqu.nonui.api.HidroQuApiConfig
import com.capstone.hidroqu.nonui.api.HidroQuApiService
import com.capstone.hidroqu.nonui.data.AuthResponse
import com.capstone.hidroqu.nonui.data.BasicResponse
import com.capstone.hidroqu.nonui.data.ForgotPasswordRequest
import com.capstone.hidroqu.nonui.data.LoginRequest
import com.capstone.hidroqu.nonui.data.RegisterRequest
import com.capstone.hidroqu.nonui.data.ResetPasswordRequest
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthViewModel : ViewModel() {
    private val authService = HidroQuApiConfig.retrofit.create(HidroQuApiService::class.java)

    fun registerUser(
        name: String,
        email: String,
        password: String,
        passwordConfirmation: String,
        onSuccess: (AuthResponse) -> Unit,
        onError: (String) -> Unit
    ) {
        val request = RegisterRequest(name, email, password, passwordConfirmation)
        viewModelScope.launch {
            authService.register(request).enqueue(object : Callback<AuthResponse> {
                override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                    if (response.isSuccessful) {
                        response.body()?.let { onSuccess(it) }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        onError("Error: ${errorBody ?: response.message()}")
                    }
                }

                override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                    onError("Failure: ${t.message}")
                }
            })
        }
    }

    fun loginUser(email: String, password: String, onSuccess: (AuthResponse) -> Unit, onError: (String) -> Unit) {
        val request = LoginRequest(email, password)
        viewModelScope.launch {
            authService.login(request).enqueue(object : Callback<AuthResponse> {
                override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                    if (response.isSuccessful) {
                        response.body()?.let { onSuccess(it) }
                    } else {
                        onError("Error: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                    onError("Failure: ${t.message}")
                }
            })
        }
    }

    fun forgotPassword(email: String, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        val request = ForgotPasswordRequest(email)
        viewModelScope.launch {
            authService.forgotPassword(request).enqueue(object : Callback<BasicResponse> {
                override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                    if (response.isSuccessful) {
                        response.body()?.message?.let { onSuccess(it) }
                    } else {
                        onError("Error: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                    onError("Failure: ${t.message}")
                }
            })
        }
    }

    fun resetPassword(
        token: String,
        email: String,
        password: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        val request = ResetPasswordRequest(token, email, password)
        viewModelScope.launch {
            authService.resetPassword(request).enqueue(object : Callback<BasicResponse> {
                override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                    if (response.isSuccessful) {
                        response.body()?.message?.let { onSuccess(it) }
                    } else {
                        onError("Error: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                    onError("Failure: ${t.message}")
                }
            })
        }
    }
}