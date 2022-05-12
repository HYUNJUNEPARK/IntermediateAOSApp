package com.june.alarmapp.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.june.alarmapp.util.ReceiverAlarmNotification
import com.june.alarmapp.util.ServiceAlarmNotification

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        Log.d("testLog", "AlarmReceiver onReceive: ")
        val alarmNotification = ReceiverAlarmNotification(context)
        alarmNotification.createReceiverNotificationChannel()
        alarmNotification.notifyReceiverNotification()
    }
}