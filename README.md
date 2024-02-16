# 프로젝트
Spring Boot Java 기반 백엔드 REST API 구축 <br/>
use: Java17, Spring Boot 3.2.2, JPA, H2, Gradle <br/>

proejct <br/>
⎮- client (Feignclient) <br/>
⎮- common <br/>   
⎮   ⌞ jwt <br/>
⎮   ⌞ Const.class (정적변수 관리) <br/>
⎮   ⌞ CustomException.class <br/>
⎮- config <br/>
⎮- controller <br/>
⎮- domain <br/>
⎮- dto <br/>
⎮   ⌞ ... <br/>
⎮   ⌞ test (테스트코드 전용 DTO) <br/>
⎮- repository <br/>
⎮- service <br/>
⎮- util <br/>
⎮- vo <br/>


# 주소 및 요구사항 구현 여부
## [1] 회원가입 - 사용자 회원가입 API
- 엔드포인트: POST `/szs/signup`
- 데이터베이스 제약조건 설정 및 민감 정보 암호화
- 특정 사용자만 회원가입이 가능하다.
 
### 구현 방법
#### 1-1. 특정 사용자만 회원가입이 가능해야 한다.
- Map 자료구조에 저장하여 특정 사용자만 회원가입이 가능하게 만들었다.(`com.szs.yongil.util.member.MemberRegisterUtil`)
  - @PostConstruct 어노테이션을 활용 해서 프로젝트 시작 시 userTableMap 변수에 특정 사용자들을 저장했다.
    - List 자료구조로 구현 했을 때 O(N)의 시간복잡도를 가지게 되면 특정 사용자가 증가함에 따라 검증로직의 시간이 점차 증가 할 수 있는 문제가 있다. <br/>
    그래서 Map 자료구조를 선택했는데 Map의 Get은 O(1)의 시간복잡도를 가지고 있기 때문에 선택했다.

#### 1-2. 회원가입 검증
- `com.szs.yongil.util.member.MemberUtil`을 활용하여 검증을 진행했다.
  - `userCheck` 메소드는 사용자 이름과 주민등록번호 조회를 통해 중복 회원가입을 체크
  - `userIdDuplicateCheck` 메소드는 Query를 통하여 사용자를 등록했다는 시나리오를 생각하여 중복 닉네임 체크를 추가
  - `userValidationCheck` 메소드는 1-1 항목을 검증하는 메소드

#### 1-3 민감정보는 암호화 된 상태로 저장해야 한다.
- 패스워드는 Spring Security에서 제공하는 암호화 기능을 사용했다.
  - `BCryptPasswordEncoder.encode()` 메서드를 활용하여 비밀번호만 암호화 진행했다.
- 주민등록번호는 AES128 대칭형 암호화 알고리즘을 사용했다.(`com.szs.yongil.config.AES128Config`)
  - 주민등록번호는 사용자 조회 시 복화하여 사용할 필요가 있었기 때문에 BCryptPasswordEncoder를 사용하지 않았다.
  - `aes128Config.encryptAes()` 메소드를 활용하여 암호화를 진행했다.
  - `aes128Config.decryptAes()` 메소드를 활용하여 복호화를 진행했다.
  - 주빈등록번호라는 민감정보인 만큼 암호학자와 해커들의 수년간 공격을 견뎌온 배경을 바탕으로 채택했다.
  - AES는 널리 사용되고 표준화되어 있어 애플리케이션에서 구현하기 쉽다는 장점이있어 채택한 또 다른 이유기도 하다.


## [2] 로그인 - 회원가입 한 사용자 로그인 API
- 엔드포인트: POST `/szs/login`
- Request에 아이디와 비밀번호를 받는다.
- JWT + Spring Security
  - 로그인 파라미터와 저장하고 있는 사용자 정보가 같다면 토큰 발급
  - Request 비밀번호와 DB 조회 한 비밀번호가 다르경우 예외 발생 
  - DB에 사용자 아이디가 없을 경우 예외 발생

### 구현방법
#### 2-1. 사용자 정보를 데이터베이스에서 조회한다.
- Request로 부터 받은 'userId'를 이용하여 `loadUserByUsername` 메소드를 통해 데이터베이스에서 사용자를 검색한다.
  - findByUserId를 통해 사용자 조회 후 사용자 정보가 없다면 `UsernameNotFoundException` 에외 발생
  
#### 2-2. 로그인 시 입력한 패스워드와 데이터베이스에 저장되어 있는 패스워드를 검증한다.
- `SecurityContextHolder`에 등록하는 과정에서 패스워드를 비교

#### 2-3. 사용자에게 토큰을 발행해준다.
- 사용자 아이디와 비밀번호가 같다면 토큰을 발행한다
  - 검증을 모두 통과 했다면 Response의 `accessToken` 통하여 JWT 발급


## [3] 스크래핑 - 스크래핑 대상 웹사이트 URL에 통신 후 필요한 데이터 DB에 저장하는 API
- 엔드포인트: POST `/szs/scrap`
- header 형식: Authorization: Bearer {Token}
- 인증 토큰을 이용하여 웹사이트와 통신한다.
  - 인증토큰은 헤더로 전달한다. 
  - body에 데이터 조회 대상인 `name`과 `regNo` 정보를 입력한다.
  - 계산에 필요한 결과 값들을 DB에 저장한다.

### 구현방법
#### 3-1. 제공되는 웹사이트와의 통신 방법
  - FeignClient를 활용하여 웹사이트와 통신했다.
    - RestTemplate은 Spring5.0 이후부터 레거시 라이브러리로 간주되기 때문에 채택하지 않았다.
    - Request에 대한 커스텀과 가독성이 좋아 FeignClient를 선택했다.
    - 단점으로는 트래픽이 급증했을 때에는 훨씩 적은 시스템 리소스를 사용하는 WebClient를 고민해봐야 한다.

#### 3-2. 스크래핑 데이터 DB 저장 방법
  - 로그인을 진행 했을 때 `SecurityContextHolder`에 저장되있는 username을 활용해 사용자 entity를 조회한다.
  - FeignClient로 통신 후 성공했을 경우 Response에서 필요한 결과 값을 DB에 저장한다.
    - 사용자 테이블과 스크래핑 테이블을 OneToOne 연관관계를 맺어 관리


## [4] 결정세액 - 결정세액 조회 API
- 엔드포인트: GET `/szs/refund`
- header 형식: Authorization: Bearer {Token}
- 인증 토큰을 이용하여 사용자의 결정세액 정보를 조회한다.
  - 인증토큰은 헤더로 전달한다.
  - 해당 사용자의 스크래핑 데이터를 DB에서 조회한다.
  - 조회 한 데이터를 계산식에 맞게 계산 후 반환한다.

### 구현방법
#### 4-1. 제공되는 웹사이트와의 통신 방법
- 로그인을 진행 했을 때 `SecurityContextHolder`에 저장되있는 username을 활용해 사용자 entity를 조회한다.
- 사용자 entity를 가지고 스크래핑 정보를 조회한다.
  - 해당 사용자의 스크래핑 정보가 존재하지 않다면 커스텀 예외인 `NOT_FOUND`를 발생한다.
- 스크래핑 데이터를 가지고 세액을 계산한다.
  - `com.szs.yongil.util.scrap.ScrapUtil`에서 계산식들을 메소드화 한다.
  - 계산식의 '%'가 변동 할 수 있기 때문에 별도로 분리하여 작성했다.
- 계산식 완료 후 Response의 변수명이 한글로 되어 있기 때문에 한글 깨짐현상이 발생 할 것을 우려해 Response header에 utf-8을 추가