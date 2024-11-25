package com.capstone.hidroqu.nonui.data

import android.net.Uri
import android.os.Parcelable
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import java.lang.reflect.Type
import com.google.gson.JsonElement
import kotlinx.parcelize.Parcelize
import java.io.File

@Parcelize
data class CommunityResponseWrapper(
    val status: String,
    val message: String,
    val data: CommunityDataWrapper
) : Parcelable

@Parcelize
data class CommunityDataWrapper(
    val current_page: Int,
    val data: List<PostData>,
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
data class CommunityDetailWrapper(
    val status: String,
    val message: String,
    val data: CommunityDetailResponse
) : Parcelable

@Parcelize
data class CommunityRequest(
    val title: String,
    val content: String
) : Parcelable

@Parcelize
data class CommunityCommentRequest(
    val community_id: Int,
    val title: String,
    val content: String
) : Parcelable

@Parcelize
data class MyPostsResponseWrapper(
    val status: String,
    val message: String,
    val data: PostData
) : Parcelable

@Parcelize
data class PostData(
    val id: Int,
    val title: String,
    val content: String,
    val image: String,
    val user_id: Int,
    val created_at: String,
    val updated_at: String,
    val comments_count: Int,
    val user: UserData
) : Parcelable

@Parcelize
data class CommunityDetailResponse(
    val id: Int,
    val title: String,
    val content: String,
    val image: String,
    val user_id: Int,
    val created_at: String,
    val updated_at: String,
    val user: UserData,
    val get_comments: List<Comment>
) : Parcelable

@Parcelize
data class UserData(
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
data class Comment(
    val id: Int,
    val community_id: Int,
    val comment_id: Int?,
    val user_id: Int,
    val content: String,
    val image: String?, // Menggunakan `image` bukan `images`
    val created_at: String,
    val updated_at: String?,
    val user: UserData,
    val replies: List<Reply>
) : Parcelable

@Parcelize
data class Reply( // Renamed from Replies to Reply
    val id: Int,
    val community_id: Int,
    val comment_id: Int?,
    val user_id: Int,
    val content: String,
    val images: String, // Changed from List<String> to String to match JSON
    val created_at: String,
    val updated_at: String?,
    val user: UserData
) : Parcelable

