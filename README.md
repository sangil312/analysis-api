# Analysis API

### 이번 과제에서 가장 중요하다고 판단한 기능
CSV 파싱 기능
- CSV 분석 통계 생성 시 스트리밍 파싱 + 집계 + 오류 라인 처리
- OpenCSV 라이브러리 사용 시 파싱 실패 예외가 메모리에 쌓이지 않도록 처리

### 특히 신경 쓴 부분
적합한 라이브러리 고민
- CSV 파일 파싱 로직 가독성과 요구사항에 맞춰 OpenCSV 라이브러리 사용(Apache Commons CSV 보다 적합하다고 판단)

구조 설계
- 인메모리가 아닌 RDB로 교체, 외부 API 스팩 변경 시에도 핵심 비지니스 로직은 수정하지 않도록 트랜잭션 범위 분리 및 구조 설계

외부 API
- timeout 시 재시도, 4xx 응답 및 재시도 최종 실패 시 "UNKNOWN", 5xx 응답과 재시도 최종 실패 시 서킷브레이커에 집계하여 장애 전파 방지
- 외부 API 작업 메서드를 비동기로 호출
  - 비동기 호출로 인해 IpInfo가 저장되기전 분석 결과 조회 시 상위 N개 IP와 개수는 정상응답 해주고, IpInfo 부분은 "UNKNOWN" 처리

### 실 서비스로 운영한다면 중점 개선하거나 보완할 포인트
- 서비스 운영중인 팀 컨벤션에 따라(혹은 조율하여) 도메인(or 엔티티) 간 의존성 격리를 얼마나 강하게 가져갈지, 계층 구조는 어떻게 나누고 강제할 것 인지 협의
- 멀티 모듈 구조 고려 (외부 클라이언트, Web 계층(Controller), 코어 도메인(서비스), DB 접근(Repository), 로깅 & 모니터링 등)
- 기존 메모리에 올려두던 분석 결과를 RDB로 전환 (IpInfo의 경우 필요시 RDB 사용)
- 다중 인스턴스 환경일 경우 IpInfo 외부 API 호출 캐싱 처리를 Redis와 같은 글로벌 캐시로 변경 고려
- 팀 서비스 기술 스택에 맞춘 http 호출 라이브러리 사용(OpenFeign 등)
- 상위 N개 데이터 추출 시 현재 조건에는 문제가 없으므로, 가독성을 위해 스트림 API 사용(stream().sorted() 전체 정렬 사용), 성능 이슈가 발생할 경우 리팩토링
- 현재는 상위 10개로 고정, 클라이언트 요청에따라 상위 데이터 갯수를 조회 할 수 있도록 변경 고려
- 관리자 페이지 존재 시 여러 설정 값들(파일 사이즈 제한, 라인 제한, 에러 샘플 갯수 등)을 동적으로 수정 및 반영 가능하도록 관리 고려

## 기술 스택
- Java 21
- Spring Boot 4.0.2
- Spring Webflux (WebClient)
- Circuit Breaker Resilience4j
- OpenCSV (CSV 파일 파싱)
- Caffeine (로컬 캐시)
- OpenAPI (API 문서)

## 개발 환경 설정
### IDE 실행
- Run/Debug Configuration -> `Environment variables`에 `IPINFO_TOKEN` 추가 후 실행
  - ex) IPINFO_TOKEN=d63f22528b329e

## API 문서
### `http://localhost:8080/swagger-ui`

## API 샘플
### 분석 생성 API
- `POST /v1/analysis`
- `multipart/form-data`로 `file` 업로드

#### bash
```bash
curl -X POST "http://localhost:8080/v1/analysis" \
  -F "file=@./과제용로그파일.csv"
```

#### Windows Powershell
```powershell
curl.exe -X POST "http://localhost:8080/v1/analysis" `
  -F "file=@$((Resolve-Path '.\과제용로그파일.csv').Path)"
```

#### 응답 예시
```json
{
  "resultType": "SUCCESS",
  "data": {
    "analysisId": 1
  },
  "error": null
}
```
---
### 분석 결과 조회 API
- `GET /v1/analysis/{analysisId}`

#### 응답 예시
```json
{
  "resultType": "SUCCESS",
  "data": {
    "totalRequestCount": 1000,
    "successRatio": 63.3,
    "redirectRatio": 2.7,
    "clientErrorRatio": 32.6,
    "serverErrorRatio": 1.4,
    "parseError": {
      "count": 0,
      "errorSamples": []
    },
    "topRequestUris": [
      {
        "path": "/event/banner/mir2/popup",
        "count": 147
      },
      ...
    ],
    "topStatusCodes": [
      {
        "statusCode": 200,
        "count": 630
      },
      ...
    ],
    "topIps": [
      {
        "ip": "120.242.23.238",
        "count": 114,
        "country": "China",
        "region": "Asia",
        "asn": "AS9808",
        "isp": "China Mobile Communications Group Co., Ltd."
      },
      ...
    ]
  },
  "error": null
}
```
---
### 공통 예외 응답
#### 응답 예시
```json
{
    "resultType": "ERROR",
    "data": null,
    "error": {
        "statusCode": 404,
        "message": "요청 데이터가 존재하지 않습니다."
    }
}
```
