package com.june.alarmapp.activity

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.june.alarmapp.R
import com.june.alarmapp.databinding.ActivityMainBinding
import com.june.alarmapp.model.Alarm
import com.june.alarmapp.receiver.AlarmReceiver
import java.util.*

class MainActivity : AppCompatActivity() {
    private val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}
    companion object {
        private const val SHARED_PREFERENCE_NAME = "time"
        private const val ALARM_KEY = "alarm"
        private const val ON_OFF_KEY ="isON"
        private const val ALARM_REQUEST_CODE = 1000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initOnOffButton()
        initChangeAlarmButton()

        val model = fetchAlarmFromSP()
        updateViews(model)
    }

    private fun initOnOffButton() {
        binding.onOffButton.setOnClickListener { view ->
            val model = view.tag as? Alarm ?: return@setOnClickListener
            val newModel = saveAlarmToSP(model.hour, model.minute, model.isOn.not())
            updateViews(newModel)
            //Alarm On
            if (newModel.isOn) {
                view.setBackgroundColor(getColor(R.color.red))
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
                alarmManager.setInexactRepeating( //cf. alarmManger.setAndAllowWhileIdle(), alarmManager.setExact()
                    AlarmManager.RTC_WAKEUP,
                    triggerTime.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
                )
            }
            //Alarm OFF
            else {
                view.setBackgroundColor(getColor(R.color.gray))
                cancelAlarm()
            }
        }
    }

    private fun initChangeAlarmButton() {
        binding.changeAlarmTimeButton.setOnClickListener {
            val systemTime = Calendar.getInstance()
            TimePickerDialog(
                this,
                { /*view: TimePicker!*/_, hour, minute ->
//                    val model = saveAlarmToSP(hour, minute, false)
                    val model = Alarm(hour, minute, false)
                    updateViews(model)
                    cancelAlarm()
                },
                systemTime.get(Calendar.HOUR_OF_DAY),
                systemTime.get(Calendar.MINUTE),
                false
            ).show()
        }
    }

    private fun saveAlarmToSP(hour: Int, minute: Int, isOn: Boolean): Alarm {
        val model = Alarm(hour, minute, isOn)
        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString(ALARM_KEY, model.makeTimeFormat()/*$hour:$minute*/)
            putBoolean(ON_OFF_KEY, model.isOn)
            commit()
        }
        return model
    }

    private fun updateViews(model: Alarm) {
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


    private fun fetchAlarmFromSP(): Alarm {
        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
        val timeSPValue = sharedPreferences.getString(ALARM_KEY, /*default*/"12:00") ?: "12:00"
        val alarmFormat = timeSPValue.split(":")
        val onOffSPValue = sharedPreferences.getBoolean(ON_OFF_KEY, /*default*/false)
        val alarm = Alarm(
            hour = alarmFormat[0].toInt(),
            minute = alarmFormat[1].toInt(),
            isOn = onOffSPValue
        )

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            ALARM_REQUEST_CODE,
            Intent(this, AlarmReceiver::class.java),
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











