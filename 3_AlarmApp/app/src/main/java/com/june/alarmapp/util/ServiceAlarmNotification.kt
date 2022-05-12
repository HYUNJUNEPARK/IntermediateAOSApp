package com.june.alarmapp.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.os.Build
import androidx.core.app.NotificationCompat
import com.june.alarmapp.R
import com.june.alarmapp.key.AlarmKey.Companion.SERVICE_NOTIFICATION_CHANNEL_ID
import com.june.alarmapp.key.AlarmKey.Companion.SERVICE_NOTIFICATION_CHANNEL_NAME
import com.june.alarmapp.key.AlarmKey.Companion.SERVICE_NOTIFICATION_ID

class ServiceAlarmNotification(private val service: Service) {
    fun createForegroundServiceNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                SERVICE_NOTIFICATION_CHANNEL_ID,
                SERVICE_NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = service.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    fun notifyForegroundServiceNotification() {
        val notification: Notification = NotificationCompat.Builder(service, SERVICE_NOTIFICATION_CHANNEL_ID)
            .setContentTitle("알람 설정됨")
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .build()
        service.startForeground(SERVICE_NOTIFICATION_ID, notification)
    }
}