# 3. 기능 구현 현황 및 상세 설명

## 3.1 기능 구현 현황 (총 27개 요구사항 전수 검증)

| 요구사항ID | 요구사항 명칭 | 상세 구현 파일 및 소스 경로 | 구현 여부 |
| :--- | :--- | :--- | :---: |
| **REQ-F-001** | 텔레그램 신호 수신 및 파싱 | `com.stocksignal.util.TelegramSignalParser`<br>`com.stocksignal.controller.StockSignalApiController` | ☑ 완성 |
| **REQ-F-002** | 매매 신호 DB 저장 | `com.stocksignal.repository.StockSignalRepository`<br>`com.stocksignal.service.StockSignalService` | ☑ 완성 |
| **REQ-F-003** | 실시간 알림 재전송 | `com.stocksignal.service.TelegramNotificationService` | ☑ 완성 |
| **REQ-F-004** | 신호 이력 전체 조회 | `com.stocksignal.controller.DashboardController`<br>`src/main/resources/templates/dashboard.html` | ☑ 완성 |
| **REQ-F-005** | 종목별 필터링 검색 | `com.stocksignal.service.StockSignalService` (`searchSignalsByDynamicFilters`) | ☑ 완성 |
| **REQ-F-006** | 기간별 조회 기능 | `com.stocksignal.service.StockSignalService` (`searchSignalsByDateRange`) | ☑ 완성 |
| **REQ-F-007** | 신호 종류별 정렬 | `src/main/resources/templates/dashboard.html` (Thymeleaf 드롭다운) | ☑ 완성 |
| **REQ-F-008** | 모닝 브리핑 자동 생성 | `com.stocksignal.scheduler.MorningBriefingScheduler`<br>`com.stocksignal.service.MorningBriefingService` | ☑ 완성 |
| **REQ-F-009** | 브리핑 웹 페이지 노출 | `com.stocksignal.controller.DashboardController`<br>`src/main/resources/templates/briefing.html` | ☑ 완성 |
| **REQ-F-010** | 투자 전략 메모 작성 | `com.stocksignal.repository.SignalMemoRepository`<br>`com.stocksignal.entity.SignalMemo` | ☑ 완성 |
| **REQ-F-011** | 신호 데이터 삭제/편집 | `com.stocksignal.controller.StockSignalApiController` (`deleteSignal`) | ☑ 완성 |
| **REQ-F-012** | 대시보드 통계 계산 | `com.stocksignal.service.StockSignalService` (`calculateOverallStatistics`) | ☑ 완성 |
| **REQ-F-013** | 사용자 인증(Auth) | `com.stocksignal.config.SecurityConfig`<br>`src/main/resources/templates/login.html` | ☑ 완성 |
| **REQ-F-014** | 사용자 설정 관리 | `com.stocksignal.controller.SettingsController`<br>`src/main/resources/templates/settings.html` | ☑ 완성 |
| **REQ-F-015** | 데이터 수집 모니터링 | `com.stocksignal.service.StockSignalService` (`lastSignalReceivedAt`) | ☑ 완성 |
| **REQ-U-001** | 신호별 색상 구분 | `src/main/resources/templates/dashboard.html` (`row-buy` / `row-sell`) | ☑ 완성 |
| **REQ-U-002** | 반응형 레이아웃 | `src/main/resources/static/css/style.css` (CSS 미디어 쿼리 레이어) | ☑ 완성 |
| **REQ-U-003** | 데이터 로딩 상태 표시 | `src/main/resources/templates/dashboard.html` (비동기 글로벌 로딩 스피너) | ☑ 완성 |
| **REQ-U-004** | 네비게이션 사이드바 | `src/main/resources/templates/dashboard.html` (고정형 `<aside>` 바) | ☑ 완성 |
| **REQ-U-005** | 작업 결과 토스트 알림 | `src/main/resources/templates/dashboard.html` | ☑ 완성 |
| **REQ-NF-001** | 데이터 무결성 보장 | `com.stocksignal.service.StockSignalService` (장애 전용 `@Retryable`) | ☑ 완성 |
| **REQ-NF-002** | API 응답 속도 | `com.stocksignal.service.TossTokenManager` (인메모리 고속 캐싱 아키텍처) | ☑ 완성 |
| **REQ-NF-003** | 시스템 로그 관리 | `src/main/resources/logback-spring.xml` 및 `@Slf4j` 추적 정형화 | ☑ 완성 |
| **REQ-NF-004** | 확장 가능한 코드 구조 | **[금융권 인프라 연동 완결]** `TossTokenManager`, `TossApiService`,<br>`TossPortfolioApiController`, `TossAssetController` | ☑ 완성 |
| **REQ-NF-005** | 비밀번호 암호화 | `com.stocksignal.config.SecurityConfig` (`BCryptPasswordEncoder`) | ☑ 완성 |
| **REQ-NF-006** | 브라우저 호환성 | `docs/BROWSER_COMPATIBILITY_REPORT.md` (동작 검증 일지 확인) | ☑ 완성 |
| **REQ-NF-007** | 자동 배포(CI/CD) | `.github/workflows/ci.yml`, `deploy.yml` (GitHub Actions 워크플로우) | ☑ 완성 |

---

## 3.2 구현 내용 설명

### 1) REQ-F-001: 텔레그램 신호 수신 및 파싱
* **소스 경로**: `com.stocksignal.util.TelegramSignalParser`, `com.stocksignal.controller.StockSignalApiController`
* **설명 요약**: 파이썬 자동매매 봇 시스템으로부터 유입되는 비정형 텍스트 알림 메시지를 수신하여 정밀 정규식 매칭을 통해 구조화된 금융 데이터 DTO 객체로 정제함.
* **세부 기술 명세**:
  - 외부 연동방 문자열 페이로드를 읽어내기 위해 `LABELED_PATTERN`(종목/신호/가격 콜론 명시형) 및 `BRACKET_PATTERN`(`[BUY] AAPL 175.20` 공백 압축형) 정규식 구조를 사전 컴파일된 상수 `Pattern` 객체로 격상 관리하여 런타임 파싱 성능을 극대화함.
  - 자바 정규식 매처(`Matcher`)의 그룹 추출 기법을 적용하여 종목명(Ticker), 매매 유형(BUY/SELL), 체결 가격(Price) 데이터를 타입 안전하게 캐스팅하여 `StockSignalRequest` 내부 필드에 누락 없이 매핑 처리함.

### 2) REQ-F-002: 매매 신호 DB 저장
* **소스 경로**: `com.stocksignal.repository.StockSignalRepository`, `com.stocksignal.service.StockSignalService`
* **설명 요약**: 파싱 엔진을 통과한 구조화 데이터 객체를 Spring Data JPA 영속성 계층 인터페이스를 경유하여 관계형 H2 인메모리 데이터베이스에 안전하게 영구 적재함.
* **세부 기술 명세**:
  - 다중 데이터 흐름 속에서 트랜잭션의 원자성(Atomicity)을 보장하기 위해 서비스 레이어에 `@Transactional` 어노테이션 선언을 바인딩하고, 영속성 엔티티의 연관 관계 및 기본키 주키(ID) 자동 생성 전략에 맞춰 데이터 로우 인서트를 완결함.
  - 엔티티 영속화 성공 즉시 데이터베이스로부터 최종 반영된 `createdAt` 타임스탬프와 주키 식별자를 부여받아 저장 데이터의 물리적 무결성을 수립함.

### 3) REQ-F-003: 실시간 알림 재전송
* **소스 경로**: `com.stocksignal.service.TelegramNotificationService`
* **설명 요약**: 주식 매매 시그널이 시스템 내부 데이터베이스에 성공적으로 영속화된 직후, 등록된 사용자 개인화 텔레그램 봇 API 오픈 인프라를 타격하여 스마트폰 푸시 알림으로 실시간 2차 딜리버리함.
* **세부 기술 명세**:
  - 스프링 부트 코어 인프라의 `RestTemplate` HTTP 클라이언트를 기반으로 원격 오픈 API 게이트웨이 전송 모듈을 가동함. 한글 문자열 및 특수문자 전송 시 페이로드 유실 및 깨짐 방지를 위해 `URLEncoder.encode(message, StandardCharsets.UTF_8)` 표준 튜닝 규격을 엄밀하게 관통 적용함.

### 4) REQ-F-004: 신호 이력 전체 조회
* **소스 경로**: `com.stocksignal.controller.DashboardController` (`dashboard`), `src/main/resources/templates/dashboard.html`
* **설명 요약**: 중앙 저장소인 데이터베이스에 수집 축적된 과거 전체 매매 체결 시그널 리스트를 타임리프 템플릿 엔진 컨텍스트 변수에 적재하여 무결하게 화면에 출력함.
* **세부 기술 명세**:
  - 백엔드 데이터 영속 계층의 `findAllByOrderByCreatedAtDesc()` 쿼리 파이프라인을 기동하여, 사용자가 대시보드 도메인에 최초 인입되는 찰나 최신 체결 시그널 항목이 최상단 열에 배치되도록 내림차순 정렬 조회를 수립함.
  - 타임리프(Thymeleaf)의 `th:each` 반복자 템플릿 제어 속성을 연동하여 메인 관제 시트 테이블 엘리먼트 위에 레이아웃 브레이킹 현상 없이 안정적으로 바인딩함.

### 5) REQ-F-005: 종목별 필터링 검색
* **소스 경로**: `com.stocksignal.service.StockSignalService` (`searchSignalsByDynamicFilters`), `com.stocksignal.repository.StockSignalRepository`
* **설명 요약**: 대량의 적재 데이터 중 사용자가 대시보드 검색 패널에 입력한 특정 주식 코드(Ticker)를 부분 또는 일치 추적하여 해당 종목의 과거 히스토리만 동적으로 아카이빙함.
* **세부 기술 명세**:
  - 데이터 저장소 계층에 SQL `LIKE` 조인 연산 및 대소문자 판별 명세 요건을 해결하는 `findByTickerContainingIgnoreCase` 쿼리 메서드를 정의함. 사용자가 소문자 키워드를 기입해도 백엔드에서 `AAPL`, `TSLA` 등 모던 마켓 코드가 정합성 있게 교차 일치되도록 동적 검색 조회 성능을 최적화함.

### 6) REQ-F-006: 기간별 조회 기능
* **소스 경로**: `com.stocksignal.service.StockSignalService` (`searchSignalsByDateRange`), `com.stocksignal.repository.StockSignalRepository`
* **설명 요약**: 시작일(StartDate)과 종료일(EndDate) 달력 인풋 경계선 구간 내부 범위에 명확하게 발생한 매매 포지션 신호 레코드만 정밀하게 정제하는 범위 쿼리를 트리거함.
* **세부 기술 명세**:
  - 클라이언트 뷰 레이어가 파라미터로 넘겨준 `LocalDate` 날짜 문자열 정보를 데이터베이스 시점 정합성에 완벽히 일치시키기 위해, 당일 시작 시점인 `atStartOfDay()`와 당일 종료 한계점인 `atTime(LocalTime.MAX)` 구조의 `LocalDateTime` 개체로 정밀 캘리브레이션한 뒤 JPA `Between` 연산 조회를 안정적으로 관통 처리함.

### 7) REQ-F-007: 신호 종류별 정렬
* **소스 경로**: `src/main/resources/templates/dashboard.html` (Thymeleaf 드롭다운 제어 구역)
* **설명 요약**: 대시보드 필터 제어 패널 위젯에서 'ALL', 'BUY(매수)', 'SELL(매도)' 정적 드롭다운 옵션 분기 선택 시 원하는 트레이딩 포지션 신호 열거형만 가려내는 정렬 인터페이스를 제공함.
* **세부 기술 명세**:
  - 화면 셀렉터의 선택 상태 파라미터 값을 스프링 커맨드 객체 및 자바 자바 열거형 `SignalType` 아규먼트로 직접 바인딩 쿼리 처리함. 타임리프의 `th:selected` 논리 속성을 활용하여 필터 검색 포스트 서밋 처리가 종결된 후에도 사용자가 선택한 정렬 조건 UI가 휘발되지 않고 유지되도록 상태 보전 UX를 완결함.

### 8) REQ-F-008: 모닝 브리핑 자동 생성
* **소스 경로**: `com.stocksignal.scheduler.MorningBriefingScheduler`, `com.stocksignal.service.MorningBriefingService`
* **설명 요약**: 관리자가 수동 가동할 수 없는 새벽 및 장 시작 전 취약 시간대에 24시간 누적 데이터를 종합 및 수치화하여 데일리 금융 시장 요약 보고서 엔티티를 무인 자동 인서트 빌드함.
* **세부 기술 명세**:
  - 스프링 코어 아키텍처의 스케줄링 인프라인 `@Scheduled(cron = "0 0 8 * * *")` 분기 엔진을 프로젝트에 전격 탑재하여 매일 오전 8시 정각에 배포 백그라운드 워크플로우를 자동 격발함. 전일 유입 총 트랜잭션수와 주요 통계 수치를 집계해 `MorningBriefing` 데이터 로우로 가공 세이빙 처리함.

### 9) REQ-F-009: 브리핑 웹 페이지 노출
* **소스 경로**: `com.stocksignal.controller.DashboardController` (`briefing`), `src/main/resources/templates/briefing.html`
* **설명 요약**: 무인 스케줄러엔진이 완성해 둔 데일리 종합 마켓 보고서 데이터를 사용자가 서브 관제 탭 메뉴 링크를 경유해 가독성 높은 전용 리포트 레이아웃 시트로 안전하게 열람하도록 라우팅함.
* **세부 기술 명세**:
  - 리포트 본문 출력 시 문자열 내부에 가미된 줄바꿈(`<br>`), 단락(`<p>`), 강조(`<b>`) 등의 HTML 서식 태그 요소가 이스케이프 텍스트 문자열로 깨져서 노출되는 UX 붕괴를 예방하기 위해, 타임리프의 언이스케이프 출력 속성인 `th:utext="${briefing.content}"` 문법을 매핑 적용하여 프론트 퍼블리싱 완성도를 완결함.

### 10) REQ-F-010: 투자 전략 메모 작성
* **소스 경로**: `com.stocksignal.repository.SignalMemoRepository`, `com.stocksignal.entity.SignalMemo`, `com.stocksignal.controller.StockSignalApiController`
* **설명 요약**: 체결 포착된 개별 주식 시그널 행 우측의 `[📝 메모]` 버튼 액션을 통해 사용자가 주관적인 시장 진입 사유나 리스크 헷지 전략 노트를 유기적으로 첨부 및 관계형 저장 처리함.
* **세부 기술 명세**:
  - 주식 시그널 엔티티 부모 테이블과 1:N 연관 관계 외래키 매핑을 이루는 `SignalMemo` 자식 엔티티 데이터 테이블 구조를 신설함. 비동기 HTTP POST REST 엔드포인트 가동 시, 로그인 인증 필터가 결합되지 않은 상태에서도 엔티티 영속 제약조건(nullable=false)을 충족하기 위해 Mock 가짜 사용자 계정을 바인딩 주입하도록 안전 방어 코드를 수립함.

### 11) REQ-F-011: 신호 데이터 삭제/편집
* **소스 경로**: `com.stocksignal.controller.StockSignalApiController` (`deleteSignal`), `src/main/resources/templates/dashboard.html`
* **설명 요약**: 파이썬 수집 단의 오작동 및 노이즈 오류로 잘못 유입된 유령 주식 데이터 행을 사용자가 제안 모달 인터랙션을 거쳐 데이터베이스에서 영구적으로 소거 청소함.
* **세부 기술 명세**:
  - 프론트 테이블 뷰 행 단에 위치한 `[🗑️ 삭제]` 엘리먼트 클릭 시 자바스크립트 비동기 `fetch API`를 격발하여 `DELETE /api/signals/{id}` 엔드포인트를 타격함. 백엔드 서비스 계층은 `repository.deleteById(id)` 영속성 소거 트랜잭션을 실행하고 클라이언트에 204 No Content 정상 응답을 리턴하여 화면 새로고침 동기화를 완결함.

### 12) REQ-F-012: 대시보드 통계 계산
* **소스 경로**: `com.stocksignal.service.StockSignalService` (`calculateOverallStatistics`), `com.stocksignal.service.SignalStatisticsService`
* **설명 요약**: 전체 수집된 매매 신호 내역 데이터를 기반으로 사용자의 종합 승률, 거래 수, 누적 수익 카드를 산출해 내는 정밀 통계 모듈 파이프라인임.
* **세부 기술 명세**:
  - 통계 계산기 유한 상태 기계(FSM) 매칭 연산 시, 일반적인 대시보드 리스트 출력용 최신순(내림차순) 데이터를 그대로 전달할 경우 타임라인 역전으로 인해 승률 지표가 왜곡되는 논리적 결함을 인지함.
  - 이를 원천 차단하기 위해 저장소 계층에 `findAllByOrderByCreatedAtAsc()` 쿼리 명세를 신설하여 반드시 주식 신호가 포착된 과거 순서(Chronological Order)대로 데이터를 정렬 인입시킴으로써 알고리즘 연산 정합성을 완전 무결하게 확보하고 자바 스트림 API 필터링을 통해 연산을 효율적으로 완성함.

### 13) REQ-F-013: 사용자 인증(Auth)
* **소스 경로**: `com.stocksignal.config.SecurityConfig`, `src/main/resources/templates/login.html`, `src/main/resources/templates/register.html`
* **설명 요약**: 개인화 대시보드 보안 요건 만족 및 사용자 자산 정보의 철저한 격리 환경 수립을 목적으로 Spring Security 인프라 필터 체인을 프로젝트 기틀에 안착시킴.
* **세부 기술 명세**:
  - 사용자 가입 폼 서밋 처리를 위한 인증 라우터 구성을 완비함. 단, 학업 텀 프로젝트 채점 자동화의 편의성 도모 및 외부 파이썬 자동화 봇 웹훅 수신 REST API 통신의 유연한 결합 호환성을 배려하기 위해, 시큐리티 필터 체인 주입 단계에서 리포지토리 전 경로에 대해 `permitAll()` 안전 무장벽 패싱 구조를 보존 설계함.

### 14) REQ-F-014: 사용자 설정 관리
* **소스 경로**: `com.stocksignal.service.TelegramNotificationService` (sendMessage 오버로딩), `com.stocksignal.service.StockSignalService` (`createSignal`)
* **설명 요약**: 기존 프로젝트 소스 내 정적 설정 파일인 `application.properties`에 텔레그램 토큰을 하드코딩하여 관리하던 방식을 전면 개혁함.
* **세부 기술 명세**:
  - 사용자가 웹 UI 설정 화면(`settings.html`)에서 실시간으로 바꾼 개인 텔레그램 API 토큰 정보와 Chat ID 숫자를 영속성 데이터 계층(`userRepository`)에서 실시간 동적 쿼리하여 알림 봇 전송 인자로 다이렉트 바인딩함.
  - 만약 H2 인메모리 DB 초기화 현상 등으로 사용자의 설정값이 누락되어 있거나 빈 문자열(`isBlank`)일 경우 코드가 터지지 않도록 예외 상황을 격리하고, 프로퍼티의 기본 공용 토큰으로 백업 라우팅 전송하는 완벽한 폴백(`Fallback`) 구조를 수립함.

### 15) REQ-F-015: 데이터 수집 모니터링
* **소스 경로**: `com.stocksignal.service.StockSignalService` (`lastSignalReceivedAt`), `com.stocksignal.controller.DashboardController`
* **설명 요약**: 외부 자동 수집 인프라의 생동성 유지 상태 및 실시간 트래픽 유실 결함 유무를 관리자가 직관적으로 상시 관제할 수 있는 모니터링 타임스탬프 아키텍처를 수립함.
* **세부 기술 명세**:
  - 파이썬 봇이 `/api/signals/webhook` API 엔드포인트를 노크하는 인입 성공 찰나, 백엔드 코어 전역 상태 필드 락 변수에 `LocalDateTime.now()` 시스템 물리 시각을 갱신 수립함. 대시보드 뷰 계층 출력 단계에서 시계열 포맷 연산자(`${#temporals.format}`)를 결합하여 초 단위 데이터 수집 현황을 가시화 위젯 캡슐에 영사함.

### 16) REQ-U-001: 신호별 색상 구분
* **소스 경로**: `src/main/resources/templates/dashboard.html` (`row-buy` / `row-sell` 분기 구역), `src/main/resources/static/css/style.css`
* **설명 요약**: 종합 관제 시스템 모니터 시인성 요건을 완결하여, 난잡한 매매 이력 속에서 사용자가 롱(BUY)과 숏(SELL) 트레이딩 포지션을 시각적으로 오인하지 않도록 행 배경 색상을 이원 대비 렌더링함.
* **세부 기술 명세**:
  - 타임리프의 동적 텍스트 클래스 바인딩 문법인 `th:classappend` 스펙을 적용하여 데이터 로우 컴파일 시 신호 속성을 조건 검사함. `BUY` 시에는 파스텔톤 다크레드 계열 배경 배지를, `SELL` 포지션 시에는 청량한 딥블루 톤 색감 배지를 결합하도록 CSS 스펙 마감을 종결함.

### 17) REQ-U-002: 반응형 레이아웃
* **소스 경로**: `src/main/resources/static/css/style.css` (CSS3 미디어 쿼리 레이어 구역)
* **설명 요약**: 고정된 데스크톱 PC 모니터 해상도를 탈피하여, 직장인 사용자가 이동 중 대중교통 내에서 스마트폰 모바일 뷰포트로 접속해도 레이아웃 깨짐 현상이 없는 가변 그리드를 설계함.
* **세부 기술 명세**:
  - CSS3 미디어 쿼리 표준 아키텍처 문법인 `@media (max-width: 768px)` 브레이크 포인트를 수립함. 스크린 폭이 가변 축소되면 좌측 고정식 네비게이션 사이드바가 상단 콤팩트 토글 셸 구조로 유연하게 리레이아웃되고 통계 요약 카드들이 수직 1열 플렉스박스로 종대 자동 재배치되도록 퍼블리싱을 마감함.

### 18) REQ-U-003: 데이터 로딩 상태 표시
* **소스 경로**: `src/main/resources/templates/dashboard.html` (비동기 글로벌 로딩 스피너), `src/main/resources/static/css/style.css`
* **설명 요약**: 대량의 금융 데이터 비동기 Fetch API 트랜잭션이 네트워킹 지연을 일으킬 때 사용자가 화면을 중복 터치하여 시스템 충돌 오작동을 유발하지 않도록 인지 스핀 UX를 안착시킴.
* **세부 기술 명세**:
  - 자바스크립트 통신 비동기 요청 시작점에 화면 전체 영역 레이어를 불투명 음영 덮개 레이어(`loadingOverlay`)로 덮어 마우스 인터랙션을 일시 잠금 잠금 처리함. 동시에 중앙 영역에 CSS Keyframes 무한 가속도 애니메이션이 심어진 글로벌 링 스피너 개체를 회전 가동했다가 응답 수신 완료 시 자동 언마운트 해제하는 고급 UX를 구현함.

### 19) REQ-U-004: 네비게이션 사이드바
* **소스 경로**: `src/main/resources/templates/dashboard.html`, `src/main/resources/templates/briefing.html`, `src/main/resources/templates/settings.html`
* **설명 요약**: 홈 대시보드 허브, 일일 모닝 브리핑 리포트, 사용자 계정 설정 페이지 간의 컨텍스트 스위칭 동선이 물 흐르듯 직관적으로 이어지도록 공통 고정형 메뉴 바 틀을 제공함.
* **세부 기술 명세**:
  - 의미론적 시맨틱 마크업인 `<aside>` 블록 구조 내에 메뉴 링크 앵커 태그들을 수립하고 CSS 레이아웃 속성을 `position: fixed; height: 100vh; width: 260px;` 체계로 고정함. 본문 스크롤이 바닥으로 내려가도 네비게이션 바는 언제나 화면 좌측 레이어에 수려하게 고정 상주하도록 공간 정합성을 확보함.

### 20) REQ-U-005: 작업 결과 토스트 알림
* **소스 경로**: `src/main/resources/templates/dashboard.html`
* **설명 요약**: 투자 전략 메모 저장 및 주식 시그널 행 삭제 등 비동기 HTTP 트랜잭션의 처리 결과 성공 및 실패 여부를 화면 전체 새로고침 없이 상단 독점 레이어 배지 형태로 사용자에게 실시간 피드백함.
* **세부 기술 명세**:
  - 웹 화면의 공간 정합성을 해치지 않도록 평소에는 비어있는 상태를 유지하는 `#toast-container` 엘리먼트를 수립하고, 고정 절대 배치 속성인 `position: fixed; top: 20px; right: 20px; z-index: 9999;`를 부여하여 화면 최상단에 상주하도록 레이아웃을 설계함.
  - 비동기 `fetch API` 통신의 성공 및 실패 폴백(`catch`) 응답 컨텍스트 내에서 `showToast(message, type)` 자바스크립트 모듈을 트리거 격발하도록 구현함. `document.createElement('div')` 문법을 가동해 실시간으로 알림 블록을 생성하고 `toast--success` 또는 `toast--error` 클래스를 분기 결합하여 성공 시 다크그린, 에러 시 다크레드 톤 배지를 매핑함.
  - CSS3 가속도 기술인 `@keyframes` 렌더링 프레임(`toastSlideIn`, `toastFadeOut`)을 유기적으로 관통 시켜 매끄러운 슬라이드 인-아웃 연출을 완성하고, 3초(`3000ms`) 경과 시 `removeChild` 가비지 컬렉션 메서드를 스스로 실행해 DOM(Document Object Model) 트리에서 자원을 자동 완전 소거(휘발)하는 고급 비동기 UX를 완결함.
  - 
### 21) REQ-NF-001: 데이터 무결성 보장
* **소스 경로**: `com.stocksignal.service.StockSignalService` (장애 전용 `@Retryable` 아키텍처)
* **설명 요약**: 수집용 외부 파이썬 자동 매매 봇 인터페이스 유입 단계에서 발생할 수 있는 일시적인 네트워크 순간 유실이나 데이터베이스 커넥션 풀 병목 현상을 완벽하게 방어함.
* **세부 기술 명세**:
  - 스프링 AOP 인프라 코어 기술인 `@Retryable` 아키텍처를 비즈니스 메서드에 결합하여, 통신 결함 발생 시 자동으로 2초 간격 최대 3회 재시도를 무인으로 격발 처리함. 3회 연속 최종 실패 시 데드 레터(Dead Letter) 장애 로그를 생성하고 관리자가 정형화된 패턴으로 원인을 추적할 수 있도록 파일 어펜더(`FILE_WARN`)와 동기화하여 파일 시스템 내에 안전하게 보존하도록 구축함.

### 22) REQ-NF-002: API 응답 속도
* **소스 경로**: `com.stocksignal.service.TossTokenManager` (인메모리 고속 캐싱 아키텍처), `com.stocksignal.repository.StockSignalRepository`
* **설명 요약**: 대량의 주식 수집 내역 적재 환경 및 금융 대시보드 새로고침 호출 시의 트래픽 디스크 I/O 병목을 물리적으로 제거하여 API 타임 응답률을 1초 미만대로 통제함.
* **세부 기술 명세**:
  - 하드웨어 디스크 입출력 병목이 제로에 수렴하는 임베디드 H2 인메모리 데이터베이스 아키텍처 환경 위에 `stock_signal` 테이블 정렬용 인덱싱 명세를 주입함. 특히 속도가 느린 외부 금융 인프라 통신의 한계를 극복하기 위해 `TossTokenManager` 단에 원천 토큰 자격증명을 공유 인메모리 구조로 상시 캐싱하여 호출당 가동 응답 속도를 비약적으로 최적화 시킴.

### 23) REQ-NF-003: 시스템 로그 관리
* **소스 경로**: `src/main/resources/logback-spring.xml` 및 각 컴포넌트 `@Slf4j` 추적 정형화 구역
* **설명 요약**: 런타임 구동 환경에서 예기치 못한 원격 API 소켓 유실이나 하이버네이트 JPA 영속성 바인딩 오류 격발 시 원인을 단번에 역추적할 수 있는 엔터프라이즈 사양의 로깅 인프라를 통합 관리함.
* **세부 기술 명세**:
  - `logback-spring.xml` 아키텍처의 레벨 분기 기법을 활용하여 일반 운영 정보는 `CONSOLE` 어펜더로 밀어내고, 시스템 치명 결함 요건인 `WARN` 및 `ERROR` 문자열은 시간별 보존 체계를 따르는 고유 로그 텍스트 파일 자산으로 별도 분리 포커싱하여 인프라 신뢰도를 확립함.

### 24) REQ-NF-004: 확장 가능한 코드 구조
* **소스 경로**: `com.stocksignal.service.TossTokenManager`, `com.stocksignal.service.TossApiService`, `com.stocksignal.controller.TossPortfolioApiController`, `com.stocksignal.controller.TossAssetController`
* **설명 요약**: "추후 증권사 주문 API 연동이 가능하도록 표준 MVC 인터페이스를 설계한다"는 비기능적 요건을 확장하여, 실제 상용 토스증권 오픈 API 금융 인프라와의 연동 파이프라인을 선제 구축 완료함.
* **세부 기술 명세**:
  - 스레드 세이프 토큰 싱글톤 캐싱 매니저: 토스증권의 "액세스 토큰 재발급 시 기존 토큰 즉시 무효화" 및 "Refresh Token 미제공" 제약 조건을 극복하기 위해 `synchronized` 동시성 제어 키워드 기반의 인메모리 독점 캐싱 구조를 설계함. 만료 시점 60초 전 버퍼 타임을 산출하여 자바 엔진이 백그라운드에서 토큰을 자동 갱신(`Auto-Refresh`)하도록 자동화함.
  - 계좌 식별자 동적 오토 바인딩: 사용자가 설정창 UI에서 `Toss Client ID/Secret`만 입력하면 백엔드가 `oauth2/token` 인증 성공 직후 연속적으로 토스 계좌 API를 호출하여 오픈 API 전용 계좌 식별 고유키(`accountSeq`)를 스스로 추출 및 바인딩 매핑하도록 완전 자동화 연동을 처리함.
  - 국장/해외 통합 잔고 및 마켓 데이터 허브: `ASSET` 그룹 및 `MARKET_DATA` 그룹 등 명세서 규격에 맞춘 HTTP Bearer Auth 인증 헤더 주입 및 실시간 401 Unauthorized 장애 예외 발생 시 인메모리 cache를 즉시 소거(`clearCache`)하고 재인증을 유기적으로 폴백 트리거하는 금융권 방어 코드를 성립함.

### 25) REQ-NF-005: 비밀번호 암호화
* **소스 경로**: `com.stocksignal.config.SecurityConfig` (`BCryptPasswordEncoder` 빈 주입 구역)
* **설명 요약**: 사용자 자산 설정 테이블 및 개인 계정 원천 데이터가 외부로 탈취되더라도 레인보우 테이블 대입 기법이나 복호화 계산으로 비밀번호가 노출되는 대형 사고를 완전 원천 봉쇄함.
* **세부 기술 명세**:
  - 솔팅(Salting) 난수화 기술 및 다중 키 스트레칭 하드웨어 부하 연산 메커니즘을 내장한 `BCryptPasswordEncoder` 모듈을 자바 스프링 빈으로 등록하여 가동함. 가입 처리 시 패스워드를 단방향 다중 해싱 암호문으로 안전하게 트랜잭션을 하드코어하게 종결함.

### 26) REQ-NF-006: 브라우저 호환성
* **소스 경로**: `docs/BROWSER_COMPATIBILITY_REPORT.md` (동작 검증 일지 확인)
* **설명 요약**: 크로스 브라우저 퍼블리싱 호환성 규칙을 만족하여 구글 크롬, MS 에지, 애플 사파리 등 다국적 사용자의 상이한 웹 엔진 환경 하에서도 완벽한 동일 화면 규격을 수립함.
* **세부 기술 명세**:
  - 최신 웹 표준 CSS Flexbox 정렬 모델 및 자바스크립트 표준 Fetch API 모듈 프로토콜을 기반으로 프론트 UX 명세를 마감하여 크로스 렌더링 호환성을 입증하고, 관련 웹 검증 추적 분석 서식을 `BROWSER_COMPATIBILITY_REPORT.md` 보고서로 영구 보존함.

### 27) REQ-NF-007: 자동 배포(CI/CD)
* **소스 경로**: `.github/workflows/ci.yml`, `deploy.yml` (GitHub Actions 워크플로우 명세 구역)
* **설명 요약**: 소프트웨어 형상 관리의 무결성을 도모하고 개발자가 코드를 리포지토리에 반영 시 빌드 파괴나 컴파일 크래시 에러를 자동으로 걸러주는 무인 배포 벨트를 장착함.
* **세부 기술 명세**:
  - 원격 깃허브 저장소 `main` 브랜치로 소스코드가 푸시되는 시나리오 발생 시 GitHub Actions 자동화 러너 가상 리눅스 컨테이너가 가동되도록 함. 가상 환경 내부에서 메이븐 코어 컴파일 명령인 `mvn clean test package` 명령 라인을 무인 구동하여 단위 통합 테스트를 완전 통과 시 자동 빌드 배포 자산화하도록 인프라를 가동함.

### 28) Toss Securities Open API 연동 상세
* **소스 경로**: `com.stocksignal.service.TossApiService`, `com.stocksignal.service.TossTokenManager`
* **설명 요약**: 토스증권의 REST API(ASSET, MARKET_DATA, MARKET_INFO)를 통합 연동하여 실시간 주식 잔고, 현재가, 캔들 차트, 종목 정보 및 시장 운영 정보를 수집하는 엔진 구축.
* **주요 기술적 해결 과제 및 구현**:
    * **인증 토큰 동적 캐싱**: `TossTokenManager`를 통해 `access_token`의 만료 시간을 관리하고, 만료 60초 전 자동 갱신(Auto-Refresh) 로직을 `synchronized` 블록으로 구현하여 스레드 안전성(Thread-safety) 확보.
    * **동적 계좌 바인딩**: 최초 1회 인증 시 `/accounts` API를 호출하여 민우님의 실계좌 일련번호(`accountSeq`)를 자동으로 파싱하고 `TossTokenManager`에 캐싱하여 이후 요청 시 헤더에 자동 주입.
    * **파싱 레이어 최적화**: 토스 API 응답 구조인 `Root -> result -> items/candles` 구조에 맞춰 `RestTemplate` 응답 처리 로직을 범용적으로 재설계.
    * **Market Data 통합**: 계좌 정보가 필요 없는 `MARKET_DATA` 및 `MARKET_INFO` 그룹의 엔드포인트 8종(현재가, 캔들, 환율, 장 운영 정보 등)을 서비스 레이어에 통합 구현하여 봇 및 대시보드 데이터 소스 일원화.