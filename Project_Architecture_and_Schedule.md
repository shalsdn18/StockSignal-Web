# Project Architecture and Schedule

## 1. 프로젝트 개요

StockSignal-Web은 주식 매수/매도 신호와 모닝 브리핑을 웹에서 조회, 저장, 관리할 수 있도록 만든 Spring Boot 기반 웹 애플리케이션입니다. 기존 텔레그램 중심 알림의 휘발성을 줄이고, 과거 신호를 체계적으로 아카이빙하는 것이 핵심 목적입니다.

## 2. 시스템 아키텍처

### 2.1 전체 구조

```text
[사용자]
   ↓
[Web UI / Thymeleaf]
   ↓
[Controller]
   ↓
[Service]
   ↓
[Repository]
   ↓
[Database]
```

외부 알림과 데이터 수집은 텔레그램 메시지 또는 기타 입력 소스에서 시작되며, Spring Boot 서버가 이를 처리해 DB에 저장하고 웹 화면에 노출합니다.

### 2.2 계층별 역할

| 계층 | 주요 책임 | 관련 패키지 예시 |
|---|---|---|
| Presentation | 사용자 화면 렌더링, 폼 입력, 리스트 표시 | `templates/`, `static/` |
| Controller | HTTP 요청 처리, 화면/JSON 응답 분기 | `controller/` |
| Service | 비즈니스 로직, 신호 처리, 통계 계산 | `service/` |
| Repository | DB 조회/저장 | `repository/` |
| Domain | 도메인 모델과 요청/응답 DTO | `entity/`, `dto/` |

### 2.3 주요 기능 흐름

#### 신호 저장 흐름
1. 외부 신호가 서버로 전달된다.
2. Controller가 요청을 받아 Service로 전달한다.
3. Service가 데이터 검증 및 가공을 수행한다.
4. Repository가 DB에 저장한다.
5. 저장 결과를 화면 또는 API 응답으로 반환한다.

#### 신호 조회 흐름
1. 사용자가 대시보드 또는 신호 이력 화면에 접근한다.
2. Controller가 목록 조회 요청을 받는다.
3. Service가 검색 조건, 정렬, 통계 계산을 수행한다.
4. Repository가 데이터를 조회한다.
5. View가 결과를 렌더링한다.

## 3. 컴포넌트 구성

| 컴포넌트 | 역할 |
|---|---|
| DashboardController | 홈 화면과 대시보드 뷰 제공 |
| StockSignalApiController | 주식 신호 CRUD 및 API 응답 제공 |
| StockSignalService | 신호 저장, 조회, 통계 계산 처리 |
| StockSignal | 신호 데이터 엔티티 |
| StockSignalRequest | 신호 입력 DTO |
| application.properties | 서버 설정, DB 설정, 실행 환경 설정 |

## 4. 테스트 전략

| 대상 | 검증 내용 |
|---|---|
| Controller Test | HTTP 상태 코드, View 이름, JSON 응답 형식 검증 |
| Service Test | 비즈니스 로직과 조회/저장 분기 검증 |
| Application Test | Spring 컨텍스트 로딩 확인 |

Spring Boot 4 환경에서는 테스트에서 기존 `@MockBean` 대신 `@MockitoBean`을 사용한다.

## 5. 개발 일정

### 5.1 일정 요약

| 단계 | 기간 | 주요 작업 | 산출물 |
|---|---|---|---|
| 1단계 | 1주차 | 요구사항 정리, 화면 흐름 정의, 기술 스택 확정 | 요구사항 문서, 화면 초안 |
| 2단계 | 2주차 | 도메인 모델 및 DB 설계 | Entity, ERD, DB 스키마 |
| 3단계 | 3주차 | Controller, Service, Repository 기본 구현 | 핵심 백엔드 로직 |
| 4단계 | 4주차 | 대시보드, 신호 이력, 모닝 브리핑 화면 구현 | Thymeleaf 화면 |
| 5단계 | 5주차 | 테스트 코드 작성 및 오류 수정 | 단위 테스트, 통합 테스트 |
| 6단계 | 6주차 | UI 개선, 문서 정리, 발표 자료 보완 | 최종 문서, 발표용 자료 |

### 5.2 주차별 세부 계획

#### 1주차: 기획 및 설계
- 프로젝트 범위 확정
- 핵심 기능 우선순위 정리
- 화면 목록과 데이터 흐름 정리

#### 2주차: 데이터 구조 설계
- Entity 설계
- Repository 구조 정의
- 테이블 관계와 컬럼 정리

#### 3주차: 백엔드 핵심 기능 구현
- 신호 저장 로직 구현
- 신호 목록 조회 로직 구현
- 기본 API 응답 형태 정의

#### 4주차: 프론트엔드 화면 구현
- 대시보드 화면 구성
- 상세 조회 및 검색 UI 구현
- 반응형 스타일 보완

#### 5주차: 테스트 및 안정화
- Controller 테스트 작성
- Service 테스트 작성
- Spring Boot 4 테스트 호환성 점검

#### 6주차: 마무리 및 발표 준비
- README 및 설계 문서 정리
- 발표용 도식 및 시나리오 정리
- 최종 점검 후 제출 준비

## 6. 리스크 및 대응 방안

| 리스크 | 영향 | 대응 방안 |
|---|---|---|
| 테스트 호환성 문제 | 빌드 실패 | Spring Boot 4 기준 테스트 어노테이션 및 의존성 점검 |
| DB 스키마 변경 | 조회/저장 오류 | Entity와 테이블 정의를 함께 관리 |
| 화면과 API 불일치 | 사용자 경험 저하 | Controller와 View 계약을 문서화 |
| 일정 지연 | 기능 누락 | 우선순위가 높은 기능부터 단계별로 구현 |

## 7. 참고 파일

- `README.md`
- `docs/presentation.md`
- `pom.xml`
- `src/main/java/com/stocksignal/controller/`
- `src/main/java/com/stocksignal/service/`
- `src/main/java/com/stocksignal/entity/`
