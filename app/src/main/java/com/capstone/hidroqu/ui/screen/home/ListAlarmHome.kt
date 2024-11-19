package com.capstone.hidroqu.ui.screen.home

import com.capstone.hidroqu.R

data class ListAlarmHome(
    val imgAlarmHome: Int,
    val txtAlarmHome: Int,
    val descAlarmHome: Int
)

val dummyListAlarmHome = listOf(
    ListAlarmHome(R.drawable.poto_tanam, R.string.txt_pototanam, R.string.txt_dummy_pototanam),
    ListAlarmHome(R.drawable.scan_tanam, R.string.txt_scantanam, R.string.txt_dummy_scantanam)
)