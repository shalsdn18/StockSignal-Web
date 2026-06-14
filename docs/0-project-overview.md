# StockSignal-Web
웹 기반 주식 매수/매도 신호 및 모닝 브리핑 제공 시스템

## 1. 프로젝트 소개
* **해결하려는 문제**: 텔레그램 알림의 휘발성 문제를 해결하고, 과거의 매수/매도 신호를 웹에서 체계적으로 아카이빙하여 분석할 수 있도록 함.
* **대상 사용자**: 실시간 대응이 어려운 직장인 및 과거 신호 분석이 필요한 개인 투자자.
* **학습 목표**: Spring Boot와 MVC 패턴을 활용한 실무적 구조 이해 및 DB 관리 능력 배양.

---

## 2. 프로젝트 배경 및 범위

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
| 노민우 | 20210579 | 팀장 / 백엔드 / API 설계 / 테스트 및 문서화 |
| 전지훈 | 20213051 | 프론트엔드 / UI 구현 / DB 설계 |
 
---

## 🛠 진행 상황

| 기능 ID | 기능 명칭 | 연결된 Issue | 상태 |
| :--- | :--- | :--- | :--- |
| **F-01** | 실시간 주식 데이터 수집 및 DB 저장 | [#1](https://github.com/shalsdn18/StockSignal-Web/issues/5) | ⚪ 대기 |
| **F-02** | 텔레그램 실시간 알림 재전송 및 설정 | [#2](https://github.com/shalsdn18/StockSignal-Web/issues/6) | ⚪ 대기 |
| **F-03** | 사용자 회원가입/로그인 및 보안 시스템 | [#3](https://github.com/shalsdn18/StockSignal-Web/issues/7) | ⚪ 대기 |
| **F-04** | 신호 이력 조회 대시보드 및 상세 검색 UI | [#4](https://github.com/shalsdn18/StockSignal-Web/issues/8) | ⚪ 대기 |
| **F-05** | 모닝 브리핑 자동 생성 및 대시보드 통계 | [#5](https://github.com/shalsdn18/StockSignal-Web/issues/9) | ⚪ 대기 |
| **F-06** | 투자 메모 작성 기능 및 반응형 UI 개선 | [#6](https://github.com/shalsdn18/StockSignal-Web/issues/10) | ⚪ 대기 |
| **U-01** | 반응형 레이아웃 및 내비게이션 시스템 구축 | [#7](https://github.com/shalsdn18/StockSignal-Web/issues/11) | ⚪ 대기 |
| **NF-01** | 사용자 보안(암호화) 및 MVC 아키텍처 준수 | [#8](https://github.com/shalsdn18/StockSignal-Web/issues/12) | ⚪ 대기 |
| **NF-02** | API 성능 최적화 및 시스템 로그 관리 체계 | [#9](https://github.com/shalsdn18/StockSignal-Web/issues/13) | ⚪ 대기 |
| **NF-03** | CI/CD 배포 자동화 및 브라우저 호환성 검증 | [#10](https://github.com/shalsdn18/StockSignal-Web/issues/14) | ⚪ 대기 |
---

## GitHub 협업 관리 방침

- **Issue 관리**: 모든 기능적 요구사항(REQ-F)은 개별 Issue로 등록하여 진행 상황을 추적함
- **Assignee**: 각 이슈마다 담당 팀원을 지정하여 책임 소재를 명확히 함
- **AI 활용 기록**: AI를 사용하여 작성된 코드는 해당 Issue의 코멘트나 docs/ai_log.md에 기록함
- **PR 및 코드 리뷰**: 기능 구현 완료 시 PR을 생성하고 팀원 간 검토 후 main 브랜치에 병합함

 ---
- 프롬프트 기록은 `PROMPT.md`에서 관리합니다. 세부 기록은 해당 파일을 확인하세요.

