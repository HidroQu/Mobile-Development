package com.capstone.hidroqu.utils

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.core.content.getSystemService
import com.capstone.hidroqu.nonui.receiver.HarvestNotificationReceiver
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HarvestNotificationHelper(private val context: Context) {
    companion object {
        const val CHANNEL_ID = "harvest_notification_channel"
        const val CHANNEL_NAME = "Harvest Notifications"
        private val NOTIFICATION_MINUTES = listOf(1, 2, 3, 4, 5)
        private val NOTIFICATION_DAYS = listOf(7, 6, 5, 4, 3, 2, 1)
        private const val DEBUG = false
    }

    private val alarmManager: AlarmManager? = context.getSystemService()
    private val notificationManager: NotificationManager? = context.getSystemService()

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for harvest notifications"
                enableVibration(true)
            }
            notificationManager?.createNotificationChannel(channel)
        }
    }

    fun canScheduleExactAlarms(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager?.canScheduleExactAlarms() == true
        } else {
            true
        }
    }

    fun getAlarmPermissionSettingsIntent(): Intent {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
        } else {
            Intent(Settings.ACTION_SETTINGS)
        }
    }

    fun scheduleHarvestReminders(plantName: String, harvestDateStr: String): Boolean {
        try {
            if (DEBUG) {
                scheduleDebugNotifications(plantName)
            } else {
                scheduleProductionNotifications(plantName, harvestDateStr)
            }
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    private fun scheduleDebugNotifications(plantName: String) {
        cancelHarvestReminders("debug")

        NOTIFICATION_MINUTES.forEach { minutesFromNow ->
            val notificationTime = Calendar.getInstance().apply {
                add(Calendar.MINUTE, minutesFromNow)
            }

            val intent = Intent(context, HarvestNotificationReceiver::class.java).apply {
                putExtra("plantName", plantName)
                putExtra("daysBeforeHarvest", minutesFromNow)
                putExtra("notificationId", minutesFromNow)
                putExtra("isDebug", true)
            }

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                minutesFromNow,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            alarmManager?.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                notificationTime.timeInMillis,
                pendingIntent
            )
        }
    }

    private fun scheduleProductionNotifications(plantName: String, harvestDateStr: String) {
        cancelHarvestReminders(harvestDateStr)

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("id", "ID"))
        val harvestDate = dateFormat.parse(harvestDateStr) ?: return
        val harvestCalendar = Calendar.getInstance().apply {
            time = harvestDate
            set(Calendar.HOUR_OF_DAY, 8)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        NOTIFICATION_DAYS.forEach { daysBeforeHarvest ->
            val notificationTime = Calendar.getInstance().apply {
                time = harvestCalendar.time
                add(Calendar.DAY_OF_YEAR, - daysBeforeHarvest)
            }
            if (notificationTime.after(Calendar.getInstance())) {
                val intent = Intent(context, HarvestNotificationReceiver::class.java).apply {
                    putExtra("plantName", plantName)
                    putExtra("daysBeforeHarvest", daysBeforeHarvest)
                    putExtra("notificationId", generateNotificationId(harvestDateStr, daysBeforeHarvest))
                    putExtra("isDebug", false)
                }

                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    generateNotificationId(harvestDateStr, daysBeforeHarvest),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                alarmManager?.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    notificationTime.timeInMillis,
                    pendingIntent
                )
            }
        }
    }


    fun cancelHarvestReminders(harvestDateStr: String) {
        if (DEBUG) {
            NOTIFICATION_MINUTES.forEach { minute ->
                val intent = Intent(context, HarvestNotificationReceiver::class.java)
                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    minute,
                    intent,
                    PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
                )
                pendingIntent?.let {
                    alarmManager?.cancel(it)
                }
            }
        } else {
            NOTIFICATION_DAYS.forEach { daysBeforeHarvest ->
                val intent = Intent(context, HarvestNotificationReceiver::class.java).apply {
                    putExtra("daysBeforeHarvest", daysBeforeHarvest)
                }
                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    generateNotificationId(harvestDateStr, daysBeforeHarvest),
                    intent,
                    PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
                )
                pendingIntent?.let {
                    alarmManager?.cancel(it)
                }
            }
        }
    }

    private fun generateNotificationId(harvestDateStr: String, daysBeforeHarvest: Int): Int {
        return (harvestDateStr.hashCode() + daysBeforeHarvest).hashCode()
    }
}