package com.capstone.hidroqu.ui.list

import com.capstone.hidroqu.R

data class ListUserData(
    val name: String,
    val img: Int,
    val bio: String
)

val dummyListUserData = listOf(
    ListUserData(
        "Tifah",
        R.drawable.ic_launcher_foreground,
        "Android Developer Advocate @google, sketch comedienne, opera singer. BLM."
    )
)