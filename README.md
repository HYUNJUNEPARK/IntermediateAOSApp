Immediate tasks
-즉시 해야하는 작업
Thread
Handler
Coroutine

Deferred tasks
-지연된 작업
WorkManger

Exact tasks
-정시 실행해야하는 작업
AlarmManager
반복된 시간에 팬딩 이벤트를 실행함
인텐트를 팬딩 시켜놨다가 실행시킴
실제 시간(Real Time) 으로 실행시키는 방법
기기가 부팅된 시점부터 얼마가 지났는지(Elapsed Time) 으로 실행시키는 방법




TimePickerDialog
-시간 설정 기능이 내장된 클래스
-처음 다이얼로그의 화면에 표시될 시간을 hourOfDay, minute 으로 세팅
`TimePickerDialog(context, listener, hourOfDay, minute, is24HourView)`

- listener: TimePickerDialog.OnTimeSetListener!
`abstract fun onTimeSet(view: TimePicker!, hourOfDay: Int, minute: Int): Unit`

```kotlin
TimePickerDialog(
    this,
    { picker, hour, minute ->
    val model = saveAlarmModel(hour, minute, false)
    renderView(model)
    },
    calendar.get(Calendar.HOUR_OF_DAY),
    calendar.get(Calendar.MINUTE),
    false
).show()
```





Calender
-시스템 시간을 가져올 수 있음

```kotlin
val calendar = Calendar.getInstance()
calendar.get(Calendar.HOUR_OF_DAY)
calendar.get(Calendar.MINUTE)
```





Notification



BroadCast Receiver



sharedPreference
-속성값 저장