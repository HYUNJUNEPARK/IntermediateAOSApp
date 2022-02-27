# FCM Receiver

<img src="이미지 주소" height="400"/>

---
1. <a href = "#content1">FCM(Firebase Cloud Messaging)</a></br>
2. <a href = "#content2">Firebase 프로젝트 등록</a></br>
3. <a href = "#content3">content3</a></br>
* <a href = "#ref">참고링크</a>
---
><a id = "content1">**1. Firebase Cloud Messaging(FCM)**</a></br>


1. FCM(Firebase Cloud Messaging)</br>

-Firebase 에서 안드로이드, ISO, 웹 등으로 메시지를 보낼 수 있음</br>

(a)데이터 메시지</br>
-앱 상태가 백그라운드, 포어그라운드에 있든지 자체적으로 메시지를 처리할 수 있어 개발이 유연함</br>
-대부분 데이터 메시지 사용</br>

(b)알림 메시지</br>
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
(1)프로젝트를 생성하고 안드로이드 앱 추가 버튼 클릭</br>
(2)Android 패키지 이름에는 build.gradle - defaultConfig { applicationId "패키지 이름" } 입력</br>
간단한 알림만 수신하는 앱으로 SHA-1 입력하지 않음</br>
(3)Download google-services.json</br>
(4)Firebase SDK 추가</br>
(5)build.gradle - implementation 'com.google.firebase:firebase-messaging-ktx' 추가</br>
(6)연결확인</br>

```
private fun checkFCMToken() {
    FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
        if(task.isSuccessful) {
            Log.d("connectionCheck", "[Firebase Token] : ${task.result}")
        }
    }
}
```

(7) 테스트 메시지 보내기(앱이 백그라운드에 있을 때 확인 가능)</br>
파이어베이스 프로젝트 -> 왼쪽 배너 '참여' - Cloud Messaging -> Send your first message 에서 테스트 메시지 보낼 수 있음

<br></br>
<br></br>


><a id = "content3">**3. content3**</a></br>
```kotlin
//코드샘플
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

안드로이드 알림(호환성 참고)
https://developer.android.com/guide/topics/ui/notifiers/notifications

알림 채널 생성 및 관리
https://developer.android.com/training/notify-user/channels
확장타입 알림
https://developer.android.com/training/notify-user/expanded
커스텀타입 알림
https://developer.android.com/training/notify-user/custom-notification

////////////////////////////////////////////////////////

메시지 서비스

(1) 서비스를 만들고 AndroidManifest 의 해당 service 에 intent-filter 추가(디바이스로 넘어온 firebase message 는 해당 서비스에서 받는다는 뜻)

```xml
<intent-filter>
    <action android:name="com.google.firebase.MESSAGING_EVENT"/>
</intent-filter>
```
(2) onNewToken() 오버라이딩
-토큰은 언제든지 변경될 가능성이 있음
-실제 라이브 서비스를 운영할 때 FirebaseMessagingService 를 상속받은 클래스에 onNewToken 을 오버라이딩해 토큰이 갱신될 때마다 서버토큰도 갱신시킴

(3)지정된 대상(등록 토큰, 주제 또는 조건)에 메시지를 보내기
-https://firebase.google.com/docs/reference/fcm/rest/v1/projects.messages/send
ㄴ우측에 있는 Try this method 를 채워넣음

-parent
ㄴid 는 google-services.json 에서 "project_id" 에서 확인 가능

-Request body
ㄴtoken, topic, condition 중 하나는 반드시 추가되어야 하며 이 앱에서는 token 을 사용
ㄴmessage - data 를 추가하고 원하는 데이터를 key - value 쌍으로 입력 후 EXECUTE

(4) 메시지 데이터 확인
-오버라이딩한 onMessageReceived 에 break point 를 걸고 디버깅하면 데이터를 확인할 수 있음
```
//MyFirebaseMessagingService
override fun onMessageReceived(message: RemoteMessage) {
    super.onMessageReceived(message)
}
```


//////////
notification

알림은 업데이트가 자주 일어남으로 호환성을 자주 체크해줘야함
안드로이드 8.0 부터는 모든 알림에 채널을 할당해야함

알림의 종류
일반타입
확반타입

커스텀 타입 - 자체적인 레이아웃 만들어 사용. 단 다양한 기기에 맞춰서 알림을 지원하는 것에 어려움이 있음
res - layout 에 별도의 레이아웃 파일을 만듬
RemoteView 에서는 constraintLayout 을 지원하지 않기 때문에 LinearLayout 을 사용함
서, 맞춤 레이아웃의 텍스트와 제목에는 각각 TextAppearance_Compat_Notification 및 TextAppearance_Compat_Notification_Title과 같은 지원 라이브러리 스타일을 항상 적용해야 합니다.



/////////////////

//텍스트뷰에 아래 속성을 추가하면 복사를 할 수 있음
`android:textIsSelectable`


