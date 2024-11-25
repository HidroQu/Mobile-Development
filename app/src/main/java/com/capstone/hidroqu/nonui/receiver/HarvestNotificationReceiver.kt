package com.capstone.hidroqu.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.capstone.hidroqu.R
import com.capstone.hidroqu.utils.HarvestNotificationHelper

class HarvestNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val plantName = intent.getStringExtra("plantName") ?: return
        val daysBeforeHarvest = intent.getIntExtra("daysBeforeHarvest", 0)
        val notificationId = intent.getIntExtra("notificationId", 0)

        android.util.Log.d("HarvestNotification",
            "Menerima broadcast untuk $plantName: H-$daysBeforeHarvest")

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat.Builder(context, HarvestNotificationHelper.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Pengingat Panen $plantName")
            .setContentText("Tanaman $plantName akan siap panen dalam $daysBeforeHarvest hari lagi!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(notificationId, notification)

        android.util.Log.d("HarvestNotification",
            "Berhasil menampilkan notifikasi untuk $plantName: H-$daysBeforeHarvest")
    }
}