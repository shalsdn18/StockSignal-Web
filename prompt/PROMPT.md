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

---

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

### 날짜: 2026-06-07
- 목적/상황: 외부 파이썬 봇 연동을 위한 텔레그램 신호 파싱 및 웹훅 수신 기능 추가
- 사용한 프롬프트:
  ```
[REQ-F-001] 텔레그램 신호 수신 및 파싱

외부 파이썬 봇으로부터 전달받은 텍스트 신호(정형/비정형 텍스트)에서 종목명(Ticker), 가격(Price), 신호종류(BUY/SELL)를 정확히 추출하는 수신 엔드포인트 및 가공 유틸리티를 구현합니다.
  ```
- 결과/메모: `TelegramSignalParser` 컴포넌트를 추가해 정규식 기반 파싱 유틸리티를 구현했고, `POST /api/signals/webhook` 텍스트 수신 엔드포인트를 컨트롤러에 연결했다. 정상/실패 케이스 단위 테스트도 함께 추가했다.

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
  - Spring Boot 4.0.6 및 Java 25 환경의 프로젝트에서 데이터베이스 설정을 고도화하기 위한 JPA 엔티티 클래스 생성을 해줘. 아래 요구사항과 가이드라인을 엄격히 준수하여 코드를 작성해줘.

  1. 역할 및 목표: 
  - [REQ-F-002] 매매 신호 DB 저장 및 [REQ-NF-001] 데이터 무결성 보장을 위해 엔티티 구조를 설계.
  - 패키지 경로: com.stocksignal.entity

  1. 신규 엔티티 생성 요구사항:
  - User.java: id(IDENTITY), username(필수, unique, 50자), password(필수, BCrypt 암호화 해시 저장용 100자), email(이메일 형식, 100자), telegramChatId(50자), telegramBotToken(100자), createdAt(생성 시 현재 시간 자동 주입) 필드를 포함.
  - SignalMemo.java: id(IDENTITY), memoContent(필수, 1000자), updatedAt(수정 시 현재 시간 자동 갱신) 필드를 포함하며, StockSignal 엔티티 및 User 엔티티와 각각 지연 로딩(FetchType.LAZY) 방식의 다대일(@ManyToOne) 연관 관계를 맺음.
  - MorningBriefing.java: id(IDENTITY), title(필수, 255자), content(필수, TEXT 타입 정의), marketStatus(500자), publishedAt(생성 시 현재 시간 자동 주입) 필드를 포함.

  1. 기존 엔티티 수정 사항:
  - StockSignal.java 엔티티 하단에 유저별 귀속 관리를 위해 User 엔티티와의 지연 로딩(FetchType.LAZY) 방식 다대일(@ManyToOne) 연관 관계 필드(private User user;)와 그에 따른 Getter/Setter를 추가해줘.

  모든 엔티티 클래스에는 자카르타 엔티티 애노테이션(jakarta.persistence.*) 및 유효성 검증 라이브러리(jakarta.validation.constraints.*)를 적절히 사용해 주고, 기본 생성자와 필요한 전체 필드 생성자, Getters/Setters를 빠짐없이 구현해줘.
  ```
---
### 날짜: 2026년 5월 24일 오후 2시 48분
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
  ```
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






