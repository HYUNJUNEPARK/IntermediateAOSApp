package com.june.alarmapp.activity

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.june.alarmapp.databinding.ActivityMainBinding
import com.june.alarmapp.model.AlarmDisplayModel
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
        initChangeAlarmTimeButton()

        //뷰 초기화
        val model = fetchDataFromSharedPreferences()
        renderView(model)
    }

    private fun initOnOffButton() {
        binding.onOffButton.setOnClickListener { view ->

            val model = view.tag as? AlarmDisplayModel ?: return@setOnClickListener //데이터를 확인한다
            val newModel = saveAlarmModel(model.hour, model.minute, model.isOn.not()) //데이터 저장
            renderView(newModel)

            if (newModel.isOn) { //켜진경우 -> 알람등록
                val calendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, newModel.hour)
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

                //정확한 주기로 사용하고 싶다면 alarmManager.setExact() 사용 단, 단말기 자원 소모가 상대적으로 심함
                //도지 모드에서도 동작하게 하는 API alarmManger.setAndAllowWhileIdle()
                alarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
                )
            }
            else { //꺼진 경우 -> 알람 제거
                cancelAlarm()
            }

        }
    }


    private fun initChangeAlarmTimeButton() {
        binding.changeAlarmTimeButton.setOnClickListener {
            val systemTime = Calendar.getInstance()

            TimePickerDialog(
                this,
                { picker, hour, minute ->
                    val model = saveAlarmModel(hour, minute, false)
                    renderView(model)//뷰를 업데이트한다

                    cancelAlarm()
                },
                systemTime.get(Calendar.HOUR_OF_DAY),
                systemTime.get(Calendar.MINUTE),
                false
            ).show()
        }
    }

    private fun saveAlarmModel(hour: Int, minute: Int, isOn: Boolean): AlarmDisplayModel {
        val model = AlarmDisplayModel(hour, minute, isOn)
        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString(ALARM_KEY, model.makeDataForDB()/*return "$hour:$minute"*/)
            putBoolean(ON_OFF_KEY, model.isOn)
            commit()
        }
        return model
    }

    private fun fetchDataFromSharedPreferences(): AlarmDisplayModel {
        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
        val timeDBValue = sharedPreferences.getString(ALARM_KEY, /*default*/"12:00") ?: "12:00"
        val onOffDBValue = sharedPreferences.getBoolean(ON_OFF_KEY, /*default*/false)
        val alarmData = timeDBValue.split(":")
        val alarmModel = AlarmDisplayModel(
            hour = alarmData[0].toInt(),
            minute = alarmData[1].toInt(),
            isOn = onOffDBValue
        )
        //보정 예외처리
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            ALARM_REQUEST_CODE,
            Intent(this, AlarmReceiver::class.java),
            PendingIntent.FLAG_NO_CREATE //있으면 만들고 없으면 안만듬(null)
        )
        if((pendingIntent == null) and alarmModel.isOn) {
            //알람은 꺼져있는데 데이터는 켜져있는 경우 -> 데이터 수정
            alarmModel.isOn = false
        }
        //알람은 등록되어 있는데 알람 모델의 온오프값이 꺼져있음(실제로 데이터가 등록되어 있지 않을 때)
        //(알람은 켜져있는데, 데이터는 꺼져있는 경우)
        //등록 알람을 꺼줌
        else if ((pendingIntent != null) and alarmModel.isOn.not()){
            pendingIntent.cancel()
        }
        return alarmModel
    }

    private fun renderView(model: AlarmDisplayModel) { //updateViews 로 바꾸는게 더 나을 것 같음
        binding.ampmTextView.text = model.ampmText
        binding.timeTextView.text = model.timeText
        binding.onOffButton.text = model.onOffText
        binding.onOffButton.tag = model//모델 전역변수가 없어 뷰를 업데이트한 모델을 저장할 곳이 없음 ->모델을 잠시 태그에 저장해두고 버튼을 눌렀을 때 태그에 있는 모델을 로드해 사용
        //태그에 저장된 데이터는 Object 로 저장되어 있기 때문에 꺼내올 때는 형변환을 해줘야한다
    }

    private fun cancelAlarm() {
        //기존에 있던 알람을 삭제한다
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            ALARM_REQUEST_CODE,
            Intent(this, AlarmReceiver::class.java),
            PendingIntent.FLAG_NO_CREATE //있으면 만들고 없으면 안만듬(null)
        )
        pendingIntent?.cancel() //처음 설정한 알람이 없다면 null 일수 있기 때문에 ? 사용
    }
}












