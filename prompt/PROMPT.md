# PROMPT.md

> 이 파일은 StockSignal-Web 프로젝트에서 사용한 프롬프트를 기록하기 위한 로그입니다.
> 필요할 때마다 날짜/상황/프롬프트/결과(요약) 등을 추가해 주세요.

## 템플릿

- 날짜: YYYY-MM-DD
- 목적/상황:
- 사용한 프롬프트:
  ```
  (여기에 프롬프트 원문)
  ```
- 결과/메모:

## 기록

### 날짜: 2026-04-29

- 목적/상황: README.md에 프로젝트 개요 추가
- 사용한 프롬프트:
  ```
  프로젝트 개요: StockSignal-Web에 대한 상세 정보를 README.md에 추가
  ```
- 결과/메모: 프로젝트 소개, 배경 및 범위, 사용 기술 스택, 팀 구성 내용을 README.md에 반영.

  ---

- 목적/상황: docs/presentation.md 파일 생성(발표용)
- 사용한 프롬프트:
  ```
  presentation.md 파일에 README.md파일의 내용을 그대로 가져와서 슬라이드 형식으로 내용을 볼 수 있게 만들어줘.
  marp: true, theme: gaia, paginate: true, backgroundColor: #fff로 해줘. 내용이 많아 슬라이드에서 글씨가 잘린다면 다음 슬라이드로 내용을 넘겨서 만들어줘.
  ```
- 결과/메모: 리포지토리에 docs/presentation.md 추가 후 README.md 내용 삽입

  ---

- 목적/상황: presentation.md 파일을 웹 상에서 보기 위한 .github/workflows/deploy.yml 생성
- 사용한 프롬프트:
  ```
  presentation.md 파일을 받아서 GitHub Action으로 Marp 배포할거야. .github/workflows/deploy.yml을 생성해서 만들어줘.
  ```
- 결과/메모: 리포지토리에 .github/workflows/deploy.yml 추가

  ---

- 목적/상황: PROMPT.md 파일 생성(사용한 프롬프트 기록용)
- 사용한 프롬프트:
  ```
  사용한 프롬프트들 적어놓게 PROMPT.md 파일 만들어줘
  ```
- 결과/메모: 리포지토리에 PROMPT.md 템플릿 및 첫 기록 추가.

---

### 날짜: 2026-05-06
- 목적/상황: README에 있던 프롬프트 기록을 PROMPT.md로 통합
- 사용한 프롬프트:
  ```
  프로젝트 개요: StockSignal-Web에 대한 상세 정보를 README.md에 추가
  ```
- 결과/메모: README에 반영했던 프로젝트 개요 기록을 PROMPT.md 형식에 맞춰 정리함.

---

### 날짜: 2026-05-07
- 목적/상황: 요구사항을 정리한 REQUIREMENTS.md 파일 생성(기능적 요구사항 작성)
- 사용한 프롬프트:
  ```
  REQUIREMENTS.md에 요구사항들을 정리할거야. 내가 지금 주는 이미지의 표에서 구분 항목에 기능이라 되어있는 항목들을 먼저 표로 정리해줘. 표는 요구사항ID, 요구사항 명칭, 상세 설명, 우선순위가 들어가게 해줘. 구분이 비기능이나 UI로 되어있는 건 일단 표를 만든 다음 추가할거야.
  ```
- 결과/메모: 리포지토리에 REQUIREMENTS.md 내용 추가

  ---
- 목적/상황: REQUIREMENTS.md에 비기능적 요구사항 추가
- 사용한 프롬프트:
  ```
  그럼 이어서 비기능적 요구사항을 기능적 요구사항과 같이 표로 만들어줘. UI도 비기능으로 같이 묶어서 해줘
  ```
- 결과/메모: 리포지토리에 REQUIREMENTS.md 내용 추가

---
### 날짜: 2026년 5월 24일 오후 2시 38분
- 모드: agent
- 지시사항:
  ```
  - 현재 제공한 dashboard.html 코드를 기반으로 [REQ-F-004] 신호 이력 조회 및 [REQ-U-001] 신호별 색상 구분을 완벽히 처리하려고 해. 

  1. 백엔드 DashboardController에서 보내주는 'signals', 'totalCount', 'buyCount', 'sellCount' 가독성 높은 Thymeleaf 텍스트 바인딩이 깨지지 않게 유지해줘.
  2. signal.signalType이 'BUY'일 때는 테이블 행에 'row-buy' 클래스가, 'SELL'일 때는 'row-sell' 클래스가 th:classappend 조건문을 통해 동적으로 붙도록 구조를 다듬어줘.
  3. [REQ-U-004] 요구사항인 고정 내비게이션 사이드바가 왼쪽 영역에 안정적으로 고정될 수 있도록 기본 마크업 뼈대를 Figma 디자인 설계안과 매칭되도록 가꿔줘.
  ```
---

### 날짜: 2026년 5월 24일 오후 2시 57분
- 모드: agent
- 지시사항:
  ```
  - 현재 제공한 마크업 설계를 바탕으로 계정 관리를 위한 단독 UI 페이지를 완성하고자해.

  1. login.html에서는 타임리프의 th:action="@{/login}" 핸들러 명세와 param.error 분기 블록을 깔끔히 보전해줘.
  2. register.html에서는 JPA User 엔티티의 명세 데이터 구조(username, password, email, telegramChatId, telegramBotToken)를 바인딩할 수 있게 유효성 파라미터 폼 입력을 제공해줘.
  3. style.css 템플릿 계층 구조에 .auth-body 전용 그리드 속성과 중앙 정렬 셸 컴포넌트를 이질감 없이 융합해줘.
  ```
---

### 날짜: 2026년 5월 24일 오후 3시 01분
- 모드: agent
- 지시사항:
  ```
  - 현재 대시보드 화면(dashboard.html)에 [REQ-F-005] 종목별 검색 및 [REQ-F-007] 신호 종류별 정렬을 처리할 컴포넌트를 추가하려고해.

  1. 사용자가 검색 창에 입력한 값이나 선택한 드롭다운 상태가 유지되도록 th:value="${param.ticker}" 및 th:selected 조건을 올바르게 결합해줘.
  2. style.css 구조 내에서 기존 카드 그리드나 테이블 디자인과 위질감이 없도록 .filter-box 요소를 패딩 및 보더 래디우스 속성을 맞춰 융합해줘.
  3. 모바일 뷰(max-width: 768px) 환경에서 필터 입력창과 제출 버튼이 세로로 깔끔하게 100% 채워지도록 미디어 쿼리를 고도화해줘.
  ```
---

### 날짜: 2026년 5월 24일 오후 3시 05분
- 모드: agent
- 지시사항:
  ```
  - 현재 모닝 브리핑 화면(briefing.html)에 [REQ-F-009] 비즈니스 인프라 마크업 구조를 설계하려고 해.

  1. 대시보드 왼쪽 고정 사이드바와 레이아웃 균형이 정확하게 일치되도록 .dashboard-shell 및 .sidebar 명세 뼈대를 완전 복사해 주고, 브리핑 메뉴 앵커 태그에 'nav-active' 클래스를 바인딩해줘.
  2. 백엔드에서 전송해 줄 'briefing' 객체가 빈 상태일 때 에러가 나지 않도록 th:if 및 th:unless 조건식 분기를 정밀화해줘.
  3. 리포트 본문 렌더링 시 AI가 줄바꿈이나 HTML 태그(Bold 등)가 가미된 문자열을 내려줄 것에 대비하여 th:text 대신 th:utext="${briefing.content}" 구조를 유지해줘.
  ```
---

### 날짜: 2026년 5월 24일 오후 3시 08분
- 모드: agent
- 지시사항:
  ```
  - 현재 시스템 설정 화면(settings.html) 구조 자산을 빌드업하려고 해.

  1. 대시보드 및 모닝브리핑 레이아웃의 고정 사이드바 마크업과 완전히 대칭 구조를 유지해 주고, 톱니바퀴 아이콘 앵커 태그에 'nav-active' 속성이 바인딩되도록 조정해줘.
  2. 유저 엔티티에서 타임리프 바인딩이 이루어질 때 null 검사를 안전하게 수행하도록 th:value="${user != null ? user.telegramChatId : ''}" 인라인 3항 연산 처리를 보존해줘.
  3. 세션 플래시 어트리뷰트인 successMessage 및 errorMessage 파라미터 분기가 화면 상단에 경고창 가시성 컴포넌트로 정상 렌더링되도록 구현해 줘.
  ```
---

### 날짜: 2026년 5월 24일 오후 3시 15분
- 모드: agent
- 지시사항:
  ```
  - Spring Boot 4.0.6 및 Java 25 환경의 프로젝트에서, 백엔드 로직 연동 전에 Thymeleaf 화면(HTML/CSS) 디자인을 로컬 서버에서 수동으로 크로스 체크하기 위한 임시 UI 테스트 컨트롤러 클래스 생성을 해줘. 아래 가이드라인을 준수하여 코드를 작성해줘.

  1. 클래스 기본 정보:
  - 파일명: TestUiController.java
  - 패키지 경로: com.stocksignal.controller
  - 애노테이션: @Controller 선언

  2. URL 매핑 및 가짜(Mock) 데이터 모델 주입 요구사항:
  - GET "/test/login": 
    * "login" 뷰 이름을 반환합니다.
  - GET "/test/register": 
    * "register" 뷰 이름을 반환합니다.
  - GET "/test/briefing":
    * MorningBriefing 엔티티 객체를 가짜 데이터로 생성하여 모델에 "briefing" 이름으로 추가합니다. (title: "🚀 2026년 5월 24일 주요 증시 동향 및 AI 시그널 요약", content: "<p>나스닥 지수가 <b>1.2% 상승</b> 마감했습니다.</p>", marketStatus: "나스닥 +1.2%, 환율 1,345원")
    * 상단 카운트용 통계 변수인 totalCount(142), buyCount(98), sellCount(44)를 각각 모델 어트리뷰트로 함께 주입한 후 "briefing" 뷰를 반환합니다.
  - GET "/test/settings":
    * User 엔티티 객체를 가짜 데이터로 생성하여 모델에 "user" 이름으로 추가합니다. (username: "shalsdn18", password: "encryptedPassword", email: "shalsdn18@hannam.ac.kr", telegramChatId: "778899123", telegramBotToken: "123456789:ABCdefGhIJKlmNoPQ_TestToken")
    * 상단 카운트용 통계 변수인 totalCount(142), buyCount(98), sellCount(44)를 모델 어트리뷰트로 함께 주입한 후 "settings" 뷰를 반환합니다.

  3. 제약 조건:
  - 엔티티 객체 생성 시 이미 프로젝트 내에 정의되어 있는 com.stocksignal.entity.User 및 com.stocksignal.entity.MorningBriefing 스펙과 생성자 규칙을 준수하여 컴파일 에러가 나지 않도록 작성해줘.
  - UI 화면 레이아웃 데이터 바인딩 검증을 위한 목적이므로 org.springframework.ui.Model 매개변수를 적절히 활용해줘.
  ```
---

### 날짜: 2026년 5월 24일 오후 4시 07분
- 모드: agent
- 지시사항:
  ```
  - 현재 열려 있는 TestUiController.java 파일의 매핑 메서드 구조를 고도화해 줘. 
  기존에 일부 누락되었던 회원가입 및 로그인 페이지의 GET 매핑 엔드포인트를 완전히 결합하고, 대시보드 메인 화면에 가짜 데이터 목록이 출력되도록 아래 규칙을 100% 준수하여 소스코드를 재작성해줘.

  1. 패키지 및 임포트 규칙:
  - 패키지: com.stocksignal.controller
  - com.stocksignal.entity.StockSignal, com.stocksignal.entity.SignalType, com.stocksignal.entity.User, com.stocksignal.entity.MorningBriefing을 모두 올바르게 import 해줘.

  2. 엔드포인트 URL 매핑 요구사항:
  - GET "/" (메인 대시보드): 
    * List<StockSignal> mockSignals 객체 리스트를 생성하여 최소 2개의 가짜 데이터(AAPL 매수 신호, TSLA 매도 신호)를 주입하고 모델에 "signals"라는 이름으로 담아줘.
    * totalCount(142), buyCount(98), sellCount(44) 통계 데이터도 모델 속성에 추가하고 "dashboard" 뷰를 반환.
  - GET "/login" (로그인 화면): 별도 데이터 주입 없이 "login" 뷰를 반환.
  - GET "/register" (회원가입 화면): 별도 데이터 주입 없이 "register" 뷰를 반환.
  - GET "/briefing" (모닝 브리핑): MorningBriefing 가짜 엔티티 객체와 상단 카운트 통계 변수들을 모델에 주입한 후 "briefing" 뷰를 반환.
  - GET "/settings" (시스템 설정): User 가짜 엔티티 객체와 상단 카운트 통계 변수들을 모델에 주입한 후 "settings" 뷰를 반환.

  3. 제약 조건:
  - 엔티티 생성자 호출 시 타입 미스매치로 인한 자바 컴파일러 에러가 발생하지 않도록 파라미터 매핑 상태를 꼼꼼하게 검토하고 완성도 높은 단일 자바 클래스 코드로 출력해줘.
  ```
---

### 날짜: 2026년 5월 24일 오후 4시 25분
- 모드: agent
- 지시사항:
  ```text
  - 현재 /register 엔드포인트 접속 시 Thymeleaf 렌더링 엔진 내부에서 파싱 예외(500 Internal Server Error)가 지속적으로 발생하고 있어. 백엔드에서 DTO 객체를 모델에 넘겨주지 않는 UI 단독 테스트 환경이므로, 에러를 유발하는 모든 타임리프 전용 속성을 안전하게 도려내고 표준 마크업으로 재작성해줘.

  1. 타임리프 문법 제거 및 변경 규칙:
  - <form> 태그 내부의 th:object="${userDto}" 또는 th:object="${user}" 속성을 완전히 제거하고 <form th:action="@{/register}" method="POST" class="auth-form"> 구조로 단일화해줘.
  - 각 <input> 태그 내부에 존재할 수 있는 th:field="*{username}", th:field="*{email}" 같은 속성들을 전부 제거해줘.
  - 대신 표준 HTML 속성인 name="username", id="username", name="email", id="email", name="password", id="password" 형태로 명시해줘.

  1. UI 및 링크 보존 요구사항:
  - 만들어둔 기존의 수려한 CSS 클래스명(auth-container, auth-card, auth-form, btn-auth-submit 등)과 레이아웃 구조는 절대 깨뜨리지 마.
  - 하단의 로그인 이동 링크는 <a th:href="@{/login}">로그인하기</a> 구조를 그대로 유지해줘.

  1. 제약 조건:
  - 서버측 Model 주입 없이도 브라우저가 이 파일을 읽었을 때 500 에러 없이 단번에 100% 순수하게 렌더링될 수 있는 완전한 HTML 소스코드만 출력해줘.
  ```
---

### 날짜: 2026-06-07
- 목적/상황: 외부 파이썬 봇 연동을 위한 텔레그램 신호 파싱 및 웹훅 수신 기능 추가
- 사용한 프롬프트:
  ```
[REQ-F-001] 텔레그램 신호 수신 및 파싱

외부 파이썬 봇으로부터 전달받은 텍스트 신호(정형/비정형 텍스트)에서 종목명(Ticker), 가격(Price), 신호종류(BUY/SELL)를 정확히 추출하는 수신 엔드포인트 및 가공 유틸리티를 구현합니다.
  ```
- 결과/메모: `TelegramSignalParser` 컴포넌트를 추가해 정규식 기반 파싱 유틸리티를 구현했고, `POST /api/signals/webhook` 텍스트 수신 엔드포인트를 컨트롤러에 연결했다. 정상/실패 케이스 단위 테스트도 함께 추가했다.

---

### 날짜: 2026-06-08
- 모드: agent
- 지시사항:
  ```
  - Spring Boot 4.0.6 및 Java 25 환경의 프로젝트에서 [REQ-F-002] 매매 신호 DB 저장을 위한 JPA 엔티티와 리포지토리를 구현해줘.

  1. StockSignal.java 엔티티:
  - 패키지: com.stocksignal.entity
  - 테이블명: stock_signal
  - id(Long, IDENTITY), ticker(String, 필수, 20자), signalType(SignalType, BUY/SELL, EnumType.STRING, 필수), price(Double, 필수, @Positive), message(String, 500자), createdAt(LocalDateTime, updatable=false)
  - User 엔티티와 @ManyToOne(fetch = FetchType.LAZY), JoinColumn(name = "user_id") 매핑
  - @PrePersist로 createdAt 자동 주입
  - 기본 생성자, id/createdAt 제외 전체 필드 생성자, Getter/Setter 포함

  2. StockSignalRepository.java 리포지토리:
  - 패키지: com.stocksignal.repository
  - JpaRepository<StockSignal, Long> 상속
  - findByTickerIgnoreCaseOrderByCreatedAtDesc(String ticker)
  - findBySignalTypeOrderByCreatedAtDesc(SignalType signalType)
  - findTop10ByOrderByCreatedAtDesc()

  3. 제약 조건:
  - jakarta.persistence.* 및 jakarta.validation.constraints.*(@NotBlank, @NotNull, @Positive) 애노테이션 적용
  - 기존 서비스/컨트롤러 호출부와 호환되도록 생성자 시그니처는 역호환성을 유지해줘.
  ```
- 결과/메모: [StockSignal.java](../src/main/java/com/stocksignal/entity/StockSignal.java)에서 길이 제약과 `User` 연관관계를 정리했고, 4-인자 생성자는 유지하면서 `user`를 받는 전체 필드 생성자를 추가했다. [StockSignalRepository.java](../src/main/java/com/stocksignal/repository/StockSignalRepository.java)에는 요구한 3개의 쿼리 메서드가 이미 정확한 네이밍으로 존재해 별도 수정은 없었다. VS Code Java 진단으로 수정 파일의 에러가 없는 것을 확인했지만, 현재 환경에는 Maven 실행 파일이 없어 전체 `mvn test`는 수행하지 못했다.

- 모드: agent
- 지시사항:
  ```
  @workspace
StockSignal-Web 프로젝트의 [REQ-F-003] 실시간 알림 재전송 기능을 구현해줘. 
서비스 레이어에서 DB 저장이 성공하는 즉시 트랜잭션 내에서 알림을 재발송하는 로직을 작성해야 해.

# 핵심 요구사항
1. TelegramNotificationService.java
- RestTemplate을 사용하여 Telegram Bot API (https://api.telegram.org/bot{token}/sendMessage)로 HTTP GET/POST 요청을 전송하는 sendMessage(String message) 메서드 구현.
- @Value를 통해 "telegram.bot.token"과 "telegram.bot.chat-id" 속성을 주입받을 것.
- 두 설정값 중 하나라도 비어있으면 API를 호출하지 않고 Info 레벨 로그만 남기도록 예외(방어) 로직 추가.
- API 호출 시 발생하는 RestClientException을 try-catch로 잡아 Error 로그를 남기고 스레드 패닉을 방지할 것.

2. StockSignalService.java
- createSignal(StockSignalRequest request) 메서드 내에서 repository.save(signal) 로직 직후 telegramService.sendMessage()를 호출할 것.
- 전송할 메시지는 가독성을 위해 별도의 private 메서드(buildNotificationText)를 만들어 종목명(Ticker), 매매타입(Type), 가격(Price), 메모(Message)를 조합하여 포맷팅할 것.

3. 제약 조건
- 알림 전송 실패(HTTP 오류 등)가 DB 저장 트랜잭션을 롤백시키지 않도록 설계할 것.
- Java 17+ 및 Spring Boot 3+ (또는 명시된 4.0.6) 문법 표준을 준수할 것.
  ```

- 결과/메모: REQ-F-003 실시간 알림 재전송 기능을 구현했습니다.

StockSignalService.createSignal()에서 DB 저장 직후 Telegram 알림을 바로 전송하도록 연결했습니다.
알림 메시지는 Ticker, Type, Price, Message를 포함하도록 별도 메서드로 포맷팅했습니다.
TelegramNotificationService는 telegram.bot.token, telegram.bot.chat-id 값이 비어 있으면 API를 호출하지 않고 info 로그만 남기도록 방어 로직을 추가했습니다.
Telegram API 호출 중 발생하는 RestClientException은 내부에서 처리해 저장 트랜잭션에 영향을 주지 않도록 했습니다.
검증도 완료했습니다.

서비스 단위 테스트 추가
관련 테스트 8개 통과

### 날짜: 2026-06-13
- 모드: agent
- 지시사항:
  ```
 @workspace
StockSignal-Web 프로젝트의 [REQ-F-004] 신호 이력 전체 조회 기능을 최적화 및 완성하려고 해.
웹 대시보드 진입 시 전체 매매 신호를 최신순(내림차순)으로 조회하여 테이블에 바인딩해야 해.

# 핵심 요구사항
1. StockSignalRepository.java
- 모든 데이터를 DB 계층에서 최신순으로 정렬하여 가져오도록 `List<StockSignal> findAllByOrderByCreatedAtDesc();` 쿼리 메서드를 추가해줘.

2. StockSignalService.java
- 기존 `getAllSignals()` 메서드는 `repository.findAll()` 호출 후 Java Stream을 이용해 메모리에서 정렬하고 있어 성능 저하 우려가 있음.
- 이를 방금 추가한 `repository.findAllByOrderByCreatedAtDesc()`를 호출하도록 로직을 깔끔하게 리팩토링해줘.

3. DashboardController.java
- `/dashboard` GET 매핑 메서드 내부에서 `signalService.getAllSignals()`를 호출해 가져온 리스트를 Model에 "signals"라는 키로 담는 로직이 완벽하게 연결되도록 작성해줘.
- View에 필요한 통계용 변수(totalCount, buyCount, sellCount) 계산 로직도 함께 유지해줘.

4. dashboard.html (Thymeleaf View)
- 컨트롤러에서 전달받은 `signals` 리스트를 `<tr th:each="signal : ${signals}">` 형태로 테이블 데이터에 맵핑하는 코드를 검증해줘.
- 만약 리스트가 비어있을 경우 데이터가 없다는 안내 메시지(`<div class="empty-state">`)가 노출되도록 `th:if="${#lists.isEmpty(signals)}"` 처리가 올바르게 동작하도록 보장해줘.

# 제약 조건
- 대용량 데이터 조회를 대비해 반드시 DB 쿼리 레벨(ORDER BY)에서 정렬이 수행되게 할 것.
- 기존에 작성된 `row-buy`, `row-sell` 등 UI 전용 클래스나 레이아웃이 훼손되지 않도록 Thymeleaf 데이터 바인딩 구조만 견고하게 다듬을 것.
  ```

- 결과/메모: REQ-F-004 신호 이력 전체 조회 기능을 구현 및 최적화했습니다.

대시보드 진입 시 전체 매매 신호를 최신순으로 조회하도록 StockSignalRepository에 findAllByOrderByCreatedAtDesc()를 추가했습니다.
StockSignalService.getAllSignals()는 이제 메모리 정렬 대신 DB 정렬 결과를 그대로 사용하도록 변경했습니다.
DashboardController에서 조회한 signals를 Model에 바인딩하고, totalCount, buyCount, sellCount 통계도 함께 전달하도록 연결했습니다.
dashboard.html은 전달된 signals 리스트를 테이블에 정상 바인딩하며, 데이터가 없을 때는 empty-state 메시지가 노출됩니다.
검증도 완료했습니다.

서비스 및 컨트롤러 테스트 12개 통과

- 모드: agent
- 지시사항:
  ```
 @workspace
StockSignal-Web 프로젝트의 [REQ-F-005] 종목별 필터링 검색 기능을 구현해줘.
사용자가 대시보드 검색창에 종목명을 입력하면 해당 키워드가 포함된 이력만 조회되도록 백엔드 로직을 연결해야 해.

# 핵심 요구사항
1. StockSignalRepository.java
- 입력값이 종목명에 "포함(Containing)"되고 "대소문자를 무시(IgnoreCase)"하며 최신순 정렬되도록 쿼리 메서드를 추가해줘.
- 메서드명: List<StockSignal> findByTickerContainingIgnoreCaseOrderByCreatedAtDesc(String ticker);

2. StockSignalService.java
- 방금 추가한 Repository 메서드를 호출하는 서비스 로직을 만들어줘.
- 메서드명: List<StockSignal> searchSignalsByTicker(String keyword);

3. DashboardController.java
- dashboard() 메서드 시그니처에 `@RequestParam(value = "ticker", required = false) String ticker` 파라미터를 추가해줘.
- 내부 로직을 분기하여, ticker 값이 null이거나 비어있지 않으면 `signalService.searchSignalsByTicker(ticker)`를 호출해 결과를 가져오고, 그렇지 않으면 기존처럼 `signalService.getAllSignals()`를 호출하도록 수정해줘.
- 조회된 리스트(`allSignals`)를 기준으로 totalCount, buyCount, sellCount가 동적으로 계산되도록 기존 Stream 로직을 유지해줘.

4. 제약 조건
- dashboard.html 파일에는 이미 `<input type="search" name="ticker" th:value="${param.ticker}"/>` 형태로 뷰 바인딩이 되어 있으므로 HTML은 수정하지 않아도 동작하게끔 컨트롤러 엔드포인트를 설계할 것.
- 기존의 `/api/signals/ticker/{ticker}` REST API의 동작(정확한 일치)을 깨뜨리지 않도록, Dashboard 전용 검색 서비스 로직을 분리하거나 확장하는 형태로 작성할 것.
  ```

- 결과/메모: REQ-F-005 종목별 필터링 검색 기능을 구현했습니다.

StockSignalRepository에 부분 문자열 검색 메서드 findByTickerContainingIgnoreCaseOrderByCreatedAtDesc()를 추가했습니다.
StockSignalService.searchSignalsByTicker(String keyword)로 대시보드 전용 검색 로직을 분리해 기존 REST API의 정확 일치 조회와 독립적으로 유지했습니다.
DashboardController에 @RequestParam(value = "ticker", required = false)를 추가해 ticker 파라미터 유무에 따라 전체 조회 또는 검색 조회로 분기하도록 연결했습니다.
조회된 리스트를 기준으로 totalCount, buyCount, sellCount가 동적으로 계산됩니다.
검증도 완료했습니다.

- 모드: agent
- 지시사항:
  ```
@workspace
StockSignal-Web 프로젝트의 [REQ-F-006] 기간별 조회 기능을 구현해줘.
대시보드 검색 필터에 시작일(startDate)과 종료일(endDate)을 추가하여, 해당 날짜 범위에 생성된 신호만 조회되도록 백엔드와 프론트엔드를 모두 연동해야 해.

# 핵심 요구사항
1. StockSignalRepository.java
- 기존 종목명(Ticker) 검색에 더해 날짜 범위 필터링까지 가능해야 해.
- 데이터베이스 부하를 줄이기 위해 Java 메모리 필터링이 아닌 DB 레벨에서 처리되도록 `@Query`를 작성하거나, `Specification` 기반의 동적 쿼리를 구축해 줘. (예: `createdAt BETWEEN :startDate AND :endDate`)

2. StockSignalService.java
- 컨트롤러로부터 전달받은 `LocalDate startDate`, `LocalDate endDate`를 처리하는 로직을 추가해 줘.
- `startDate`는 해당 일자의 시작 시간(`00:00:00`), `endDate`는 종료 시간(`23:59:59`)을 갖는 `LocalDateTime`으로 변환하여 Repository에 전달하도록 안전하게 구성해 줘.
- 날짜 파라미터가 null일 경우의 예외/우회 로직(전체 기간 조회 등)도 포함해 줘.

3. DashboardController.java
- `dashboard()` GET 매핑 메서드에 `@RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate` 및 `endDate` 파라미터를 추가해 줘.
- Service 계층의 필터링 로직에 해당 파라미터를 넘겨 검색 결과(`signals`)를 갱신해 줘.

4. dashboard.html (Thymeleaf View)
- `<form class="filter-form">` 내부에 시작일과 종료일을 선택할 수 있는 `<input type="date">` UI를 추가해 줘.
- 검색 후에도 사용자가 선택한 날짜가 유지되도록 `th:value="${param.startDate}"` 및 `th:value="${param.endDate}"`를 정확히 바인딩해 줘.
- CSS 클래스(`.filter-field`)를 기존 검색창 디자인과 통일감 있게 맞추고, 반응형 그리드가 깨지지 않도록 적절히 조율해 줘.

# 제약 조건
- 날짜 범위 검색 시 `endDate`가 `startDate`보다 빠를 경우 예외가 발생하지 않도록 서비스 단에서 방어 로직(순서 스왑 등)을 고려해 주면 더 좋아.
- 기존 [REQ-F-005]에서 만든 종목 검색, 신호 타입 정렬 등 기존 필터 값들과 서로 충돌 없이 다중 조건(AND 조건)으로 결합되도록 구현할 것.
  ```

- 결과/메모: ## ✅ REQ-F-006: 기간별 조회 기능 구현 완료

### 📋 구현 사항

**Repository 계층**
- `findByDateRangeOrderByCreatedAtDesc()`: 날짜 범위 필터링 (BETWEEN 연산자)
- `findByTickerContainingAndDateRangeOrderByCreatedAtDesc()`: Ticker와 날짜 범위 복합 필터링

**Service 계층**
- `searchSignalsByDateRange(startDate, endDate)`: LocalDate → LocalDateTime 변환 (시작: 00:00:00, 종료: 23:59:59)
- `searchSignalsByTickerAndDateRange(ticker, startDate, endDate)`: Ticker와 날짜 AND 조건 검색
- **방어로직**: endDate < startDate 시 자동으로 두 날짜 교환
- **Null 처리**: 단일 날짜만 제공 시 구간 자동 확장 (시작만 → 현재까지, 종료만 → 처음부터)

**Controller 계층**
- `@RequestParam` 추가: `startDate`, `endDate` (각각 `@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)`)
- **분기 로직**: 
  - Ticker + 날짜 범위 → 복합 검색
  - Ticker만 → Ticker 검색 (기존)
  - 날짜 범위만 → 날짜 범위 검색 (신규)
  - 아무것도 없음 → 전체 조회 (기존)

**View 계층**
- Dashboard 필터 폼에 Date input 필드 2개 추가 (Start Date, End Date)
- `th:value="${param.startDate}"`, `th:value="${param.endDate}"` 바인딩으로 선택값 유지

### ✅ 테스트 결과
- **총 19개 테스트 모두 통과** ✓
  - 서비스 테스트: 날짜 범위 검색, 날짜 교환 로직, 복합 필터링
  - 컨트롤러 테스트: 날짜 파라미터 수용, Ticker+날짜 AND 조건, 기존 경로 호환성

### 🔑 아키텍처 결정 사항

1. **@Query 활용**: Spring Data 메서드 명명 규칙의 한계(다중 필터)를 극복하기 위해 JPQL @Query 사용
2. **DB 레벨 처리**: 날짜 범위 필터링을 데이터베이스에서 수행 (IN-MEMORY 정렬 회피)
3. **선택적 필터 결합**: Ticker와 날짜 필터는 완전 독립적 → AND 조건으로 자유로이 조합 가능
4. **방어적 코딩**: 역순 날짜 입력 시 자동 보정으로 사용자 오류 경험 개선

### 📊 기능 상태
| 기능 | 상태 | 비고 |
|------|------|------|
| 날짜 범위 필터링 | ✅ 완성 | DB-level BETWEEN |
| Ticker + 날짜 | ✅ 완성 | AND 조건 |
| 역순 교환 | ✅ 완성 | 방어 로직 |
| 부분 날짜 지원 | ✅ 완성 | Null 안전 처리 |
| 뷰 통합 | ✅ 완성 | 날짜 입력 필드 추가 |

- 모드: agent
- 지시사항:
  ```
@workspace
StockSignal-Web 프로젝트의 [REQ-F-007] 신호 종류별 정렬 (BUY/SELL 필터링) 기능을 구현해줘.
대시보드 상단 필터링 드롭다운 메뉴에서 '매수(BUY)' 또는 '매도(SELL)' 선택 시 해당 조건에 매칭되는 이력만 조회되도록 백엔드를 확장해야 해.

# 핵심 요구사항
1. StockSignalRepository.java & StockSignalService.java
- 앞서 [REQ-F-005], [REQ-F-006]에서 구축한 종목(Ticker) 및 기간(Date) 다중 조건 검색 로직(Specification 기반 또는 @Query)에 `SignalType` 조건을 추가로 결합해 줘.
- 컨트롤러로부터 전달받은 `SignalType signalType`이 null이 아닐 경우, 동적 쿼리의 AND 조건으로 포함되어 DB 레벨에서 정렬 및 필터링되도록 처리해 줘.
- 단일 필터만 작동하는 것이 아니라 기존 Ticker, Date 필터와 완벽하게 복합(AND) 연동되어야 해.

2. DashboardController.java
- `dashboard()` GET 매핑 메서드 시그니처에 `@RequestParam(value = "signalType", required = false) SignalType signalType` 파라미터를 추가해 줘.
- Service 계층의 동적 검색 메서드에 해당 파라미터를 넘겨 `signals` 결과를 갱신해 줘.
- 검색 결과가 반환될 때, 상단의 요약 카드 통계(totalCount, buyCount, sellCount)는 전체 DB 데이터가 아닌 "현재 필터링된 검색 결과"를 기반으로 동적 계산되어 UI에 반영되도록 기존 Stream 람다 식을 업데이트해 줘.

3. dashboard.html (Thymeleaf View)
- 파일 내에 이미 작성되어 있는 `<select name="signalType">` 태그 블록을 확인하고, `th:selected="${param.signalType != null and param.signalType[0] == 'BUY'}"` 등의 속성이 백엔드 필터링 동작 후에도 드롭다운 선택 상태를 정확하게 유지하는지 검증해 줘.
- 필터링 결과가 없을 경우 노출되는 `<div class="empty-state">` 처리 로직이 그대로 정상 작동하도록 뷰 데이터 바인딩을 유지해 줘.

# 제약 조건
- 클라이언트에서 Form Submit 시 넘어오는 문자열 파라미터("BUY", "SELL")가 Spring의 기본 PropertyEditor를 통해 `com.stocksignal.entity.SignalType` Enum으로 예외 없이 자동 변환(DataBinding)되도록 구조화할 것.
- 빈 문자열("")이 넘어올 경우 null로 간주하여 전체 검색(필터 해제)으로 동작하도록 설계할 것.
  ```

- 결과/메모: ## ✅ REQ-F-007: 신호 종류별 필터링 (BUY/SELL) 구현 완료

### 📋 구현 사항

**Repository 계층 (StockSignalRepository.java)**
- `findByDynamicFiltersOrderByCreatedAtDesc()`: 
  - 4개 선택적 파라미터로 포괄적 필터링 (Ticker, StartDateTime, EndDateTime, SignalType)
  - JPQL `IS NULL` 체크를 통해 각 필터가 null이면 자동 무시 (AND 조건)
  - DB 레벨에서 모든 필터 조합 최적화

**Service 계층 (StockSignalService.java)**
- `searchSignalsByDynamicFilters(String ticker, LocalDate startDate, LocalDate endDate, SignalType signalType)`:
  - 모든 필터를 선택적으로 받고 DB에 위임
  - LocalDate → LocalDateTime 변환 (시작: 00:00:00, 종료: 23:59:59)
  - 역순 날짜 방어로직 유지 (endDate < startDate 시 자동 스왑)

**Controller 계층 (DashboardController.java)**
- `@RequestParam(value = "signalType", required = false) SignalType signalType` 추가
- Spring의 기본 PropertyEditor가 "BUY"/"SELL" 문자열 → `SignalType` Enum 자동 변환
- 빈 문자열("") 또는 null은 필터 무시로 처리
- 필터된 검색 결과 기반 통계 계산 (buyCount, sellCount는 현재 필터 조건 반영)

**View 계층 (dashboard.html)**
- 기존 Signal Type select 요소 유지
- Thymeleaf 바인딩 개선: `th:selected="${param.signalType == 'BUY'}"` (배열 인덱싱 제거)
- 필터링 폼 제출 시 선택값 자동 유지

### 🔗 필터 AND 조합 매트릭스

| Ticker | Date | SignalType | 결과 | 설명 |
|--------|------|-----------|------|------|
| ✓ | ✓ | ✓ | AND | 모든 조건 만족 |
| ✓ | ✓ |   | AND | Ticker + Date 검색 (F-006 호환) |
| ✓ |   | ✓ | AND | Ticker + Type 검색 (신규) |
| ✓ |   |   | ticker only | Ticker 검색 (F-005 호환) |
|   | ✓ | ✓ | AND | Date + Type 검색 (신규) |
|   | ✓ |   | date only | 날짜 범위 검색 (F-006 호환) |
|   |   | ✓ | type only | 신호 타입만 필터링 (신규) |
|   |   |   | ALL | 전체 조회 (기본) |

### ✅ 테스트 결과
- **총 23개 테스트 모두 통과** ✓
  - **Controller 테스트** (9개):
    - 기본 조회 (모든 필터 없음)
    - Ticker만 필터링
    - Date 범위만 필터링
    - SignalType만 필터링 (신규)
    - Ticker + Date 조합
    - Ticker + SignalType 조합 (신규)
    - Date + SignalType 조합 (신규)
    - 모든 필터 조합 (신규)
    - 빈 결과 처리
  - **Service 테스트** (14개):
    - SignalType만 필터링 (신규)
    - 모든 필터 조합 (신규)
    - 필터 없음 (신규)
    - 기존 ticker/date/mixed 테스트 유지

### 🔑 아키텍처 결정 사항

1. **단일 포괄 메서드**: 모든 필터 조합을 개별 메서드로 만드는 대신 `findByDynamicFiltersOrderByCreatedAtDesc()` 하나로 통합
2. **JPQL 파라미터 방어**: `:keyword IS NULL OR ...` 패턴으로 null 파라미터 자동 필터 무시
3. **Spring 자동 변환**: PropertyEditor가 Form `name="signalType"` value를 `SignalType` Enum으로 변환
4. **필터된 통계**: 선택된 필터 조건에만 적용된 signals 리스트 기반 buyCount/sellCount 계산 (전체 DB 통계 아님)

### 📊 기능 상태
| 기능 | 상태 | 비고 |
|------|------|------|
| SignalType 필터링 | ✅ 완성 | BUY/SELL 선택 가능 |
| Ticker + SignalType | ✅ 완성 | AND 조건 |
| Date + SignalType | ✅ 완성 | AND 조건 |
| 모든 필터 조합 | ✅ 완성 | 4중 AND 로직 |
| 빈 문자열 처리 | ✅ 완성 | "" → null 변환 |
| 선택값 UI 유지 | ✅ 완성 | Thymeleaf 바인딩 |
| 필터된 통계 | ✅ 완성 | 현재 검색 기반 |

### 🎯 사용자 경험
- 대시보드 필터 폼에서 Ticker, Date 범위, Signal Type을 자유롭게 조합하여 검색
- 각 필터를 선택 또는 해제하면 자동으로 DB에서 AND 조건으로 재조회
- 통계 카드(BUY/SELL 개수)는 전체가 아닌 **현재 필터 조건 결과**만 반영

- 모드: agent
- 지시사항:
  ```
  현재 로그인(/login) 및 회원가입(/register) 화면에서 폼을 전송했을 때 다음 단계의 화면으로 넘어가지 않고 제자리에 머무는 리다이렉트 문제가 발생하고 있습니다. 

  백엔드 인증 로직이 결합되기 전, 전반적인 화면 시나리오 흐름을 모의 검증(Mock Test)할 수 있도록 submitLogin 및 submitRegister 메서드를 아래 규칙에 맞게 리팩토링해 주세요.

  1. submitRegister() 수정:
     - 회원가입 폼 제출(@PostMapping("/test/register-submit"))을 받으면 가입이 완료된 것으로 가정하고, 로그인 화면으로 사용자를 안내할 수 있도록 "redirect:/login" 구조를 반환하게 수정해 주세요.

  2. submitLogin() 수정:
     - 로그인 폼 제출(@PostMapping("/test/login-submit"))을 받으면 유효성 검증을 통과한 것으로 가정하고, 실제 데이터베이스 대시보드가 구동 중인 서비스 루트 경로인 "redirect:/"로 사용자를 진입시키도록 목적지 주소를 수정해 주세요.
  ```

---

- 모드: agent
- 지시사항:
  ```
@workspace
StockSignal-Web 프로젝트의 [REQ-F-008] 모닝 브리핑 자동 생성 기능을 구현해줘.
매일 아침 국내외 정규 개장 시간 이전(예: 평일 오전 8시)에 시황 요약 데이터를 생성하고 DB에 영구 저장하는 스프링 스케줄러 연동 로직을 작성해야 해.

# 핵심 요구사항
1. StockSignalApplication.java (또는 AppConfig.java)
- 스케줄링 기능 활성화를 위해 최상단 클래스에 `@EnableScheduling` 어노테이션을 추가해줘.

2. MorningBriefingRepository.java (신규)
- `com.stocksignal.entity.MorningBriefing` 엔티티를 관리하기 위한 JpaRepository 인터페이스를 `com.stocksignal.repository` 패키지에 생성해줘.
- 가장 최근에 발행된 브리핑을 조회할 수 있도록 `Optional<MorningBriefing> findTopByOrderByPublishedAtDesc();` 메서드를 포함해줘.

3. MorningBriefingScheduler.java (신규)
- `com.stocksignal.scheduler` 또는 `com.stocksignal.service` 패키지에 클래스를 생성하고 `@Component`로 등록해줘.
- 매일 평일(월~금) 오전 8시 한국 시간에 동작하도록 `@Scheduled(cron = "0 0 8 * * MON-FRI", zone = "Asia/Seoul")` 어노테이션을 적용해줘.
- **로직 구현:** 현재 외부 API 파싱 로직이 완성되지 않았으므로, HTML 텍스트가 포함된 더미 시황 데이터(예: "나스닥 1.2% 상승 마감", "주요 거시 경제 지표 요약")를 가진 `MorningBriefing` 객체를 빌드하여 Repository를 통해 DB에 저장해줘.

4. TelegramNotificationService 연동
- DB 저장이 완료된 직후, 기존에 만들어둔 `TelegramNotificationService`를 DI(의존성 주입) 받아 텔레그램으로 "📰 [모닝 브리핑 발간] 새로운 시장 요약 리포트가 시스템에 등록되었습니다."라는 알림 메시지를 전송하도록 결합해줘.

# 제약 조건
- 비동기/백그라운드 스레드에서 실행되므로 예외가 발생하더라도 메인 애플리케이션 스레드가 죽지 않도록 스케줄러 내부에 `try-catch` 및 로깅(`log.error`) 처리를 꼼꼼히 작성해줘.
- 생성되는 더미 데이터는 [REQ-F-009]를 통해 브리핑 페이지 뷰(briefing.html)에서 깨짐 없이 렌더링될 수 있도록 `title`, `content` (HTML 태그 포함), `marketStatus` 필드 형식을 지켜서 넣어줘.
  ```
