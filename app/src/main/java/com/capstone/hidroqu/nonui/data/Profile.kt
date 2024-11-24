package com.capstone.hidroqu.nonui.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProfileResponse(
    val status: String,
    val message: String,
    val data: User
) : Parcelable
