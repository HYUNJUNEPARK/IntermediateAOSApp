

- 1. Firebase Cloud Messaging(FCM)

https://firebase.google.com/docs/cloud-messaging?authuser=2

파이어베이스 프로젝트의 이해
https://firebase.google.com/docs/projects/learn-more

하나의 사용자 관점에서 동일한 앱은 하나의 프로젝트로 독립해서 만든다



안드로이드 , ISO 웹 등에 메세지를 보낼 수 있음

데이터메세지
앱이 백그라운드, 포어그라운드에 있던 자체적으로 메세지를 처리하기 때문에 개발이 유연하다
대부분의 경우에 사용

알림메세지
구현이 쉽다
여러가지 케이스에 유연하게 대응하기 어렵다
앱이 백그라운드에서 실행될 때만 메세지를 수신할 수 있음
ㄴ onMessageReceived 를 호출하지 않음. 알아서 notification 을 만들어서 보여줌


1. 프로젝트를 생성하고 안드로이드 앱 추가 버튼 클릭
2. Android 패키지 이름에는 build.gradle 의
defaultConfig { applicationId "패키지 이름" } 을 입력

간단한 알림만 수신할 거기에 SHA-1 입력하지 않음

3. Download google-services.json
4. Firebase SDK 추가
5.
implementation 'com.google.firebase:firebase-messaging-ktx'

연결확인
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            task ->
            if(task.isSuccessful) {
                binding.firebaseTokenTextView.text = task.result
            }
        }


파이어베이스 프로젝트에 들어가 왼쪽에 Cloud Messaging 에 확인한 토큰을 복사해 테스트 메세지를 보낼 수 있음


-----
- 2. Notification





<TextView
    android:textIsSelectable />
텍스트 뷰에 있는 text 를 복사할수 있음




Firebase 토큰을 확인할 수 있다
일반, 확장형, 커스텀 알림을 볼 수 있다.
-normal
-custom
-extension

///////////////

토큰은 언제든지 변경될 가능성이 있음
실제 라이브 서비스를 운영할때는 FirebaseMessagingService를 상속받은 클래스에 onNewToken 을 오버라이딩해
토큰이 갱신될 때마다 서버에다가 토큰도 갱신시킴


```kotlin
서비스를 만들고 인텐트 필터 추가
            <intent-filter>
                <action android:name="com.google.firebase.MESSAGING_EVENT"/>
            </intent-filter>
```



///////
데이터 메시지


https://firebase.google.com/docs/reference/fcm/rest/v1/projects.messages/send
여기서 우측에 있는 Try this method 를 채워넣음
parent 에 들어갈 id 는 google-services.json 에서 "project_id" 에서 확인 가능



바디에 채울 메시지 구조
https://firebase.google.com/docs/reference/fcm/rest/v1/projects.messages#Message
```




```



////////////////
클라우드 메시징
https://firebase.google.com/docs/cloud-messaging/android/receive


서버를 구현하지 않아도 API 를 구현할 수 있는 툴
https://firebase.google.com/docs/reference/fcm/rest/v1/projects.messages/send