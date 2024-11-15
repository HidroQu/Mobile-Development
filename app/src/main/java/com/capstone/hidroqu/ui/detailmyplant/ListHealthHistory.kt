package com.capstone.hidroqu.ui.detailmyplant

import com.capstone.hidroqu.R


data class ListHealthHistory(
    val id: Int,
    val issue: String,
    val date: String,
    val symptoms: String,
    val cause: String,
    val userPlantPhoto: Int,
    val relatedPhoto: Int
)
val dummyListHealthHistory = listOf(
    ListHealthHistory(1,
        "Kekurangan Sulfur",
        "04 November 2024",
        "Tanaman yang mengalami kekurangan sulfur menunjukkan gejala daun muda menguning (klorosis), mirip dengan gejala kekurangan nitrogen, tetapi dimulai dari daun-daun muda terlebih dahulu. Sulfur diperlukan untuk pembentukan protein dan enzim, sehingga kekurangan sulfur dapat menghambat pertumbuhan tanaman, menghasilkan daun yang lebih kecil, dan batang yang lebih lemah. Pada tanaman dengan kekurangan sulfur yang parah, seluruh tanaman dapat tampak kuning atau pucat, dan hasil panen pun cenderung menurun.",
        "Penurunan Kadar Sulfur dalam Larutan Nutrisi Seiring Waktu\nTanaman akan menyerap sulfur dari larutan nutrisi, dan jika larutan tidak diganti secara berkala, konsentrasi sulfur akan menurun. Ini dapat menyebabkan kekurangan sulfur, terutama pada tahap pertumbuhan cepat.\n\nKetidaksesuaian pH dalam Larutan Nutrisi\npH yang terlalu tinggi atau rendah dapat menghambat penyerapan sulfur dan nutrisi lainnya oleh tanaman. Dalam hidroponik, pH optimal untuk tanaman timun adalah antara 5.5 dan 6.5. Di luar rentang ini, akar tanaman mungkin kesulitan menyerap sulfur meskipun unsur ini tersedia dalam larutan.",
        R.drawable.poto_tanam,
        R.drawable.scan_tanam
    ),
    ListHealthHistory(2,
        "Kekurangan Sulfur",
        "04 November 2024",
        "Tanaman yang mengalami kekurangan sulfur menunjukkan gejala daun muda menguning (klorosis), mirip dengan gejala kekurangan nitrogen, tetapi dimulai dari daun-daun muda terlebih dahulu. Sulfur diperlukan untuk pembentukan protein dan enzim, sehingga kekurangan sulfur dapat menghambat pertumbuhan tanaman, menghasilkan daun yang lebih kecil, dan batang yang lebih lemah. Pada tanaman dengan kekurangan sulfur yang parah, seluruh tanaman dapat tampak kuning atau pucat, dan hasil panen pun cenderung menurun.",
        "Penurunan Kadar Sulfur dalam Larutan Nutrisi Seiring Waktu\nTanaman akan menyerap sulfur dari larutan nutrisi, dan jika larutan tidak diganti secara berkala, konsentrasi sulfur akan menurun. Ini dapat menyebabkan kekurangan sulfur, terutama pada tahap pertumbuhan cepat.\n\nKetidaksesuaian pH dalam Larutan Nutrisi\npH yang terlalu tinggi atau rendah dapat menghambat penyerapan sulfur dan nutrisi lainnya oleh tanaman. Dalam hidroponik, pH optimal untuk tanaman timun adalah antara 5.5 dan 6.5. Di luar rentang ini, akar tanaman mungkin kesulitan menyerap sulfur meskipun unsur ini tersedia dalam larutan.",
        R.drawable.poto_tanam,
        R.drawable.scan_tanam
    ),
)