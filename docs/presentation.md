---
marp: true
theme: gaia
paginate: true
backgroundColor: #fff
---
<!-- _class: lead -->
# 프로젝트 개요

---

## 1. 프로젝트 소개
<style scoped>
section {
  font-size: 27px;
}
</style>
### 프로젝트명
StockSignal-Web (웹 기반 주식 매매 신호 및 대시보드 시스템)

### 프로젝트 목적

**해결하려는 문제**
- 텔레그램 알림의 휘발성 문제를 해결하고, 과거의 매수/매도 신호를 웹에서 체계적으로 아카이빙하여 분석할 수 있도록 합니다.

**대상 사용자**
- 실시간 대응이 어려운 직장인 투자자
- 과거 신호 이력을 분석하고자 하는 개인 투자자

---

## 1. 프로젝트 소개
<style scoped>
section {
  font-size: 27px;
}
</style>
### 기존 방법 대비 개선점
- 단순 텍스트 알림을 넘어, 웹 대시보드를 통한 시각화된 데이터와 종목별 히스토리 조회 기능을 제공합니다.

### 기대 효과

**가치**
- 투자 신호의 누적 데이터를 통해 본인만의 투자 전략을 검증하고 승률을 파악할 수 있습니다.

**학습 목표와의 연계성**
- Spring Boot 프레임워크와 MVC 패턴을 활용하여 실무적인 웹 애플리케이션 구조를 익히고, REST API 연동 및 DB 관리 능력을 배양합니다.

---

## 2. 프로젝트 배경 및 범위
<style scoped>
section {
  font-size: 27px;
}
</style>
### 배경
기존에 사용하던 파이썬 기반 텔레그램 봇의 기능을 확장하여, 데이터의 연속성을 보장하고 사용자 편의성을 높이기 위해 웹 전환을 결정했습니다.

### 프로젝트 범위 (Scope)

| 포함 (In-Scope) | 제외 (Out-of-Scope) |
|---|---|
| 핵심 기능 A: 주식 매수/매도 신호 생성 및 저장 | 고급 보안 처리: OAuth2.0 등 고수준 보안 |
| 핵심 기능 B: 텔레그램 봇을 통한 실시간 알림 전송 | 외부 API 연동: 증권사 주문 API 연동 |
| 기본 UI/UX: 신호 이력 리스트 및 모닝 브리핑 조회 페이지 | 다국어 지원: 한국어 외 타 언어 지원 |

---

## 3. 사용 기술 스택

- **언어**: Java 17, JavaScript, HTML, CSS
- **프레임워크**: Spring Boot
- **버전 관리**: Git & GitHub

---

## 4. 팀 구성 및 GitHub 저장소

### 팀 구성원

| 이름 | 학번 | 역할 |
|---|---|---|
| 노민우 | 20210579 | 팀장 / 백엔드 / API 설계 / DB 설계 |
| 전지훈 | 20213051 | 프론트엔드 / UI 구현 / 테스트 및 문서화 |

---
<!-- _class: lead -->
# 요구사항 분석 및 설계

---

## 1. 요구사항 정의
### 기능적 요구사항
<style scoped>
section {
  font-size: 18px;
}
</style>

| 요구사항ID | 요구사항 명칭 | 상세 설명 | 우선순위 |
| --- | --- | --- | --- |
| REQ-F-001 | 텔레그램 신호 수신 및 파싱 | 파이썬 봇으로부터 전달받은 텍스트 신호에서 종목명, 가격, 신호종류(매수/매도)를 추출함 | P1 |
| REQ-F-002 | 매매 신호 DB 저장 | 파싱된 정보를 MySQL 기반 데이터베이스에 영구적으로 저장하여 휘발성을 방지함 | P1 |
| REQ-F-003 | 실시간 알림 재전송 | DB 저장과 동시에 사용자에게 텔레그램 API를 통해 즉시 알림을 재전송함 | P1 |
| REQ-F-004 | 신호 이력 전체 조회 | 웹 페이지에서 과거부터 현재까지 저장된 모든 매매 신호 리스트를 출력함 | P1 |
| REQ-F-005 | 종목별 필터링 검색 | 특정 종목명을 입력하여 해당 종목의 과거 매매 히스토리만 필터링하여 조회함 | P1 |

---

### 기능적 요구사항 (계속)
<style scoped>
section {
  font-size: 18px;
}
</style>

| 요구사항ID | 요구사항 명칭 | 상세 설명 | 우선순위 |
| --- | --- | --- | --- |
| REQ-F-006 | 기간별 조회 기능 | 시작일과 종료일을 설정하여 특정 기간 내에 발생한 신호만 아카이빙에서 추출함 | P2 |
| REQ-F-007 | 신호 종류별 정렬 | 매수 신호만 보기 또는 매도 신호만 보기 기능을 제공함 | P2 |
| REQ-F-008 | 모닝 브리핑 자동 생성 | 매일 아침 특정 시간(예: 장 시작 전)에 전일 증감과 주요 이슈 요약 데이터를 생성함 | P1 |
| REQ-F-009 | 브리핑 웹 페이지 노출 | 생성된 모닝 브리핑 데이터를 별도의 웹 전용 탭에서 조회할 수 있도록 함 | P1 |
| REQ-F-010 | 투자 전략 메모 작성 | 특정 매매 신호에 대해 사용자가 개인적인 매수/매도 사유를 메모로 남길 수 있는 기능을 제공함 | P3 |

---

### 기능적 요구사항 (계속 2)
<style scoped>
section {
  font-size: 18px;
}
</style>

| 요구사항ID | 요구사항 명칭 | 상세 설명 | 우선순위 |
| --- | --- | --- | --- |
| REQ-F-011 | 신호 데이터 삭제/편집 | 잘못 수집된 데이터나 불필요한 이력을 사용자가 직접 관리(삭제)할 수 있도록 함 | P2 |
| REQ-F-012 | 대시보드 통계 계산 | 전체 아카이빙 데이터를 기반으로 승률(매수 후 매도 신호 발생 시 수익 여부)을 계산함 | P3 |
| REQ-F-013 | 사용자 인증(Auth) | 회원가입, 로그인, 로그아웃 기능을 구현하여 개인화된 대시보드를 제공함 | P1 |
| REQ-F-014 | 사용자 설정 관리 | 웹 UI에서 사용자가 자신의 텔레그램 봇 토큰 및 채팅 ID를 직접 수정/관리할 수 있게 함 | P2 |
| REQ-F-015 | 데이터 수집 모니터링 | 외부 API와의 마지막 통신 상태 및 데이터 동기화 성공 여부를 화면에 표시함 | P3 |

---

### 비기능적 요구사항(UI 포함)
<style scoped>
section {
  font-size: 18px;
}
</style>

| 요구사항ID | 구분 | 요구사항 명칭 | 상세 설명 | 우선순위 |
| --- | --- | --- | --- | --- |
| REQ-U-001 | UI | 신호별 색상 구분 | 리스트에서 매수 신호는 빨간색, 매도 신호는 파란색으로 시각적 대비를 줌 | P1 |
| REQ-U-002 | UI | 반응형 레이아웃 | PC 웹뿐만 아니라 직장인들이 이동 중 모바일로도 확인할 수 있도록 반응형 UI를 적용함 | P2 |
| REQ-U-003 | UI | 데이터 로딩 상태 표시 | 대량의 이력을 불러올 때 스켈레톤 UI 또는 로딩 스피너를 노출하여 사용자 경험을 개선함 | P3 |
| REQ-U-004 | UI | 네비게이션 사이드바 | 홈, 신호 이력, 모닝 브리핑 페이지 간 이동이 용이한 고정 메뉴를 제공함 | P2 |
| REQ-NF-001 | 비기능 | 데이터 무결성 보장 | 신호 수집 중 오류 발생 시 재시도 로직을 통해 데이터 누락을 최소화함 | P1 |

---

### 비기능적 요구사항(UI 포함) (계속)
<style scoped>
section {
  font-size: 18px;
}
</style>

| 요구사항ID | 구분 | 요구사항 명칭 | 상세 설명 | 우선순위 |
| --- | --- | --- | --- | --- |
| REQ-NF-002 | 비기능 | API 응답 속도 | 웹 대시보드 리스트 호출 시 1초 이내에 데이터가 로딩되도록 성능을 최적화함 | P2 |
| REQ-NF-003 | 비기능 | 시스템 로그 관리 | 신호 수신 실패나 DB 연결 오류 발생 시 원인을 파악할 수 있는 시스템 로그를 기록함 | P2 |
| REQ-NF-004 | 비기능 | 확장 가능한 코드 구조 | 추후 증권사 주문 API 연동이 가능하도록 MVC 패턴을 준수하여 인터페이스를 설계함 | P2 |
| REQ-NF-005 | 비기능 | 비밀번호 암호화 | 사용자의 비밀번호는 DB 저장 시 BCrypt 등의 알고리즘을 사용해 단방향 해시 암호화함 | P1 |
| REQ-NF-006 | 비기능 | 브라우저 호환성 | 최신 Chrome, Edge, Safari 등 주요 웹 브라우저에서의 정상 동작을 보장함 | P2 |
| REQ-NF-007 | 비기능 | 자동 배포(CI/CD) | GitHub Actions를 사용하여 main 브랜치 푸시 시 빌드 및 테스트 자동화를 구현함 | P3 |

---

## 2. 시스템 설계 (System Design)
<style scoped>
section {
  font-size: 25px;
}
</style>
### 데이터 흐름도 (Data Flow Diagram)

**[데이터 수집 및 적재]**
```
외부 파이썬 봇 → Telegram API → Spring Boot 서버 (수신 및 가공) → MySQL DB (저장)
```

**[서비스 제공 및 조회]**
```
사용자 ↔ 웹 브라우저(UI) ↔ Spring Boot 서버 (비즈니스 로직) ↔ MySQL DB (조회)
```

---

### MVC 디자인 패턴 - View Layer
<style scoped>
section {
  font-size: 24px;
}
</style>
#### View (Presentation Layer)
- **역할**: 사용자 접점 및 데이터 시각화 (Thymeleaf, Bootstrap 활용)
- **주요 구성**:
  - **메인 대시보드**: 실시간 신호 요약 및 승률 통계
  - **신호 리스트**: 검색/정렬 기능이 포함된 이력 화면
  - **모닝 브리핑 탭**: 일일 시장 요약 리포트 조회
  - **공통 UI**: 반응형 사이드바 및 상태 알림 토스트

---

### MVC 디자인 패턴 - Controller Layer
<style scoped>
section {
  font-size: 24px;
}
</style>
#### Controller (Control Layer)
- **역할**: HTTP 요청 매핑 및 서비스 호출 제어
- **주요 클래스**:
  - **AccountController**: 회원가입 및 로그인 흐름 제어
  - **SignalController**: 신호 데이터 조회, 검색, 삭제 요청 처리
  - **SettingController**: 유저별 텔레그램 API 설정 관리

---

### MVC 디자인 패턴 - Model & Service Layer
<style scoped>
section {
  font-size: 24px;
}
</style>
#### Model & Service (Business & Data Layer)
- **역할**: 핵심 비즈니스 로직 처리 및 DB 연동
- **주요 구성**:
  - **Service**: 신호 파싱 유틸, 브리핑 생성 엔진, 알림 전송 로직
  - **Repository**: MySQL 연동 및 CRUD 인터페이스
  - **Entity**: 데이터 객체 정의 (StockSignal, User, MorningBriefing 등)

---

### 데이터베이스 설계 (ERD 요약)
<style scoped>
section {
  font-size: 20px;
}
</style>

| 테이블명 | 주요 칼럼 |
|---|---|
| users | id, username, password, email, telegram_chat_id, telegram_bot_token, created_at |
| stock_signals | id, user_id, stock_name, stock_code, signal_type, price, raw_message, created_at |
| morning_briefings | id, title, content, market_status, published_at |
| signal_memos | id, signal_id, user_id, memo_content, updated_at |

---

### 화면 설계 (Wireframe)
<style scoped>
section {
  font-size: 24px;
}
</style>

#### Figma 디자인 링크
https://www.figma.com/make/YFBTRYNRjZ4XecQISpKTlo/StockSignal-Web-Dashboard-Design?p=f&t=wWjSiEnqR3TZas6Z-0&fullscreen=1

---

## 로그인 화면
![로그인](https://github.com/user-attachments/assets/af130f20-717a-4cb3-945b-23aea2227220)

---

## 회원가입 화면
![회원가입](https://github.com/user-attachments/assets/a6fdea29-93e1-47ad-8cf0-32d35a6cb09d)

---

## 홈 화면
![홈](https://github.com/user-attachments/assets/a9a88194-3ac9-4756-99b4-51e574560b10)

---

## 모닝 브리핑 화면
![모닝브리핑](https://github.com/user-attachments/assets/52ce903d-476a-48c1-be16-032a23134580)

---

## 설정 화면
![설정](https://github.com/user-attachments/assets/46d23e4e-74ee-43e7-9df3-2346c38d0d42)
