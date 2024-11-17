package com.capstone.hidroqu.ui.list

import com.capstone.hidroqu.R

data class ListCommunity(
    val idPost: Int,
    val userPostImg: Int,
    val userName: String,
    val imgPost: Int?,
    val txtPost: String,
    val commentList: Int,
    val time: String
)

val dummyListCommunity = listOf(
    ListCommunity(1, R.drawable.ic_launcher_foreground, "Muammar Ramadhani Maulizidan", R.drawable.hidroponikbg, "Apa pupuk terbaik termahal terbagus termurah Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean feugiat arcu et egestas euismod. Pellentesque condimentum risus tincidunt justo pulvinar, rhoncus commodo metus sagittis. Nulla non varius nisi, at tincidunt ex. ", 2, "Tahun lalu"),
    ListCommunity(2, R.drawable.ic_launcher_foreground, "Syakillah Nachwa", null, "Apa pupuk terbaik termahal terbagus termurah Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean feugiat arcu et egestas euismod. Pellentesque condimentum risus tincidunt justo pulvinar, rhoncus commodo metus sagittis. Nulla non varius nisi, at tincidunt ex. ", 4, "1 Bulan yang lalu"),
    ListCommunity(3, R.drawable.ic_launcher_foreground, "M Hanif", R.drawable.scan_tanam, "Apa pupuk terbaik termahal terbagus termurah Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean feugiat arcu et egestas euismod. Pellentesque condimentum risus tincidunt justo pulvinar, rhoncus commodo metus sagittis. Nulla non varius nisi, at tincidunt ex. ", 4, "1 Hari yang lalu "),
    ListCommunity(4, R.drawable.ic_launcher_foreground, "Sela",null, "Apa pupuk terbaik termahal terbagus termurah Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean feugiat arcu et egestas euismod. Pellentesque condimentum risus tincidunt justo pulvinar, rhoncus commodo metus sagittis. Nulla non varius nisi, at tincidunt ex. ", 2, "Baru saja "),
    ListCommunity(5, R.drawable.ic_launcher_foreground, "Caca", R.drawable.timun, "Apa pupuk terbaik termahal terbagus termurah Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean feugiat arcu et egestas euismod. Pellentesque condimentum risus tincidunt justo pulvinar, rhoncus commodo metus sagittis. Nulla non varius nisi, at tincidunt ex. ", 4, "3 Jam "),
    ListCommunity(6, R.drawable.ic_launcher_foreground, "Dayana",R.drawable.ic_launcher_background, "Apa pupuk terbaik termahal terbagus termurah Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean feugiat arcu et egestas euismod. Pellentesque condimentum risus tincidunt justo pulvinar, rhoncus commodo metus sagittis. Nulla non varius nisi, at tincidunt ex. ", 2, "5 Jam ")
)