package com.june.alarmapp.activity

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.june.alarmapp.R
import com.june.alarmapp.databinding.ActivityMainBinding
import com.june.alarmapp.key.AlarmKey.Companion.ALARM_REQUEST_CODE
import com.june.alarmapp.model.AlarmModel
import com.june.alarmapp.receiver.AlarmReceiver
import com.june.alarmapp.service.AlarmForegroundService
import java.util.*

class MainActivity : AppCompatActivity() {
    private val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}
    lateinit var alarmSharedPreference: AlarmSharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        alarmSharedPreference = AlarmSharedPreference(this)
        val model = alarmSharedPreference.alarmFetchSharedPreference()
        updateViews(model)
    }

    fun settingAlarmButtonPressed(view: View) {
        val systemTime = Calendar.getInstance()
        TimePickerDialog(
            this,
            { /*view: TimePicker!*/_, hour, minute ->
                val model = AlarmModel(hour, minute, false)
                updateViews(model)
                cancelAlarm()
            },
            systemTime.get(Calendar.HOUR_OF_DAY),
            systemTime.get(Calendar.MINUTE),
            false
        ).show()
    }

    private fun updateViews(model: AlarmModel) {
        binding.ampmTextView.text = model.ampmText
        binding.timeTextView.text = model.timeText
        binding.onOffButton.text = model.onOffText
        binding.onOffButton.tag = model
    }

    private fun cancelAlarm() {
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            ALARM_REQUEST_CODE,
            Intent(this, AlarmReceiver::class.java),
            PendingIntent.FLAG_NO_CREATE
        )
        pendingIntent?.cancel()
    }

    fun onOffButtonPressed(view: View) {
        val model = view.tag as? AlarmModel ?: return
        val newModel = alarmSharedPreference.alarmSharedPreference(model.hour, model.minute, model.isOn.not())
        updateViews(newModel)
        //Alarm On
        if (newModel.isOn) {
            view.setBackgroundColor(getColor(R.color.red))

            //[START]
            val serviceIntent = Intent(this, AlarmForegroundService::class.java)
            ContextCompat.startForegroundService(this, serviceIntent)
            //[END]

            val triggerTime = Calendar.getInstance().apply {
                set(/*field*/Calendar.HOUR_OF_DAY, /*value*/newModel.hour)
                set(Calendar.MINUTE, newModel.minute)
                if (before(Calendar.getInstance())) {
                    add(Calendar.DATE, 1)
                }
            }
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(this, AlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                this,
                ALARM_REQUEST_CODE,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                triggerTime.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        }
        else {
            view.setBackgroundColor(getColor(R.color.gray))

            //[START]
            val serviceIntent = Intent(this, AlarmForegroundService::class.java)
            stopService(serviceIntent)
            //[END]

            cancelAlarm()
        }
    }
}












