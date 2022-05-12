package com.june.alarmapp.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.june.alarmapp.util.ServiceAlarmNotification

class AlarmForegroundService : Service() {
    override fun onBind(intent: Intent): IBinder {
        return Binder()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val alarmNotification = ServiceAlarmNotification(this)
        alarmNotification.createForegroundServiceNotificationChannel()
        alarmNotification.notifyForegroundServiceNotification()
        return super.onStartCommand(intent, flags, startId)
    }
}