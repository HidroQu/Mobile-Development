package com.capstone.hidroqu.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.capstone.hidroqu.R
import com.capstone.hidroqu.utils.HarvestNotificationHelper

class HarvestNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        val plantName = intent.getStringExtra("plantName")
        if (plantName == null) {
            return
        }

        val daysBeforeHarvest = intent.getIntExtra("daysBeforeHarvest", 0)
        val notificationId = intent.getIntExtra("notificationId", 0)
        val isDebug = intent.getBooleanExtra("isDebug", false)

        Log.d("NotificationDebug", "Processing notification for plant: $plantName, days: $daysBeforeHarvest")

        try {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val notificationText = if (isDebug) {
                if (daysBeforeHarvest == 0) {
                    "[TEST] Notifikasimu akan muncul seperti ini!"
                } else {
                    "[TEST] Notifikasimu nanti kana muncul seperti ini!"
                }
            } else {
                when (daysBeforeHarvest) {
                    7 -> "Tanaman $plantName akan siap panen dalam 7 hari lagi! Mulai persiapkan peralatanmu."
                    6 -> "Tanaman $plantName akan siap panen dalam 6 hari lagi."
                    5 -> "Tanaman $plantName akan siap panen dalam 5 hari lagi."
                    4 -> "Tanaman $plantName akan siap panen dalam 4 hari lagi."
                    3 -> "Tinggal 3 hari lagi sebelum panen tanaman $plantName! Pastikan semua persiapan sudah siap."
                    2 -> "Tanaman $plantName akan siap panen dalam 2 hari lagi."
                    1 -> "Besok adalah waktu panen untuk tanaman $plantName! Persiapkan peralatanmu."
                    else -> "Tanaman $plantName akan siap panen dalam $daysBeforeHarvest hari lagi!"
                }
            }

            val notification = NotificationCompat.Builder(context, HarvestNotificationHelper.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(if (isDebug) "TEST: Pengingat Panen" else "Pengingat Panen")
                .setContentText(notificationText)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setAutoCancel(true)
                .setStyle(NotificationCompat.BigTextStyle().bigText(notificationText))
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .build()

            notificationManager.notify(notificationId, notification)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}