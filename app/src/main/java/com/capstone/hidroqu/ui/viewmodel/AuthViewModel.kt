package com.capstone.hidroqu.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.hidroqu.nonui.api.HidroQuApiConfig
import com.capstone.hidroqu.nonui.api.HidroQuApiService
import com.capstone.hidroqu.nonui.data.AuthResponse
import com.capstone.hidroqu.nonui.data.BasicResponse
import com.capstone.hidroqu.nonui.data.ForgotPasswordRequest
import com.capstone.hidroqu.nonui.data.LoginRequest
import com.capstone.hidroqu.nonui.data.LoginResponse
import com.capstone.hidroqu.nonui.data.RegisterRequest
import com.capstone.hidroqu.nonui.data.ResetPasswordRequest
import com.capstone.hidroqu.nonui.data.SharedPreferencesHelper
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val authService = HidroQuApiConfig.retrofit.create(HidroQuApiService::class.java)
    private val sharedPreferencesHelper = SharedPreferencesHelper(application)

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _loginResult = MutableLiveData<LoginResponse?>()
    val loginResult: LiveData<LoginResponse?> = _loginResult

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
                        response.body()?.let {
                            onSuccess(it) }
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

    fun loginUser(
        email: String,
        password: String,
        context: Context,
        onSuccess: (LoginResponse) -> Unit,
        onError: (String) -> Unit
    ) {
        val request = LoginRequest(email, password)
        _isLoading.value = true
        viewModelScope.launch {
            authService.login(request).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        loginResponse?.let {
                            loginResponse.data.token.let { token ->
                                sharedPreferencesHelper.saveToken(token)
                            }
                            onSuccess(it)
                        }
                    } else {
                        onError("Login failed: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    _isLoading.value = false
                    onError("Failure: ${t.message}")
                }
            })
        }
    }

    fun isUserLoggedIn(): Boolean {
        return sharedPreferencesHelper.getToken() != null
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