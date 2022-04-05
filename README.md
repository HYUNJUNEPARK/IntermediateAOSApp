1. Firebase Authentication

Email SignIn / SignUp
-`implementation 'com.google.firebase:firebase-auth-ktx'`

Facebook Login
-credential : 보통 사용자 이름과 비밀번호의 조합이지만 보안 인증 툴에 의해 더 많은 요소가 추가될 수 있음

```kotlin
val credential = FacebookAuthProvider.getCredential(result.accessToken.token)
auth.signInWithCredential(credential)
```

2. Firebase Realtime Database

`implementation 'com.google.firebase:firebase-database-ktx'`
-Realtime Database 시작을 따로하지 않으면 google-services.json 에 Realtime Database 권한이 포함되어 있지 않음
->프로젝트 설정으로 다시 들어가 google-services.json 을 받고 갱신
-JSON 타입으로 데이터를 저장

```
//DB 데이터 업데이트
val userId = auth.currentUser!!.uid
val currentUserDB = Firebase.database.reference.child("Users").child(userId)
val user = mutableMapOf<String, Any>()
user["userId"] = userId
user["name"] = name
currentUserDB.updateChildren(user)
```

```
//DB 구조
reference
ㄴ Users
    ㄴ currentUser(EAfewfcgwwdv3234434t542234sxvacv)
        ㄴ name
        ㄴ userId : EAfewfcgwwdv3234434t542234sxvacv
    ㄴ currentUser(E12315ddvaaDSASwdv3234434tsxvacv)
        ㄴ name
        ㄴ userId : E12315ddvaaDSASwdv3234434tsxvacv
    ...
```




yuyakaido / CardStackView(Opensource Library)



