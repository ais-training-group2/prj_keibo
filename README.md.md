- 데이터베이스 접근방법
1. 각 액티비티 혹은 프래그먼트에서 애플리케이션을 선언한다.
val app = requireActivity().application as MyApplication
2. 다음과 같은 방법으로 데이터베이스에 접근한다.
ex) app.db.deleteII(5)