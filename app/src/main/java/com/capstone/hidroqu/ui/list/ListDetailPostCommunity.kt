package com.capstone.hidroqu.ui.list

import com.capstone.hidroqu.R

data class ListDetailPostCommunity(
    val idPost: Int,
    val idComment: Int,
    val userCommentImg: Int,
    val userCommentName: String,
    val txtComment: String,
    val timeComment: String
)

val dummyListDetailPostCommunity = listOf(
    ListDetailPostCommunity(1, 1, R.drawable.ic_launcher_foreground, "Rifki", "Hi, Your plant looks good", "11 November 2024"),
    ListDetailPostCommunity(1, 2, R.drawable.ic_launcher_foreground, "Aulia", "Wow, what a healthy plant!", "12 November 2024"),
    ListDetailPostCommunity(1, 3, R.drawable.ic_launcher_foreground, "Nisa", "Looks so fresh and vibrant!", "13 November 2024"),
    ListDetailPostCommunity(1, 4, R.drawable.ic_launcher_foreground, "Dina", "Your plant is thriving beautifully!", "14 November 2024"),
    ListDetailPostCommunity(1, 5, R.drawable.ic_launcher_foreground, "Raka", "Amazing color on that plant!", "15 November 2024"),
    ListDetailPostCommunity(1, 6, R.drawable.ic_launcher_foreground, "Yani", "Such a great variety of colors!", "16 November 2024"),
    ListDetailPostCommunity(1, 7, R.drawable.ic_launcher_foreground, "Joko", "This is exactly how I want my plant to look!", "17 November 2024"),
    ListDetailPostCommunity(1, 8, R.drawable.ic_launcher_foreground, "Fauzan", "The leaves are so lush, nice job!", "18 November 2024"),
    ListDetailPostCommunity(1, 9, R.drawable.ic_launcher_foreground, "Wira", "It’s glowing, great care!", "19 November 2024"),

    ListDetailPostCommunity(2, 1, R.drawable.ic_launcher_foreground, "Fikri", "Hi, I love the way your plant grows!", "16 November 2024"),
    ListDetailPostCommunity(2, 2, R.drawable.ic_launcher_foreground, "Sarah", "Such a stunning plant!", "17 November 2024"),
    ListDetailPostCommunity(2, 3, R.drawable.ic_launcher_foreground, "Lutfi", "How do you take care of it?", "18 November 2024"),
    ListDetailPostCommunity(2, 4, R.drawable.ic_launcher_foreground, "Zahra", "I’m curious about the soil mix you use", "19 November 2024"),
    ListDetailPostCommunity(2, 5, R.drawable.ic_launcher_foreground, "Mira", "Is this a specific variety of plant?", "20 November 2024"),
    ListDetailPostCommunity(2, 6, R.drawable.ic_launcher_foreground, "Adi", "How often do you water it?", "21 November 2024"),

    ListDetailPostCommunity(3, 1, R.drawable.ic_launcher_foreground, "Hani", "Great job on keeping it healthy!", "19 November 2024"),
    ListDetailPostCommunity(3, 2, R.drawable.ic_launcher_foreground, "Fina", "That plant looks amazing, good job!", "20 November 2024"),
    ListDetailPostCommunity(3, 3, R.drawable.ic_launcher_foreground, "Nina", "Can you share some tips for beginners?", "21 November 2024"),
    ListDetailPostCommunity(3, 4, R.drawable.ic_launcher_foreground, "Anisa", "I’ve never seen a plant so vibrant!", "22 November 2024"),

    ListDetailPostCommunity(4, 1, R.drawable.ic_launcher_foreground, "Zahra", "That plant looks very well cared for!", "20 November 2024"),
    ListDetailPostCommunity(4, 2, R.drawable.ic_launcher_foreground, "Ali", "Beautiful plant, looks so healthy!", "21 November 2024"),

    ListDetailPostCommunity(5, 1, R.drawable.ic_launcher_foreground, "Reza", "It's so beautiful and green!", "21 November 2024"),
    ListDetailPostCommunity(5, 2, R.drawable.ic_launcher_foreground, "Amir", "Nice plant!", "22 November 2024"),
    ListDetailPostCommunity(5, 3, R.drawable.ic_launcher_foreground, "Rina", "I wish my plants looked like that!", "11 November 2024"),
    ListDetailPostCommunity(5, 4, R.drawable.ic_launcher_foreground, "Dina", "I need some advice on plant care!", "12 November 2024"),
    ListDetailPostCommunity(5, 5, R.drawable.ic_launcher_foreground, "Jaya", "This is a dream plant!", "13 November 2024"),

    ListDetailPostCommunity(6, 1, R.drawable.ic_launcher_foreground, "Hendra", "This one is my favorite, so lush!", "23 November 2024"),
    ListDetailPostCommunity(6, 2, R.drawable.ic_launcher_foreground, "Tari", "Looks amazing, what’s the name of the plant?", "24 November 2024")
)

fun getDetailPostById(idPost: Int): List<ListDetailPostCommunity> {
    return dummyListDetailPostCommunity.filter { it.idPost == idPost }
}
fun getDetailPostCommunityByidPostAndidCommentId(idPost: Int, idComment: Int): ListDetailPostCommunity? {
    // Mengambil data berdasarkan kedua ID
    return dummyListDetailPostCommunity.find { it.idPost== idPost && it.idComment == idComment }
}