# QuoteApp

---
1. <a href = "#content1">ViewPager2</a></br>
2. <a href = "#content2">Firebase remote config</a></br>
3. <a href = "#content3">XML</a></br>
* <a href = "#ref">참고링크</a>
---
><a id = "content1">**1. ViewPager2**</a></br>


-리사이클러뷰를 기반으로 사용됨</br>
-ViewPager 를 개선한 클래스(ex. 수직 스크롤 지원)</br>
-실무에서는 ViewPager 를 더 많이 사용하지만 완전히 대체될 것으로 예상</br>
-`implementation 'com.google.android.material:material:1.5.0'` 에 viewPager2 가 포함되어 있음</br>

<br></br>
<br></br>

><a id = "content2">**2. Firebase remote config**</a></br>


-앱을 업데이트 하지 않아도 앱의 동작과 모양을 변경할 수 있는 클라우드 서비스(별도의 배포와 업데이트가 필요없음)</br>
<br></br>
**정책 및 한도**</br>
-사용자가 승인해야 하는 앱 업데이트에는 remote config 가 아닌 정식 API 를 사용해 업데이트할 것</br>
(그렇지 않으면 앱의 신뢰성을 해치며 앱 검수 시 문제가 될 수 있음)</br>
-사용하는 키는 암호화되어 있지 않기 때문에 민감한 데이터를 담아서는 안됨</br>
-한도가 있음(2000개 매개변수, 500개 조건 등)</br>
<br></br>
**사용 사례**</br>
(a) 비율 출시 메커니즘</br>
-새로운 기능을 일정 비율만 배포하여 사용자에게 새 기능에 천천히 노출 -> 반응을 살핌</br>
https://firebase.google.com/docs/remote-config/use-cases#launch_new_features_with_the_percentage_rollout_mechanism</br>

(b) 프로모션</br>
-타이밍에 따라서 홍보 문자를 변경할 때 사용</br>
https://firebase.google.com/docs/remote-config/use-cases#define_platform_and_locale-specific_promo_banners_for_your_app</br>
<br></br>
**로딩 화면 뒤에서 활성화(자주 사용되는 로딩 전략)**</br>
-로딩 화면(ex. Splash Screen, Loading Indicator)을 통해 먼저 Fetch 를 진행</br>
-로드가 끝나면 Complete Handler 에서 fetchAndActivate()를 호출</br>
-앞선 과정들이 끝나면 로딩 화면을 닫고 사용자가 상호작용을 할 수 있게 함</br>
*해당 전략을 사용하는 경우 로딩 화면에 시간 제한을 걸어 두는 것이 좋음</br>

<br></br>
<br></br>

><a id = "content3">**3. XML**</a></br>


XML - TextView</br>
(1) chainStyle 속성 - spread (default), spread_inside, packed</br>
`app:layout_constraintVertical_chainStyle="packed"`</br>
https://zoiworld.tistory.com/488</br>

(2) maxLines 를 초과하면 끝자리(end)가 ... 으로 표시됨</br>

```xml
android:ellipsize="end"
android:maxLines="6"
```

<br></br>
<br></br>
---

><a id = "ref">**참고링크**</a></br>

remote config 공식 문서</br>
https://firebase.google.com/docs/remote-config</br>

remote config 로딩 전략</br>
https://firebase.google.com/docs/remote-config/loading</br>

Remote Config 사용해보기</br>
https://doitddo.tistory.com/106</br>

패키지 문제 해결</br>
https://developer88.tistory.com/37</br>

chainStyle 속성</br>
https://zoiworld.tistory.com/48</br>