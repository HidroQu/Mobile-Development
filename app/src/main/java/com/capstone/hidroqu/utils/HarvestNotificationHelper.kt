package com.capstone.hidroqu.utils

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.capstone.hidroqu.R
import com.capstone.hidroqu.receiver.HarvestNotificationReceiver
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class HarvestNotificationHelper(private val context: Context) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    companion object {
        const val CHANNEL_ID = "harvest_reminder"
        const val CHANNEL_NAME = "Harvest Reminder"
        private val REMINDER_DAYS = listOf(7, 6, 5, 4, 3, 2, 1)
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH // Harus menggunakan IMPORTANCE_HIGH agar notifikasi dapat muncul dengan benar
            ).apply {
                description = "Channel for harvest reminder notifications"
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    // Fungsi untuk mengecek apakah aplikasi memiliki izin untuk menjadwalkan alarm eksak
    fun canScheduleExactAlarms(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true // Untuk versi Android di bawah S (12), izin selalu dianggap ada
        }
    }

    // Fungsi untuk mendapatkan Intent ke pengaturan alarm
    fun getAlarmPermissionSettingsIntent(): Intent {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
        } else {
            Intent(Settings.ACTION_SETTINGS)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun scheduleHarvestReminders(plantName: String, harvestDate: String): Boolean {
        if (!canScheduleExactAlarms()) {
            return false
        }

        try {
            // Parse harvest date
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale("id", "ID"))
            val harvestLocalDate = LocalDate.parse(harvestDate, formatter)

            android.util.Log.d("HarvestNotification", "Tanggal panen: $harvestDate")

            // Schedule notifications for each reminder day
            REMINDER_DAYS.forEach { daysBeforeHarvest ->
                val notificationDate = harvestLocalDate.minusDays(daysBeforeHarvest.toLong())
                val notificationDateTime = notificationDate.atTime(8, 0)

                android.util.Log.d("HarvestNotification",
                    "Menjadwalkan notifikasi untuk $plantName: " +
                            "H-$daysBeforeHarvest pada tanggal ${notificationDate.format(formatter)} jam 8:00"
                )

                scheduleNotification(
                    plantName,
                    daysBeforeHarvest,
                    notificationDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                    (harvestLocalDate.toEpochDay() * daysBeforeHarvest).toInt()
                )
            }
            return true
        } catch (e: SecurityException) {
            android.util.Log.e("HarvestNotification", "Security Exception: ${e.message}")
            e.printStackTrace()
            return false
        } catch (e: Exception) {
            android.util.Log.e("HarvestNotification", "Error: ${e.message}")
            e.printStackTrace()
            return false
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun cancelHarvestReminders(harvestDate: String) {
        if (!canScheduleExactAlarms()) {
            return
        }

        try {
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale("id", "ID"))
            val harvestLocalDate = LocalDate.parse(harvestDate, formatter)

            REMINDER_DAYS.forEach { daysBeforeHarvest ->
                val notificationId = (harvestLocalDate.toEpochDay() * daysBeforeHarvest).toInt()
                val intent = Intent(context, HarvestNotificationReceiver::class.java)
                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    notificationId,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                alarmManager.cancel(pendingIntent)
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun scheduleNotification(
        plantName: String,
        daysBeforeHarvest: Int,
        timeInMillis: Long,
        notificationId: Int
    ) {
        if (!canScheduleExactAlarms()) {
            return
        }

        try {
            val intent = Intent(context, HarvestNotificationReceiver::class.java).apply {
                putExtra("plantName", plantName)
                putExtra("daysBeforeHarvest", daysBeforeHarvest)
                putExtra("notificationId", notificationId)
            }

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                notificationId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                timeInMillis,
                pendingIntent
            )
        } catch (e: SecurityException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}