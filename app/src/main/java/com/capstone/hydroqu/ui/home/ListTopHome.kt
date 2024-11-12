package com.capstone.hydroqu.ui.home

import com.fundamentalandroid.hydroqu.R

data class ListTopHome(
    val imgTopHome: Int,
    val iconTopHome: Int,
    val txtTopHome: Int,
    val descTopHome: Int
)

val dummyListTopHome = listOf(
    ListTopHome(R.drawable.poto_tanam, R.drawable.camera, R.string.txt_pototanam, R.string.txt_dummy_pototanam),
    ListTopHome(R.drawable.scan_tanam, R.drawable.flip, R.string.txt_scantanam, R.string.txt_dummy_scantanam)
)