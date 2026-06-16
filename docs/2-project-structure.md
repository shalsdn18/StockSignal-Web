# 프로젝트 구조 및 역할, 일정 관리

## 1. 디렉토리 구조

```text
StockSignal-Web/
├── .github/workflows/                 # CI/CD 자동화 환경 
│   ├── ci.yml                         # PR 및 메인 브랜치 푸시 시 자동 빌드 및 Java CI 단위 테스트 수행
│   └── deploy.yml                     # main 브랜치 반영 시 Marp 발표 자료를 GitHub Pages로 웹 배포
├── .vscode/                           # 에디터 설정
│   └── settings.json                  # Java 컴파일러 독립 실행 모드 및 IDE 개발 환경 통합 설정
├── data/                              # 로컬 H2 파일 임베디드 데이터베이스 물리 보존 폴더
│   ├── stocksignaldb.lock.db          # 파일 락 제어용 데이터베이스 파일
│   ├── stocksignaldb.mv.db            # 실제 주식 신호, 유저, 메모가 영구 적재되는 MV 스토리지 파일
│   └── stocksignaldb.trace.db         # DB 내부 트랜잭션 추적 및 정밀 이력 로그 파일
├── docs/                              # 텀 프로젝트 소프트웨어 공학 설계 및 검증 산출물 서식군 (가이드라인 준수)
│   ├── 0-project-overview.md          # 프로젝트 통합 개요 및 인/아웃오브스코프 정의서
│   ├── 1-requirement-analysis.md      # 기능적/비기능적 26개 요구사항 분석 명세서
│   ├── 2-project-structure.md         # 현 시스템 아키텍처 다이어그램 및 모듈 정의서
│   ├── 3-feature-implementation.md    # 요구사항ID별 세부 구현 소스 경로 및 기술 명세서
│   ├── 4-summary.md                   # 구현 요약 통계 수식, 팀원 기여도 명세 및 종합 회고록
│   ├── 9-AI-prompts.md                # 개발 단계별 AI 프롬프트 목적, 이력 및 요구사항 매핑지
│   ├── BROWSER_COMPATIBILITY_REPORT.md # 모던 웹 브라우저(Chrome, Edge, Safari) 호환성 검증 보고서
│   └── presentation.md                # Marp 엔진 기반의 마크다운 슬라이드 발표 자료
├── src/                               # 메인 비즈니스 소스 코드 및 통합 테스트 패키지
│   ├── main/
│   │   ├── java/com/stocksignal/
│   │   │   ├── config/                # 인프라 환경 설정 및 글로벌 보안 아키텍처 계층
│   │   │   │   ├── AppConfig.java     # RestTemplate 런타임 빈 등록 및 공통 클라이언트 주입 설정
│   │   │   │   └── SecurityConfig.java # Spring Security 웹 접근 제어 정책 및 비밀번호 BCrypt 암호화 설정
│   │   │   ├── controller/            # [Presentation Layer] HTTP 요청 제어 및 라우팅 계층
│   │   │   │   ├── DashboardController.java     # 대시보드 홈 및 모닝 브리핑 타임리프 화면 렌더링 제어
│   │   │   │   ├── SettingsController.java      # 개인 계정 알림 인프라 설정 변경 폼 엔드포인트 제어
│   │   │   │   ├── StockSignalApiController.java # 파이썬 봇 연동 웹훅 수신 및 비동기 메모/삭제 REST API 처리
│   │   │   │   ├── TestUiController.java        # 백엔드 데이터 결합 전 시나리오 검증용 모의 목업 컨트롤러
│   │   │   │   ├── TossAssetController.java     # 토스증권 API 연동 국내/해외 잔고 holdings 웹 화면 매핑
│   │   │   │   └── TossPortfolioApiController.java # 토스증권 금융 포트폴리오 자산 전송 비동기 REST API 컨트롤러
│   │   │   ├── dto/                   # 계층 간 유효성 검증용 데이터 전송 객체 패키지
│   │   │   │   ├── DashboardStatisticsDto.java  # 메인 관제 위젯 출력용 통계 집계 통합 전달 DTO
│   │   │   │   ├── SignalStatistics.java        # 알고리즘 엔진 계산 통계 속성 DTO
│   │   │   │   └── StockSignalRequest.java      # 외부 유입 텍스트 매매 파싱용 페이로드 규격 검증 DTO
│   │   │   ├── entity/                # [Persistence Layer] 하이버네이트 ORM 엔티티 계층
│   │   │   │   ├── MorningBriefing.java         # 시장 요약 및 당일 이슈 브리핑 영속성 데이터 객체
│   │   │   │   ├── SignalMemo.java              # 매매 시그널별 일대다(1:N) 관계형 투자 메모 객체
│   │   │   │   ├── SignalType.java              # BUY(매수) / SELL(매도) 도메인 고정 트레이딩 열거형
│   │   │   │   ├── StockSignal.java             # 주식 매매 체결 신호 정보 영속화 매스트 테이블 객체
│   │   │   │   ├── SystemConfig.java            # 시스템 런타임 영속성 설정 정의 객체
│   │   │   │   └── User.java                    # 로그인 마스터 정보 및 개인화 텔레그램 토큰 테이블 객체
│   │   │   ├── exception/             # 아키텍처 예외 처리 인프라 계층
│   │   │   │   └── GlobalExceptionHandler.java  # 시스템 전역 컨트롤러 트랜잭션 장애 제어 인터셉터 허브
│   │   │   ├── repository/            # [Persistence Layer] Spring Data JPA 레포지토리 계층
│   │   │   │   ├── MorningBriefingRepository.java
│   │   │   │   ├── SignalMemoRepository.java    # 일대다 메모 로우 CRUD 쿼리 처리
│   │   │   │   ├── StockSignalRepository.java   # 과거순/최신순 시계열 융합 주식 동적 검색 쿼리 수행
│   │   │   │   ├── SystemConfigRepository.java
│   │   │   │   └── UserRepository.java          # 회원 자격 검증 및 커스텀 토큰 정보 갱신 처리
│   │   │   ├── scheduler/             # 배치 무인 동기화 스케줄러 계층
│   │   │   │   └── MorningBriefingScheduler.java # 리눅스 크론탭(@Scheduled) 기반 매일 아침 8시 자동 리포트 점화기
│   │   │   ├── service/               # [Business Logic Layer] 핵심 컴포넌트 서비스 계층
│   │   │   │   ├── MorningBriefingService.java  # 장 시작 전 전일 통계 및 증시 리포트 빌드 컴포넌트
│   │   │   │   ├── SignalStatisticsService.java # FSM 기반 승률, 누적수익률 정밀 산출 알고리즘 연산 서비스
│   │   │   │   ├── StockSignalService.java      # 데이터 수집 모니터링, @Retryable 방어선 구축 및 통계 총괄 제어
│   │   │   │   ├── TelegramNotificationService.java # 설정 테이블 기반의 실시간 사용자 동적 알림 발송 처리
│   │   │   │   ├── TossApiService.java          # 토스증권 통신 엔드포인트 디스패치 및 401 에러 캐시클리어 폴백 제어
│   │   │   │   ├── TossOpenApiService.java      # 오픈 API 금융 데이터 수집 규격 추상화 인터페이스
│   │   │   │   └── TossTokenManager.java        # 스레드세이프(synchronized) 인메모리 독점 토큰 캐싱 매니저
│   │   │   ├── util/                  # 아키텍처 범용 금융 유틸리티 패키지
│   │   │   │   └── TelegramSignalParser.java    # 정규식 패턴 그룹화를 활용한 이중 매칭 주식 데이터 파싱 엔진
│   │   │   └── StockSignalApplication.java     # Spring Boot 웹 애플리케이션 기동 마스터 메인 클래스
│   │   └── resources/
│   │       ├── static/css/            # 프론트엔드 정적 스타일시트 에셋 레이어
│   │       │   └── style.css          # 고정식 네비바, 반응형 미디어쿼리, 글로벌 링스피너 컴포넌트 CSS
│   │       ├── templates/             # [Presentation View Layer] 타임리프 템플릿 디렉토리
│   │       │   ├── briefing.html      # AI 컴파일 오전 모닝 브리핑 출력 리포트 뷰 화면
│   │       │   ├── dashboard.html     # 데이터 수집 모니터링창, 통계 카드, 실시간 비동기 조작 관제 화면
│   │       │   ├── login.html         # 시큐리티 인터셉터 대응 계정 인증 로그인 독립 화면
│   │       │   ├── register.html      # 표준 HTML 유효성 폼 전송 회원가입 독립 화면
│   │       │   └── settings.html      # 개인 알림 토큰 및 토스증권 API 자격증명 수립 관리 설정 화면
│   │       └── application.properties # H2 파일 DB 연결 인프라 소스 정보 및 봇 백업용 정적 설정 프로퍼티
│   └── test/                          # JUnit5 및 Mockito 프레임워크 기반 통합/단위 슬라이스 테스트 패키지
│       ├── java/com/stocksignal/
│       │   ├── controller/            # MockMvc 기반 슬라이스 엔드포인트 및 목업 컴포넌트 연동 검증 테스트
│       │   ├── scheduler/             # 무인 정시 크론 트리거 가동 정합성 테스트
│       │   ├── service/               # Mockito 가짜 협력 객체 주입을 통한 단위 코어 비즈니스 로직 테스트
│       │   ├── util/                  # 정규식 경계 조건 및 비정형 문자열 추출 파싱 정합성 테스트
│       │   └── StockSignalApplicationTest.java # 컨텍스트 로딩 유실 검증 스프링 부트 통합 테스트
│       └── resources/
│           └── application-test.properties # 테스트 독립 가동용 격리 테스트 인메모리 스펙 설정 파일
├── .gitignore                         # 빌드target/, .mv.db 로컬 저장소 및 IDE 캐시 Git 추적 원천 제외
├── pom.xml                            # Spring Boot 및 금융 API 연동 의존성(Maven) 종합 빌드 명세 관리 파일
├── README.md                          # 프로젝트 인/아웃오브스코프 가이드 및 원터치 구동 통합 가이드 개요서
└── LICENSE                            # 오픈소스 사용 배포 권한 규격 명시 (MIT License)
```

---
## 1-1. ERD 다이어그램
<img width="693" height="798" alt="image" src="https://github.com/user-attachments/assets/91da6843-c191-4dbc-9ad7-d58e4b122c48" />

---
## 2. 팀 역할 분담

팀원별 전문성과 요구사항 명세서(REQUIREMENTS.md)의 ID를 매핑하여 책임 범위를 정의했습니다.

| 팀원 (학번) | 담당 역할 | 주요 작업 내용 (요구사항 매핑) |
|---|---|---|
| **노민우 (20210579)** | **팀장 / BE / API** | Spring Boot 서버 구축 및 API 설계 <br>- 텔레그램 API 연동 및 파싱 로직 (**REQ-F-001, 003**) <br>- DB 스키마 설계 및 JPA ... |
| **전지훈 (20213051)** | **FE / 문서화** | Thymeleaf 기반 웹 UI 구현 (**REQ-F-004, 009**) <br>- 반응형 레이아웃 및 시각화 (**REQ-U-001, 002**) <br>- 기술 문서(docs/) 관... |

---

## 3. 협업 및 의사소통 방안 (GitHub 활용)

* **Issues**: 모든 기능적/비기능적 요구사항을 이슈로 등록하고 담당자(Assignee)를 지정함.
* **Projects**: 칸반 보드를 통해 'To Do', 'In Progress', 'Done' 상태를 실시간 공유함.
* **AI 활용 기록**: AI를 통해 작성된 코드와 프롬프트 로그는 `PROMPT.md`에 투명하게 기록함.
* **정기 회의**: 주 1회 오프라인 미팅 및 긴급 이슈 발생 시 Slack/카카오톡 활용.

---

## 4. 일정 관리

| 주차 | 단계 | 상세 작업 내용 | 담당 |
|---|---|---|---|
| **1~2주차** | **개요/분석** | 프로젝트 목적 정의, 요구사항 명세화 및 와이어프레임 설계 | 전체 |
| **3주차** | **설계/분담** | **[현재]** 시스템 구조도 도식화, 역할 분담 및 상세 일정 확정 | 전체 |
| **4주차** | **기능 구현** | REST API 서버 구축, 텔레그램 연동 및 핵심 DB 연동 구현 | 역할별 |
| **5주차** | **테스트/최적화** | 통합 테스트 수행, API 응답 속도 및 데이터 무결성 검증 | 전체 |
| **6주차** | **구조 재정비** | 디렉토리 구성 최적화 및 코드 리팩토링 | 전체 |
| **7주차** | **배포/Release** | GitHub Actions 연동, 빌드 자동화 및 v1.0 릴리즈 | 전체 |
| **8주차** | **아카이빙** | 최종 보고서 작성 및 발표 자료(Marp) 업데이트 | 전체 |

---

## 5. 주요 마일스톤 및 중간 점검

### 5.1 주요 마일스톤 (Milestones)

| 마일스톤 | 목표 시점 | 달성 조건 (Definition of Done) |
|---|---|---|
| M1: 분석 및 설계 완료 | 2주차 종료 | 요구사항 명세서(REQUIREMENTS.md) 및 UI 와이어프레임 확정 |
| M2: 핵심 기능 구현 | 4주차 종료 | 텔레그램 신호 수신 → DB 저장 → API 조회가 포함된 기본 백엔드 로직 완성 |
| M3: 시스템 통합 완료 | 6주차 종료 | 프론트엔드 UI와 백엔드 API 연동 완료 및 전체 디렉토리 구조 최적화 |
| M4: 프로젝트 릴리즈 | 8주차 종료 | GitHub Actions를 통한 배포 자동화 완료 및 최종 프로젝트 아카이빙 |

### 5.2 중간 점검 계획 (Interim Review)

- **점검 시점:** 4주차 금요일 (기능 구현 단계 종료 시)
- **점검 대상:** REQ-F-001(신호 수신), REQ-F-002(DB 저장), REQ-F-004(이력 조회) 등 핵심 요구사항 구현 여부
- **주요 확인 항목:**
  - 데이터 무결성: 외부 텔레그램 메시지가 유실 없이 정해진 규격대로 DB에 파싱되어 저장되는가?
  - API 응답: REST API 호출 시 저장된 데이터가 정상적으로 반환되는가?
  - UI 렌더링: 서버에서 가져온 데이터가 대시보드 리스트에 정상적으로 출력되는가?
- **리스크 대응:** 점검 결과 핵심 기능 구현이 지연될 경우, 5주차 최적화 일정의 일부를 기능 보완으로 전환하여 일정을 조정함.

### 5.3 협업 도구를 활용한 상시 점검

- **Milestone 관리:** GitHub Milestones 기능을 사용하여 주차별 작업 마감일을 설정하고 진행률(%)을 시각화함.
- **코드 리뷰:** 모든 기능은 Pull Request(PR)를 통해 팀원의 승인을 얻은 후 main 브랜치에 병합하여 품질을 유지함.
- **상태 추적:** GitHub Projects의 칸반 보드를 활용하여 미완료 작업(Todo)이 적체되지 않도록 관리함.

### 5.4 화면 설계 (Wireframe)
## 링크 
https://www.figma.com/make/YFBTRYNRjZ4XecQISpKTlo/StockSignal-Web-Dashboard-Design?p=f&t=wWjSiEnqR3TZas6Z-0&fullscreen=1
## 로그인 화면
<img width="2552" height="1330" alt="image" src="https://github.com/user-attachments/assets/af130f20-717a-4cb3-945b-23aea2227220" />

## 회원가입 화면
<img width="2547" height="1332" alt="image" src="https://github.com/user-attachments/assets/a6fdea29-93e1-47ad-8cf0-32d35a6cb09d" />

## 홈 화면
<img width="2553" height="1317" alt="image" src="https://github.com/user-attachments/assets/a9a88194-3ac9-4756-99b4-51e574560b10" />

## 모닝 브리핑 화면
<img width="2550" height="1339" alt="image" src="https://github.com/user-attachments/assets/52ce903d-476a-48c1-be16-032a23134580" />

## 설정 화면
<img width="2547" height="1330" alt="image" src="https://github.com/user-attachments/assets/46d23e4e-74ee-43e7-9df3-2346c38d0d42" />
