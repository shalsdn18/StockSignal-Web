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

