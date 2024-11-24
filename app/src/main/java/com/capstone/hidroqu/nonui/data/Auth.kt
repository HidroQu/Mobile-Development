package com.capstone.hidroqu.nonui.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val password_confirmation: String
) : Parcelable

@Parcelize
data class LoginRequest(
    val email: String,
    val password: String
) : Parcelable

@Parcelize
data class LoginResponse(
    val status: String,
    val message: String,
    val data: AuthResponse
): Parcelable

@Parcelize
data class AuthResponse(
    val user: User,
    val token: String
): Parcelable

@Parcelize
data class ForgotPasswordRequest(
    val email: String
) : Parcelable

@Parcelize
data class User(
    val id: Int,
    val name: String,
    val email: String,
    val email_verified_at: String?,
    val profile_image: String?,
    val bio: String?,
    val created_at: String,
    val updated_at: String
) : Parcelable

@Parcelize
data class BasicResponse(
    val message: String
) : Parcelable

data class TestResponse(
    val status: String,
    val message: String,
    val data: Any?
) {
    override fun toString(): String {
        return "BasicResponse(status='$status', message='$message', data=$data)"
    }
}


@Parcelize
data class ResetPasswordRequest(
    val token: String,
    val email: String,
    val password: String
) : Parcelable