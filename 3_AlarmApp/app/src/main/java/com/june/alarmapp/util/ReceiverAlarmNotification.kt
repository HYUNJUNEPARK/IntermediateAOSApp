package com.june.alarmapp.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.june.alarmapp.R
import com.june.alarmapp.key.AlarmKey
import com.june.alarmapp.key.AlarmKey.Companion.NOTIFICATION_CHANNEL_ID
import com.june.alarmapp.key.AlarmKey.Companion.NOTIFICATION_CHANNEL_NAME
import com.june.alarmapp.key.AlarmKey.Companion.NOTIFICATION_ID

class ReceiverAlarmNotification(val context: Context) {
    fun createReceiverNotificationChannel() {
        Log.d("testLog", "createReceiverNotificationChannel: channel created")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH,
            )
            NotificationManagerCompat.from(context).createNotificationChannel(notificationChannel)
        }
    }
    fun notifyReceiverNotification() {
        with(NotificationManagerCompat.from(context)) {
            val build = NotificationCompat.Builder(context, AlarmKey.NOTIFICATION_CHANNEL_ID)
                .setContentTitle(context.getString(R.string.alarm_title))
                .setContentText(context.getString(R.string.alarm_text))
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
            notify(NOTIFICATION_ID, build.build())
        }
    }
}