Firebase Authentication
-Email Login / SignUp
auth.signInWithEmailAndPassword(email, password)
auth.createUserWithEmailAndPassword(email, password)

-Facebook Login
val credential = FacebookAuthProvider.getCredential(result.accessToken.token)
auth.signInWithCredential(credential)
credential : 보통 사용자 이름과 비밀번호의 조합이지만 보안 인증 툴에 의해 더 많은 요소가 추가될 수 있음





///////////


implementation 'com.google.firebase:firebase-auth-ktx' 추가
->Firebase class 사용 가능




Firebase Realtime Database

Realtime Database 시작을 따로하지 않으면 google-services.json 에 Realtime Database 권한이 포함되어 있지 않음
-> 프로젝트 설정으로 다시 들어가 google-services.json 을 받고 갱신







yuyakaido / CardStackView(Opensource Library)



////////
버튼 속성 `android:enabled="false"` 으로 비활성화 가능


