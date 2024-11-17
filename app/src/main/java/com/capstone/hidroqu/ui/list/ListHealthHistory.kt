package com.capstone.hidroqu.ui.list

import com.capstone.hidroqu.R


data class ListHealthHistory(
    val plantId: Int,
    val healthId: Int, //primary
    val issue: String,
    val dateHistory: String,
    val symptoms: String,
    val cause: String,
    val relatedPhotos: List<Int>,
    val userPlantPhoto: Int
)


val dummyListHealthHistory = listOf(
    ListHealthHistory(
        plantId = 1,
        healthId = 1,
        issue = "Kekurangan sulfur 1",
        dateHistory = "04 November 2024",
        symptoms = "Tanaman yang mengalami kekurangan sulfur menunjukkan gejala daun muda menguning (klorosis), mirip dengan gejala kekurangan nitrogen, tetapi dimulai dari daun-daun muda terlebih dahulu.",
        cause = "Penurunan Kadar Sulfur dalam Larutan Nutrisi Seiring Waktu\nTanaman akan menyerap sulfur dari larutan nutrisi, dan jika larutan tidak diganti secara berkala, konsentrasi sulfur akan menurun. Ini dapat menyebabkan kekurangan sulfur, terutama pada tahap pertumbuhan cepat.",
        relatedPhotos = listOf(R.drawable.ic_launcher_background, R.drawable.scan_tanam, R.drawable.poto_tanam, R.drawable.ic_launcher_background),
        userPlantPhoto = R.drawable.scan_tanam
    ),
    ListHealthHistory(
        plantId = 1,
        healthId = 2,
        issue = "Kekurangan sulfur 2",
        dateHistory = "04 November 2024",
        symptoms = "Tanaman yang mengalami kekurangan sulfur menunjukkan gejala daun muda menguning (klorosis), mirip dengan gejala kekurangan nitrogen, tetapi dimulai dari daun-daun muda terlebih dahulu.",
        cause = "Penurunan Kadar Sulfur dalam Larutan Nutrisi Seiring Waktu\nTanaman akan menyerap sulfur dari larutan nutrisi, dan jika larutan tidak diganti secara berkala, konsentrasi sulfur akan menurun. Ini dapat menyebabkan kekurangan sulfur, terutama pada tahap pertumbuhan cepat.",
        relatedPhotos = listOf(R.drawable.ic_launcher_background, R.drawable.scan_tanam, R.drawable.poto_tanam, R.drawable.ic_launcher_background),
        userPlantPhoto = R.drawable.scan_tanam
    ),
    ListHealthHistory(
        plantId = 1,
        healthId = 3,
        issue = "Kekurangan sulfur 3",
        dateHistory = "04 November 2024",
        symptoms = "Tanaman yang mengalami kekurangan sulfur menunjukkan gejala daun muda menguning (klorosis), mirip dengan gejala kekurangan nitrogen, tetapi dimulai dari daun-daun muda terlebih dahulu.",
        cause = "Penurunan Kadar Sulfur dalam Larutan Nutrisi Seiring Waktu\nTanaman akan menyerap sulfur dari larutan nutrisi, dan jika larutan tidak diganti secara berkala, konsentrasi sulfur akan menurun. Ini dapat menyebabkan kekurangan sulfur, terutama pada tahap pertumbuhan cepat.",
        relatedPhotos = listOf(R.drawable.ic_launcher_background, R.drawable.scan_tanam, R.drawable.poto_tanam, R.drawable.ic_launcher_background),
        userPlantPhoto = R.drawable.scan_tanam
    ),
    ListHealthHistory(
        plantId = 2,
        healthId = 1,
        issue = "Kekurangan vitamin a",
        dateHistory = "30 Oktober 2024",
        symptoms = "Tanaman yang mengalami kekurangan sulfur menunjukkan gejala daun muda menguning (klorosis), mirip dengan gejala kekurangan nitrogen, tetapi dimulai dari daun-daun muda terlebih dahulu.",
        cause = "Penurunan Kadar Sulfur dalam Larutan Nutrisi Seiring Waktu\nTanaman akan menyerap sulfur dari larutan nutrisi, dan jika larutan tidak diganti secara berkala, konsentrasi sulfur akan menurun. Ini dapat menyebabkan kekurangan sulfur, terutama pada tahap pertumbuhan cepat.",
        relatedPhotos = listOf(R.drawable.ic_launcher_background, R.drawable.scan_tanam, R.drawable.poto_tanam, R.drawable.ic_launcher_background),
        userPlantPhoto = R.drawable.scan_tanam
    )
)


fun getHealthHistoryByPlantId(plantId: Int): List<ListHealthHistory> {
    return dummyListHealthHistory.filter { it.plantId == plantId }
}

fun getHealthHistoryById(healthId: Int): ListHealthHistory? {
    // Dummy data for example. Replace with your actual data fetching logic.
    return dummyListHealthHistory.find { it.healthId == healthId }
}
