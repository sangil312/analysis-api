# Analysis API 구현 초안

## 이번 과제에서 가장 중요하다고 판단한 기능

## 특히 신경 쓴 부분

## 실 서비스로 운영한다면 중점 개선하거나 보완할 포인트


## 기술 스택
- Java 21
- Spring Boot 4.0.2
- Spring Webflux (WebClient)
- Circuit Breaker Resilience4j
- OpenCSV 5.12.0

## 개발 환경 설정
### IDE 실행
- Run/Debug Configuration의 `Environment variables`에 `IPINFO_TOKEN` 추가 후 실행
  - ex) IPINFO_TOKEN=d63f22528b329e

### JAR 실행
- 실행 서버(OS)에 `IPINFO_TOKEN` 환경변수 등록 후 실행
```powershell
$env:IPINFO_TOKEN="발급받은_토큰"
java -jar build/libs/analysis-0.0.1-SNAPSHOT.jar
```

## API 샘플
### 분석 생성 API
- `POST /analysis`
- `multipart/form-data`로 `file` 업로드

#### bash
```bash
curl -X POST "http://localhost:8080/analysis" \
  -F "file=@./과제용로그파일.csv"
```

#### Windows Powershell
```powershell
curl.exe -X POST "http://localhost:8080/analysis" `
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

### 분석 결과 조회 API
- `GET/analysis/{analysisId}`

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