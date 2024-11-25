package com.capstone.hidroqu.nonui.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ArticleResponseWrapper(
    val status: String,
    val message: String,
    val data: ArticleDataWrapper
) : Parcelable

@Parcelize
data class ArticleDataWrapper(
    val currentPage: Int,
    val data: List<ArticleDetailResponse>,
    val firstPageUrl: String?,
    val lastPage: Int,
    val lastPageUrl: String?,
    val nextPageUrl: String?,
    val prevPageUrl: String?,
    val total: Int,
    val perPage: Int,
    val from: Int,
    val to: Int,
    val path: String
) : Parcelable

@Parcelize
data class ArticleDetailWrapper(
    val status: String,
    val message: String,
    val data: ArticleDetailResponse
) : Parcelable

@Parcelize
data class ArticleDetailResponse(
    val id: Int,
    val title: String,
    val content: String,
    val image: String,
    val userid: Int,
    val createdAt: String,
    val updatedAt: String,
    val user: UserDataArticle,
) : Parcelable

@Parcelize
data class UserDataArticle(
    val id: Int,
    val name: String,
    val email: String,
    val emailVerifiedAt: String?,
    val profileImage: String?,
    val bio: String?,
    val createdAt: String,
    val updatedAt: String
) : Parcelable