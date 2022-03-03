ViewPager2
-리사이클러뷰를 기반으로 사용됨
-ViewPager 를 개선한 클래스(ex. 수직 스크롤 지원 등)
-실무에서는 ViewPager 를 더 많이 사용하지만 완전히 대체될 것으로 예상
-`implementation 'com.google.android.material:material:1.5.0'` 에 viewPager2 가 포함되어 있음

Firebase remote config
-앱을 업데이트 하지 않아도 앱의 동작과 모양을 변경할 수 있는 클라우드 서비스(별도의 배포와 업데이트가 필요없음)

**정책 및 한도**
-사용자가 승인해야 하는 앱 업데이트에는 remote config 가 아닌 정식 API 를 사용해 업데이트할 것
그렇지 않으면 앱의 신뢰성을 해치며 앱 검수 시 문제가 될 수 있음
-사용하는 키는 암호화되어 있지 않기 때문에 민감한 데이터를 담아서는 안됨
-한도가 있음(2000개 매개변수, 500개 조건 등)

**사용 사례**
(a) 비율 출시 메커니즘
-새로운 기능을 일정 비율만 배포하여 사용자에게 새 기능에 천천히 노출 -> 반응을 살핌
https://firebase.google.com/docs/remote-config/use-cases#launch_new_features_with_the_percentage_rollout_mechanism

(b) 프로모션
-타이밍에 따라서 홍보 문자를 변경할 때 사용
https://firebase.google.com/docs/remote-config/use-cases#define_platform_and_locale-specific_promo_banners_for_your_app

**로딩 화면 뒤에서 활성화(자주 사용되는 로딩 전략)**
-로딩 화면(ex. Splash Screen, Loading Indicator)을 통해 먼저 Fetch 를 진행
-로드가 끝나면 Complete Handler 에서 fetchAndActivate()를 호출
-앞선 과정들이 끝나면 로딩 화면을 닫고 사용자가 상호작용을 할 수 있게 함
*해당 전략을 사용하는 경우 로딩 화면에 시간 제한을 걸어 두는 것이 좋음

///////////

remote config 공식 문서
https://firebase.google.com/docs/remote-config

remote config 로딩 전략
firebase.google.com/docs/remote-config/loading

Remote Config 사용해보기
https://doitddo.tistory.com/106

패키지 문제 해결
https://developer88.tistory.com/37

///////////
app:layout_constraintVertical_chainStyle="packed"