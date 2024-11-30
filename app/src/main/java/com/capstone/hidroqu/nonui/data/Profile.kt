package com.capstone.hidroqu.nonui.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProfileResponse(
    val status: String,
    val message: String,
    val data: User
) : Parcelable

@Parcelize
data class MyPostResponse(
    val status: String,
    val message: String,
    val data: MyPostPage
) : Parcelable

@Parcelize
data class MyPostPage(
    val current_page : Int,
    val data : List<MyPostData>,
    val first_page_url: String?,
    val last_page: Int,
    val last_page_url: String?,
    val next_page_url: String?,
    val prev_page_url: String?,
    val total: Int,
    val per_page: Int,
    val from: Int,
    val to: Int,
    val path: String
) : Parcelable

@Parcelize
data class MyPostData(
    val id : Int,
    val title: String,
    val content: String,
    val image: String,
    val user_id: String,
    val created_at: String,
    val updated_at: String,
    val comments_count: Int,
    val user: User
) : Parcelable