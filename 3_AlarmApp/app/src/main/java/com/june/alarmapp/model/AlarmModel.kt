package com.june.alarmapp.model

data class AlarmModel (val hour: Int, val minute: Int, var isOn: Boolean) {
    val timeText: String
        get() {
            val h = "%02d".format(if (hour<12) hour else hour -12)
            val m = "%02d".format(minute)
            return "$h:$m"
        }

    val ampmText: String
        get() {
            return if (hour < 12) "AM" else "PM"
        }

    val onOffText: String
        get() {
            return  if(isOn) "알람 끄기" else "알람 켜기"
        }

    fun makeTimeFormat(): String {
        return "$hour:$minute"
    }
}