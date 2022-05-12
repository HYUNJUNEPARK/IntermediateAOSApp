package com.june.alarmapp.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.os.Build
import androidx.core.app.NotificationCompat
import com.june.alarmapp.R
import com.june.alarmapp.key.AlarmKey

class ServiceAlarmNotification(private val service: Service) {
    fun createForegroundServiceNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                AlarmKey.CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = service.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    fun notifyForegroundServiceNotification() {
        val notification: Notification = NotificationCompat.Builder(service, AlarmKey.CHANNEL_ID)
            .setContentTitle("알람 설정됨")
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .build()
        service.startForeground(1, notification)
    }
}