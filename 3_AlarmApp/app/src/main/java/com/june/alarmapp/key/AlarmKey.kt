package com.june.alarmapp.key

class AlarmKey {
    companion object {
        //Alarm
        const val SHARED_PREFERENCE_NAME = "time"
        const val ALARM_KEY = "alarm"
        const val ON_OFF_KEY ="isON"
        const val ALARM_REQUEST_CODE = 1000
        //BroadcastReceiver
        const val NOTIFICATION_ID = 100
        const val NOTIFICATION_CHANNEL_ID = "1000"
        const val NOTIFICATION_CHANNEL_NAME = "기상 알람"
        //Foreground Service
        const val SERVICE_NOTIFICATION_ID = 1
        const val SERVICE_NOTIFICATION_CHANNEL_ID = "ForegroundChannel"
        const val SERVICE_NOTIFICATION_CHANNEL_NAME = "Foreground Service Channel"

    }
}