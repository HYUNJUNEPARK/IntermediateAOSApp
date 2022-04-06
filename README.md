# Tinder

<img src="https://github.com/HYUNJUNEPARK/ImageRepository/blob/master/IntermediateApp/Tinder.png" height="400"/>

---
1. <a href = "#content1">Firebase Authentication</a></br>
2. <a href = "#content2">Firebase Realtime Database</a></br>
3. <a href = "#content3">Firebase Database 데이터 읽기 3가지 방법</a></br>
* <a href = "#ref">참고링크</a>
---
><a id = "content1">**1. Firebase Authentication**</a></br>

Email SignIn / SignUp</br>
-`implementation 'com.google.firebase:firebase-auth-ktx'`</br>

Facebook Login</br>
-credential : 보통 사용자 이름과 비밀번호의 조합이지만 보안 인증 툴에 의해 더 많은 요소가 추가될 수 있음</br>

```kotlin
val credential = FacebookAuthProvider.getCredential(result.accessToken.token)
auth.signInWithCredential(credential)
```

<br></br>
<br></br>

><a id = "content2">**2. Firebase Realtime Database**</a></br>

`implementation 'com.google.firebase:firebase-database-ktx'`</br>
-Realtime Database 시작을 따로하지 않으면 google-services.json 에 Realtime Database 권한이 포함되어 있지 않음</br>
->프로젝트 설정으로 다시 들어가 google-services.json 을 받고 갱신</br>
-JSON 타입으로 데이터를 저장</br>

```
//DB 데이터 업데이트
val userId = auth.currentUser!!.uid
val currentUserDB = Firebase.database.reference.child("Users").child(userId)
val user = mutableMapOf<String, Any>()
user["userId"] = userId
user["name"] = name
currentUserDB.updateChildren(user)
```
<br></br>
```
//DB 구조
reference
ㄴ Users
    ㄴ currentUser(EAfewfcgwwdv3234434t542234sxvacv)
        ㄴ likedBy
            ㄴ 'like' or 'diskLike' or 'match'
                ㄴ E12315ddvaaDSASwdv3234434tsxvacv
        ㄴ name
        ㄴ userId : EAfewfcgwwdv3234434t542234sxvacv
    ㄴ currentUser(E12315ddvaaDSASwdv3234434tsxvacv)
        ㄴ likedBy
            ㄴ 'like' or 'diskLike' or 'match'
                ㄴ E12315ddvaaDSASwdv3234434tsxvacv
        ㄴ name
        ㄴ userId : E12315ddvaaDSASwdv3234434tsxvacv
    ...
```

<br></br>
<br></br>

><a id = "content3">**3. Firebase Database 데이터 읽기 3가지 방법**</a></br>


**(1) addValueEventListener() 메소드를 이용하여 DatabaseReference 에 ValueEventListener 를 추가**
-경로의 전체 내용에 대한 변경 사항을 읽고 수신 대기
<br></br>

**(2) addListenerForSingleValueEvent() 메소드를 이용하여 DatabaseReference 에 ValueEventListener 를 추가**
-한 번만 호출되고 즉시 삭제되는 콜백이 필요한 경우에 사용
-한 번 로드된 후 자주 변경되지 않거나 능동적으로 수신 대기할 필요가 없는 데이터에 유용
-이 메소드는 한번 호출된 후 다시 호출되지 않는다
ex) 사용자의 프로필을 로드와 같은 이후에 변경되지 않는 UI 요소를 초기화할 때

```kotlin
    private fun getUserByKey(userId: String) {
        val listener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val name = snapshot.child(NAME).value.toString()
                cardItems.add(CardItem(userId, name))
                adapter.submitList(cardItems)
            }
            override fun onCancelled(error: DatabaseError) { }
        }
        usersDB.child(userId).addListenerForSingleValueEvent(listener)
    }
```
<br></br>

**(3) addChildEventListener() 메소드를 이용하여 DatabaseReference 에 ChildEventListener 를 추가**
-하위 이벤트를 수신 대기
-데이터베이스의 특정한 노드에 대한 변경을 수신 대기하는데 유용

```kotlin
    private fun getMatchUsers() {
        val matchedDB = usersDB.child(getCurrentUserID()).child(LIKED_BY).child(MATCH)
        val listener = object: ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val userId = snapshot.key
                if (userId?.isNotEmpty() == true) {
                    getUserByKey(userId.orEmpty())
                }
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) { }
            override fun onChildRemoved(snapshot: DataSnapshot) { }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) { }
            override fun onCancelled(error: DatabaseError) { }
        }
        matchedDB.addChildEventListener(listener)
    }
```
<br></br>

**DataSnapshot**</br>
-수정불가 데이터베이스 데이터 복사본</br>
-데이터베이스의 데이터를 읽을 때마다 데이터스냅샷을 데이터로 받음</br>
-on(), once() 가 붙은 데이터 콜백메서드로 전달됨</br>

<br></br>
<br></br>
---

><a id = "ref">**참고링크**</a></br>

DiffUtil</br>
https://velog.io/@haero_kim/Android-DiffUtil-%EC%82%AC%EC%9A%A9%EB%B2%95-%EC%95%8C%EC%95%84%EB%B3%B4%EA%B8%B0</br>

Firebase Database 데이터 읽기 3가지 방법</br>
https://stack07142.tistory.com/282</br>

yuyakaido / CardStackView(Opensource Library)</br>
https://github.com/yuyakaido/CardStackView</br>
