# AlarmApp

<img src="https://github.com/HYUNJUNEPARK/ImageRepository/blob/master/IntermediateApp/4_AlarmApp.jpg" height="400"/>

---
1. <a href = "#content1">Calendar</a></br>
2. <a href = "#content2">TimePickerDialog</a></br>
3. <a href = "#content3">pendingIntent</a></br>
4. <a href = "#content4">AlarmManger</a></br>
5. <a href = "#content5">view.tag</a></br>
6. <a href = "#content6">get() 으로 변수 초기화<</a></br>

* <a href = "#ref">참고링크</a>
---
><a id = "content1">**1. Calendar**</a></br>


-시스템 시간을 가져올 수 있음</br>


```kotlin
val calendar = Calendar.getInstance()
calendar.get(Calendar.HOUR_OF_DAY)
calendar.get(Calendar.MINUTE)
```

<br></br>
<br></br>

><a id = "content2">**2. TimePickerDialog**</a></br>

-시간 설정 기능이 내장된 클래스</br>
-처음 다이얼로그의 화면에 표시될 시간을 hourOfDay, minute 으로 세팅</br>
`TimePickerDialog(context, listener, hourOfDay, minute, is24HourView)`</br>
- listener: TimePickerDialog.OnTimeSetListener!</br>
`abstract fun onTimeSet(view: TimePicker!, hourOfDay: Int, minute: Int): Unit`</br>

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

<br></br>
<br></br>

><a id = "content3">**3. pendingIntent**</a></br>

-갖고 있는 인텐트를 당장 수행하지 않고 특정 시점(앱이 구동되고 있지 않을 때)에서 수행</br>

**사용 사례**
(a) Notification (푸시알림) 으로 Intent 작업 수행시 사용</br>
(b) 바탕화면 (런쳐) 위젯에서 Intent 작업 수행 시 사용</br>
(c) AlarmManager 를 통해 지정된 시간에 Intent 작업 수행시 사용</br>
-> 다른 앱이 프로세스를 점유하고 있을 때 실행된다는 점이 공통점이 있음</br>

**컴포넌트의 유형에 따라 생성자를 호출하는 방식이 다름**
`PendingIntent.getActivity(Context, requestCode, Intent, FLAG)`</br>
`PendingIntent.getService(Context, requestCode, Intent, FLAG)`</br>
`PendingIntent.getBroadcast(Context, requestCode, Intent, FLAG)`</br>

**플래그**</br>
FLAG_CANCEL_CURRENT : 이전에 생성한 PendingIntent 취소 후 새로 생성</br>
FLAG_NO_CREATE : 이미 생성된 PendingIntent 가 있다면 재사용 (없으면 Null 리턴)</br>
FLAG_ONE_SHOT : 해당 PendingIntent 를 일회성으로 사용</br>
FLAG_UPDATE_CURRENT : 이미 생성된 PendingIntent 가 있다면, Extra Data 만 갈아끼움 (업데이트)</br>

<br></br>
<br></br>

><a id = "content4">4. AlarmManger</a></br>

-시스템 알람 서비스에 접근할 수 있는 클래스로 앱이 특정 시점에 동작할 수 있도록 함</br>
-시스템에 의해 동록된 펜딩인텐트는 특정 시점이 되면 앱을 자동으로 동작 시킴</br>
-setInexactRepeating(), setAndAllowWhileIdle(), setExact() 등을 호출해 시스템에 알람 정보를 최종 전달</br>

```java
public void setInexactRepeating (int type,
                long triggerAtMillis,
                long intervalMillis,
                PendingIntent operation)
```

<br></br>
<br></br>

><a id = "content5">5. view.tag</a></br>

-모델을 잠시 뷰의 태그에 저장해 두고 클릭했을 때 태그에 저장해 둔 모델을 로드해 사용</br>
-태그에 저장되어있던 데이터는 Object 로 저장되어 있기 때문에 로드할 때는 형변환이 필요</br>

```kotlin
//tag 에 모델 저장
private fun updateViews(model: Alarm) {
    //...
    binding.onOffButton.tag = model
}

//모델 로드, 형변환
binding.onOffButton.setOnClickListener { view ->
    val model = view.tag as? Alarm ?: return@setOnClickListener
    //...
}
```
<br></br>
<br></br>


><a id = "content6">6. get() 으로 변수 초기화</a></br>

```kotlin
data class Alarm (val hour: Int, val minute: Int, var isOn: Boolean) {
    val timeText: String
        get() {
            val h = "%02d".format(if (hour<12) hour else hour -12)
            val m = "%02d".format(minute)
            return "$h:$m"
        }
}
```
<br></br>
<br></br>
---

><a id = "ref">**참고링크**</a></br>

pendingIntent</br>
https://velog.io/@haero_kim/Android-PendingIntent-%EA%B0%9C%EB%85%90-%EC%9D%B5%ED%9E%88%EA%B8%B0</br>

AlarmManager</br>
https://developer.android.com/reference/android/app/AlarmManager</br>

Calendar</br>
https://developer.android.com/reference/kotlin/android/icu/util/Calendar

