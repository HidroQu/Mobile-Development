package com.capstone.hidroqu.utils

import android.os.Build
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun formatDate(dateTime: String): String {
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale("id", "ID"))
            val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale("id", "ID"))
            val date = LocalDate.parse(dateTime.substring(0, 10)) // Hanya ambil bagian tanggal
            date.format(outputFormatter) // Format ke dd/MM/yyyy
        } else {
            val inputFormatter = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale("id", "ID"))
            val outputFormatter = java.text.SimpleDateFormat("dd/MM/yyyy", Locale("id", "ID"))
            val date = inputFormatter.parse(dateTime)
            outputFormatter.format(date!!)
        }
    } catch (e: Exception) {
        "00/00/0000"
    }
}