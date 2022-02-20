

- 1. Firebase Cloud Messaging(FCM)

https://firebase.google.com/docs/cloud-messaging?authuser=2

파이어베이스 프로젝트의 이해
https://firebase.google.com/docs/projects/learn-more

하나의 사용자 관점에서 동일한 앱은 하나의 프로젝트로 독립해서 만든다




안드로이드 , ISO 웹 등에 메세지를 보낼 수 있음

데이터메세지
앱이 백그라운드, 포어그라운드에 있던 자체적으로 메세지를 처리하기 떄문에 개발이 유연하다
대부분의 경우에 사용

알림메세지
구현이 쉽다
여러가지 케이스에 유연하게 대응하기 어렵다
애널리틱스 기반의 A/B 테스팅을 제공하여 마케팅 메세지를 수정하고 개선하는데 도움이 됨

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