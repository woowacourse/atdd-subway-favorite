## 즐겨찾기 - API 테스트 / 문서자동화

### 1단계 요구사항
- [x] 회원 관리 인수테스트 작성
    (예외 테스트)
    - [x] 이메일 중복
    - [x] 빈문자열 입력했을 때
- [x] 단위 테스트 작성하면서 프로덕션 코드 작성 (TDD - Controller Test, Service Test, Domain Test)
    - [x] 회원가입
        - [x] 이메일, 이름, 비밀번호, 비밀번호 확인 칸이 모두 채워져야 한다.
        - [x] 비밀번호, 비밀번호 확인이 같아야 한다.
        - [x] 이메일이 유일해야 한다. (unique)
    - [x] 로그인
        - [x] 로그인에 성공하면 토큰을 발급받는다.
    - [x] 마이페이지
        - [x] 발급된 토큰값을 식별해 사용자를 찾고, 그에 맞는 정보를 내려준다.
    - [x] 회원정보 수정
        - [x] 이름, 비밀번호 수정이 가능하다.
    - [x] 회원정보 삭제
        - [x] DB의 회원 정보를 삭제한다.
- [ ] side case에 대한 예외처리
    - [ ] 로그인하지 않은 사용자가 마이페이지 접근할 때 로그인창 띄우기
- [x] API 문서 작성
- [x] 페이지 연동


### 2단계 요구사항
- [x] 즐겨찾기 인수 테스트 작성
- [x] 단위 테스트 작성하면서 프로덕션 코드 작성 (TDD - Controller Test, Service Test, Domain Test)
    - [x] 자신의 정보만 수정 가능하도록 해야하며 로그인이 선행되어야 함
    - [x] 토큰의 유효성 검사와 본인 여부를 판단하는 로직 추가(interceptor, argument resolver)
    - [x] 즐겨찾기 추가
    - [x] 즐겨찾기 조회
    - [x] 즐겨찾기 삭제
- [ ] side case에 대한 예외처리 필수
- [ ] API 문서를 작성하고 문서화를 위한 테스트 작성
- [ ] 페이지 연동