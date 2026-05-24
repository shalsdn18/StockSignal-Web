# 프로젝트 구조 및 역할, 일정 관리

## 1. 디렉토리 구조

```text
StockSignal-Web/
├── .github/workflows/          # CI/CD 자동화 환경 
│   ├── ci.yml                  # PR 발생 시 자동 빌드 및 Java CI 테스트 수행
│   └── deploy.yml              # main 브랜치 푸시 시 Marp 발표 자료를 GitHub Pages로 자동 배포
├── .vscode/                    # 에디터 설정 (Java 컴파일 독립 실행 모드 등)
├── 1. requirements/            # 프로젝트 요구사항 설계 자산 관리 폴더
│   └── REQUIREMENTS.md         # 기능적/비기능적 요구사항 상세 명세서
├── 2. project_architecture_and_schedule/ # 프로젝트 관리 및 설계 문서 폴더
│   └── Project_Architecture_and_Schedule.md # ◀ [확인] 본 문서가 위치한 경로입니다
├── docs/                       # 프로젝트 시각 자료 및 발표 문서 집합
│   ├── presentation.md         # Marp 기반 슬라이드 발표 자료
│   ├── ERD.png                 # 데이터베이스 스키마 설계 다이어그램
│   └── wireframe.png           # Figma 프로토타입 화면 설계도
├── prompt/                     # AI 자산 아카이빙 폴더
│   └── PROMPT.md               # AI 활용 프롬프트 및 엔지니어링 수행 로그
├── src/                        # 소스 코드 메인 디렉토리
│   ├── main/
│   │   ├── java/com/stocksignal/
│   │   │   ├── config/         # 인프라 공통 설정 (RestTemplate 빈 등록 등 AppConfig)
│   │   │   ├── controller/     # [Controller Layer] HTTP 요청 매핑 및 뷰 포워딩
│   │   │   │   ├── DashboardController.java    # Thymeleaf 대시보드 렌더링 제어
│   │   │   │   └── StockSignalApiController.java # 외부 수신용 REST API 엔드포인트 제어
│   │   │   ├── dto/            # [Data Transfer Object] 계층 간 데이터 교환 객체
│   │   │   │   └── StockSignalRequest.java     # 수신 데이터 유효성 검증 DTO
│   │   │   ├── entity/         # [Model Layer] ORM 매핑용 도메인 선언 계층
│   │   │   │   ├── SignalType.java             # BUY / SELL 열거형 타입 고정
│   │   │   │   └── StockSignal.java            # 매매 신호 JPA 테이블 매핑 엔티티
│   │   │   ├── repository/     # [Model Layer] Spring Data JPA 기반 Repository 인터페이스
│   │   │   │   └── StockSignalRepository.java  # 시간 정렬 및 조건 필터링 전용 CRUD 쿼리 수행
│   │   │   ├── service/        # [Model Layer] 트랜잭션 단위의 핵심 비즈니스 로직
│   │   │   │   ├── StockSignalService.java      # 수신 데이터 파싱 처리 및 알림 위임 로직
│   │   │   │   └── TelegramNotificationService.java # 텔레그램 봇 API 비동기 실시간 전송 처리
│   │   │   └── StockSignalApplication.java # Spring Boot 애플리케이션 메인 부트스트랩 클래스
│   │   └── resources/
│   │       ├── static/css/     # 정적 스타일시트 에셋 디렉토리
│   │       │   └── style.css   # 메인 레이아웃 및 컴포넌트 전용 스타일
│   │       ├── templates/      # [View Layer] Thymeleaf 템플릿 엔진 전용 디렉토리
│   │       │   └── dashboard.html # 요약 카드 및 통계 테이블 데이터 바인딩 화면
│   │       └── application.properties # H2 인메모리 DB 설정 및 환경 변수 파일
│   └── test/                   # JUnit5 및 Mockito 통합 테스트 패키지
│       └── java/com/stocksignal/
│           ├── controller/     # API 및 대시보드 엔드포인트 단위 슬라이스 테스트
│           ├── service/        # 비즈니스 로직 및 Mockito 빈 주입 통합 테스트
│           └── StockSignalApplicationTest.java # 컨텍스트 로드 검증용 테스트
├── .gitignore                  # Git 추적 대상에서 빌드 파일(target/) 및 IDE 설정 제외
├── pom.xml                     # 의존성 주입(JPA, Thymeleaf) 및 Maven 빌드 파이프라인 관리
├── README.md                   # 프로젝트 전체 통합 개요서 (In-Scope, Out-of-Scope 관리)
└── LICENSE                     # 오픈소스 사용권 권한 명시 (MIT License)
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
