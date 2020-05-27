## 1. 회원 관리
- [x] email, name, password로 회원가입
- [x] 로그인 (email, password)
- [x] 로그아웃
- [x] 로그인 성공시 Search 페이지로 이동
- [x] my page 연동
- [x] 수정하기 버튼 누르면 edit page로 이동
- [x] 저장하기 버튼 누르면 db에 수정 내역 반영
- [x] 탈퇴하기 버튼 누르면 회원 정보 삭제

## 2. 즐겨찾기
- [x] 인수테스트 작성
- [x] 즐겨찾기 추가 기능 
- [x] 즐겨찾기 조회 기능
- [x] 즐겨찾기 삭제 기능
- [x] 즐겨찾기 페이지 연동
- [ ] 회원 탈퇴 시 관련 즐겨찾기 모두 삭제

## 3. 사이드 케이스 처리
- [x] 백엔드 Custom Exception 정의 
    - [x] 경로 조회 시 출발역과 도착역이 같은 경우 : DuplicatedStationException
    - [x] 경로 조회 시 출발역 또는 도착역이 존재 하지 않는 역일 경우 : NotExistedStationException 
    - [x] 경로 조회 시 경로가 존재하지 않는 경우 : NotExistedPathException
    - [x] 이미 등록된 이메일로 회원가입을 요청할 경우 : ExistedEmailException
    - [x] 등록되지 않은 이메일로 로그인을 요청을 보낼 경우 : NotExistedEmailException
    - [x] 패스워드가 일치 하지 않는 경우 : WrongPasswordException
    - [x] 토큰이 유효하지 않은 경우 : InvalidAuthenticationException
    - [x] 이미 해당 유저에 등록된 즐겨찾기인 경우 : ExistedFavoritesException
    - [ ] 로그인 안된 상태에서 즐겨찾기 추가 요청을 보낸 경우 : LoginRequiredException
- [x] Controller Advice로 예외 핸들링 및 컨트롤러 테스트 추가
    - [x] 나머지 로직 예외는 500 에러로 처리
- [ ] 프론트 엔드 유효성 검증
    - [ ] form이 다 채워지지 않은 채로 요청을 보내는 경우, 백엔드에서도 Dto로 검증 필요
    - [ ] 회원 가입시 비밀 번호와 비밀번호 확인이 서로 일치 하지 않는 경우
- [ ] 프론트에서 예외 메시지 처리

## 4. API 문서 자동화
- [x] Line API 문서
- [x] Station API 문서
- [x] Member API 문서
- [x] Favorite API 문서