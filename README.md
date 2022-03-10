1. Calender
-시스템 시간을 가져올 수 있음

```kotlin
val calendar = Calendar.getInstance()
calendar.get(Calendar.HOUR_OF_DAY)
calendar.get(Calendar.MINUTE)
```



2. TimePickerDialog
-시간 설정 기능이 내장된 클래스
-처음 다이얼로그의 화면에 표시될 시간을 hourOfDay, minute 으로 세팅
`TimePickerDialog(context, listener, hourOfDay, minute, is24HourView)`

- listener: TimePickerDialog.OnTimeSetListener!
`abstract fun onTimeSet(view: TimePicker!, hourOfDay: Int, minute: Int): Unit`

```kotlin
val systemTime = Calendar.getInstance()

TimePickerDialog(
    this,
    { view, hour, minute ->
        //view, hour, minute 데이터를 갖고 상황에 맞게 커스텀
    },
    systemTime.get(Calendar.HOUR_OF_DAY),
    systemTime.get(Calendar.MINUTE),
    false
).show()
```



3. pendingIntent
-갖고 있는 인텐트를 당장 수행하지 않고 특정 시점(앱이 구동되고 있지 않을 때)에서 수행

사용 사례
(a) Notification (푸시알림) 으로 Intent 작업 수행시 사용
(b) 바탕화면 (런쳐) 위젯에서 Intent 작업 수행 시 사용
(c) AlarmManager 를 통해 지정된 시간에 Intent 작업 수행시 사용
-> 다른 앱이 프로세스를 점유하고 있을 때 실행된다는 점이 공통점이 있음

컴포넌트의 유형에 따라 생성자를 호출하는 방식이 다름
`PendingIntent.getActivity(Context, requestCode, Intent, FLAG)`

`PendingIntent.getService(Context, requestCode, Intent, FLAG)`

`PendingIntent.getBroadcast(Context, requestCode, Intent, FLAG)`

플래그
FLAG_CANCEL_CURRENT : 이전에 생성한 PendingIntent 취소 후 새로 생성
FLAG_NO_CREATE : 이미 생성된 PendingIntent 가 있다면 재사용 (없으면 Null 리턴)
FLAG_ONE_SHOT : 해당 PendingIntent 를 일회성으로 사용
FLAG_UPDATE_CURRENT : 이미 생성된 PendingIntent 가 있다면, Extra Data 만 갈아끼움 (업데이트)



4. AlarmManger
-시스템 알람 서비스에 접근할 수 있는 클래스로 앱이 특정 시점에 동작할 수 있도록 함
-시스템에 의해 동록된 펜딩인텐트는 특정 시점이 되면 앱을 자동으로 동작 시킴
-setInexactRepeating(), setAndAllowWhileIdle(), setExact() 등을 호출해 시스템에 알람 정보를 최종 전달

```java
public void setInexactRepeating (int type,
                long triggerAtMillis,
                long intervalMillis,
                PendingIntent operation)
```



pendingIntent
https://velog.io/@haero_kim/Android-PendingIntent-%EA%B0%9C%EB%85%90-%EC%9D%B5%ED%9E%88%EA%B8%B0

AlarmManger
https://developer.android.com/reference/android/app/AlarmManager

