package com.june.alarmapp.activity

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.june.alarmapp.key.AlarmKey
import com.june.alarmapp.model.AlarmModel
import com.june.alarmapp.receiver.AlarmReceiver

class AlarmSharedPreference(val context: Context) {
    fun alarmSharedPreference(hour: Int, minute: Int, isOn: Boolean): AlarmModel {
        val model = AlarmModel(hour, minute, isOn)
        val sharedPreferences = context.getSharedPreferences(
            AlarmKey.SHARED_PREFERENCE_NAME,
            Context.MODE_PRIVATE
        )
        with(sharedPreferences.edit()) {
            putString(AlarmKey.ALARM_KEY, model.makeTimeFormat()/*$hour:$minute*/)
            putBoolean(AlarmKey.ON_OFF_KEY, model.isOn)
            commit()
        }
        return model
    }

    fun alarmFetchSharedPreference(): AlarmModel {
        val sharedPreferences = context.getSharedPreferences(AlarmKey.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
        val timeSPValue = sharedPreferences.getString(AlarmKey.ALARM_KEY, /*default*/"12:00") ?: "12:00"
        val alarmFormat = timeSPValue.split(":")
        val onOffSPValue = sharedPreferences.getBoolean(AlarmKey.ON_OFF_KEY, /*default*/false)
        val alarm = AlarmModel(
            hour = alarmFormat[0].toInt(),
            minute = alarmFormat[1].toInt(),
            isOn = onOffSPValue
        )

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            AlarmKey.ALARM_REQUEST_CODE,
            Intent(context, AlarmReceiver::class.java),
            PendingIntent.FLAG_NO_CREATE
        )

        if((pendingIntent == null) and alarm.isOn) {
            alarm.isOn = false
        }
        else if ((pendingIntent != null) and alarm.isOn.not()){
            pendingIntent.cancel()
        }
        return alarm
    }
}