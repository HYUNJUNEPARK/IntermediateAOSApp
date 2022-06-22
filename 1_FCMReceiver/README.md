# FCM Receiver

<img src="https://github.com/HYUNJUNEPARK/ImageRepository/blob/master/IntermediateApp/1_FCMReciever.png" height="400"/>

---
1. <a href = "#content1">FCM(Firebase Cloud Messaging)</a></br>
2. <a href = "#content2">Firebase 프로젝트 등록</a></br>
3. <a href = "#content3">Message Service</a></br>
4. <a href = "#content4">Notification</a></br>
4. <a href = "#content5">Pending Intent</a></br>
* <a href = "#ref">참고링크</a>
---
><a id = "content1">**1. Firebase Cloud Messaging(FCM)**</a></br>


-Firebase 에서 안드로이드, ISO, 웹 등으로 메시지를 보낼 수 있음</br>

(a) 데이터 메시지</br>
-앱 상태가 백그라운드, 포어그라운드에 있든지 자체적으로 메시지를 처리할 수 있어 개발이 유연함</br>
-대부분 데이터 메시지 사용</br>

(b) 알림 메시지</br>
-앱 상태에 제한이 있어 유연한 개발이 어려움(앱이 백그라운드에서 실행 될 때만 메시지 수신 가능)</br>

<br></br>
<br></br>

><a id = "content2">**2. Firebase 프로젝트 등록**</a></br>

(0) build.gradle</br>
//plugins</br>
`id 'com.google.gms.google-services'`</br>
//dependencies</br>
`implementation platform('com.google.firebase:firebase-bom:29.1.0')`</br>
`implementation 'com.google.firebase:firebase-analytics-ktx'`</br>

(1) 프로젝트를 생성하고 안드로이드 앱 추가 버튼 클릭</br>

(2) Android 패키지 이름에는 build.gradle - defaultConfig { applicationId "패키지 이름" } 입력</br>
간단한 알림만 수신하는 앱으로 SHA-1 입력하지 않음</br>

(3) Download google-services.json</br>

(4) Firebase SDK 추가</br>

(5) build.gradle - implementation 'com.google.firebase:firebase-messaging-ktx' 추가</br>

(6) 연결확인</br>

```kotlin
//MainActivity
private fun checkFCMToken() {
    FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
        if(task.isSuccessful) {
            Log.d("connectionCheck", "[Firebase Token] : ${task.result}")
        }
    }
}
```

cf. activity_main - textView 에 아래 속성을 추가하면 텍스트를 복사 할 수 있음</br>
`android:textIsSelectable`</br>

(7) 테스트 메시지 보내기(앱이 백그라운드에 있을 때 확인 가능)</br>
파이어베이스 프로젝트 -> 왼쪽 배너 '참여' - Cloud Messaging -> Send your first message 에서 테스트 메시지 보낼 수 있음

<br></br>
<br></br>


><a id = "content3">**3. Message Service**</a></br>

(1) 서비스를 만들고 AndroidManifest 의 해당 service 에 intent-filter 추가</br>
-디바이스로 넘어온 firebase message 는 해당 서비스에서 받는다는 뜻

```xml
<intent-filter>
    <action android:name="com.google.firebase.MESSAGING_EVENT"/>
</intent-filter>
```

(2) onNewToken() 오버라이딩</br>
-토큰은 언제든지 변경될 가능성이 있음</br>
-실제 라이브 서비스를 운영할 때 FirebaseMessagingService 를 상속받은 클래스에 onNewToken 을 오버라이딩해 토큰이 갱신될 때마다 서버토큰도 갱신시킴</br>

(3) 파이어베이스에서 지정 앱에 메시지를 보내기</br>
-우측에 있는 Try this method 를 채워넣음</br>
-`https://firebase.google.com/docs/reference/fcm/rest/v1/projects.messages/send`</br>

-**parent** : id 는 google-services.json - "project_id" 에서 확인 가능</br>
-**Request body**</br>
    ㄴ token, topic, condition 중 하나는 반드시 추가되어야 하며 이 앱에서는 token 을 사용</br>
    ㄴ message - data 를 추가하고 원하는 데이터를 key - value 쌍으로 입력 후 EXECUTE</br>

```
//Request body sample
{
    "message": {
        "token": "userToken"
        "data" : {
            "type" : CUSTOM
            "title" : "Title Of Test Message"
            "message" : "Message Of Test Message"
        }
    }
}
```

(4) 메시지 데이터 확인
-오버라이딩한 onMessageReceived() 에 break point 를 걸고 디버깅하면 데이터를 확인할 수 있음

```kotlin
//MyFirebaseMessagingService
override fun onMessageReceived(message: RemoteMessage) {
    super.onMessageReceived(message)
}
```

<br></br>
<br></br>

><a id = "content4">**4. Notification**</a></br>

-알림은 업데이트가 자주 일어남으로 호환성을 자주 체크해줘야함</br>
-안드로이드 8.0(오레오 버전)부터는 모든 알림에 채널을 할당해야함(이전 버전에서는 알림마다 중요도 수준을 별도 설정)</br>

**알림의 종류**</br>
(a) NORMAL</br>
(b) EXPANDABLE</br>
(c) CUSTOM</br>
-자체적인 레이아웃 만들어 사용 cf. res - layout - activity_main.xml</br>
    ㄴ res - layout 에 별도의 레이아웃 파일을 만듬</br>
    ㄴ RemoteView 에서는 constraintLayout 을 지원하지 않기 때문에 LinearLayout 을 사용</br>
-다양한 기기에 맞춰서 알림을 지원하는 것에 어려움이 있음</br>

<br></br>
<br></br>

><a id = "content5">**5. Pending Intent**</a></br>

**펜딩 인텐트(Pending Intent)**</br>
-Intent 를 갖고 있는 클래스로 갖고있는 인텐트를 보류하고 특정 시점에 작업을 요청하도록 함(AOS 의 NotificationManager 가 인텐트를 실행)</br>
(a) Notification 을 통해 인텐트를 실행</br>
(b) 특정 시점에 AlarmManager 를 이용하여 인텐트를 실행할 때 사용</br>

**펜딩 인텐트 FLAG**</br>
-FLAG_CANCEL_CURRENT : 이전에 생성한 PendingIntent 는 취소하고 새로 만듬</br>
-FLAG_NO_CREATE : 이미 생성된 PendingIntent 가 없다면 null return. 생성된 PendingIntent 가 있다면 해당 intent 반환 (재사용)</br>
-FLAG_ONE_SHOT : 한번만 사용 (일회용)</br>
-FLAG_UPDATE_CURRENT : 이미 생성된 PendingIntent 가 존재하면 해당 intent 의 extra data 만 변경</br>

**인텐트 FLAG**</br>
-FLAG_ACTIVITY_SINGLE_TOP</br>
    ㄴ 활성화 될 task 의 top 에 같은 activity 가 존재할 경우, 새로운 activity 를 top 으로 올리지 않고 기존의 top 을 재사용</br>


```kotlin
//(a) Notification 을 통해 인텐트를 실행
//1. 펜딩 인텐트 생성
private fun createPendingIntent(type: NotificationType): PendingIntent {
    //(1) 인텐트 생성 : 실행 시킬 activity 데이터가 있음
    val intentForTab = Intent(this, MainActivity::class.java).apply {
        putExtra("notificationType", "${type.title} 타입") //type.title : 일반 알림, 확장형 알림, 커스텀 알림
        addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
    }
    //(2) 펜딩 인텐트 생성
    //requestCode : 펜딩 인텐트를 가져올 때 구분하기 위한 id
    //intent : 실행시키고 싶은 인텐트
    return PendingIntent.getActivity(this, /*requestCode*/type.id, /*intent*/intentForTab, FLAG_UPDATE_CURRENT)
}

//2. notification builder 에 펜딩인텐트 추가
private fun createNotification(type: NotificationType, title: String, message: String): Notification {
    val pendingIntent = createPendingIntent(type)
    val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
         //...
        .setPriority(NotificationCompat.PRIORITY_DEFAULT) //O 버전 이하 중요도 수준 설정
        .setContentIntent(pendingIntent) //펜딩 인텐트 추가
        .setAutoCancel(true) //알림 터치시 알림 자동 삭제
    //...
    return notificationBuilder.build()
}

```

<br></br>
<br></br>
---

><a id = "ref">**참고링크**</a></br>

클라우드 메시징</br>
https://firebase.google.com/docs/cloud-messaging/android/receive</br>

서버를 구현하지 않아도 API 를 구현할 수 있는 툴</br>
https://firebase.google.com/docs/reference/fcm/rest/v1/projects.messages/send</br>

Firebase 클라우드 메시징 서비스에서 보낼 메시지(Request body 구조 언급)</br>
https://firebase.google.com/docs/reference/fcm/rest/v1/projects.messages#Message</br>

안드로이드 알림(호환성 참고)</br>
https://developer.android.com/guide/topics/ui/notifiers/notifications</br>

알림 채널 생성 및 관리</br>
https://developer.android.com/training/notify-user/channels</br>

확장타입 알림</br>
https://developer.android.com/training/notify-user/expanded</br>

커스텀타입 알림</br>
https://developer.android.com/training/notify-user/custom-notification</br>

알림 터치 작업</br>
https://developer.android.com/training/notify-user/build-notification#click</br>

코틀린 문법 : enum 클래스와 valueOf()</br>
https://gold.gitbook.io/kotlin/class/enum</br>

펜딩 인텐트</br>
https://developer.android.com/reference/android/app/PendingIntent</br>
https://lesslate.github.io/android/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-%ED%8E%9C%EB%94%A9-%EC%9D%B8%ED%85%90%ED%8A%B8(Pending-Intent)/</br>

FLAG_ACTIVITY_SINGLE_TOP</br>
https://parkho79.tistory.com/48</br>







