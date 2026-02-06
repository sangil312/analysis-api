## 과제 목적
- Java/Spring Boot 기반 API 설계/구현 역량
- 로그 파싱 및 데이터 집계 로직 설계 능력
- 외부 Public API(ipinfo) 연동 및 장애/레이트리밋 고려
- 성능/확장 관점(대용량 로그 처리, 캐싱)과 운영 관점(로깅/설정 분리) 확인
- GitHub 기반 개발 프로세스(커밋, PR, 문서화) 확인

## 과제 개요
- 제공되는 접속 로그 csv 파일을 업로드하면, 서버가 로그를 파싱하여 요약
- 통계를 만들고, 로그에 등장한 IP에 대해 ipinfo로 국가/도시/ASN 정보를 조회(가능한 범위 내)하여 결과를 포함한 리포트를 제공하는 백엔드 애플리케이션을 구현합니다.
- ipinfo Lite API(외부 API) 사용

## 기능 구현
### 로그 업로드 및 분석 실행
- Endpoint: POST /analysis
- 입력: 로그 파일(텍스트) 업로드 (multipart/form-data)
- 동작:
    - 파일을 스트리밍 방식으로 읽으며 파싱
    - 파싱 오류 라인은 스킵하되, 오류 개수/샘플을 결과에 포함
- 출력: analysisId 반환 (동기 처리도 가능하나, 1주 범위에서는 “동기 + 작은 파일”로 제한해도 무방)

### 분석 결과 조회
- Endpoint: GET /analysis/{analysisId}
- 포함해야 할 기본 통계:
    - 총 요청 수, 성공(2xx) / 리다이렉트(3xx) / 클라이언트(4xx) / 서버(5xx) 비율 (%)
    - 상위 N개 Path (요청 수 기준)
    - 상위 N개 상태코드
    - 상위 N개 IP (요청 수 기준)

### IP 정보 enrichment (ipinfo 활용)
- 분석 결과에 다음을 포함:
    - 상위 N개 IP에 대해 ipinfo 조회 후 country, region, city, org(ASN/ISP)등을 결과에 포함
- 필수 고려사항
    - 레이트리밋/실패 시 처리: 타임아웃, 재시도(최대 1~2회), 실패 시 fallback(“UNKNOWN”)
    - 동일 IP 반복 조회 방지: 메모리 캐시(예: Caffeine/ConcurrentHashMap) 적용
    - ipinfo 토큰은 환경변수/설정 파일로 분리 (코드 하드코딩 금지)

### API 명세/문서화
- README에 실행 방법, 엔드포인트, 샘플 요청/응답을 포함
- (가능하면) OpenAPI(Swagger) 제공

### csv 데이터 예시
- TimeGenerated [UTC],ClientIp,HttpMethod,RequestUri,UserAgent,HttpStatus,HttpVersion,ReceivedBytes,SentBytes,ClientResponseTime,SslProtocol,OriginalRequestUriWithArgs
- "1/29/2026, 5:44:10.000 AM",121.158.115.86,GET,/event/banner/mir2/popup,MyThreadedApp/1.0,200,HTTP/1.1,176,1138,0,TLSv1.2,/event/banner/mir2/popup

### 구현 제약
- 저장소(RDB/Redis 등) 사용 금지(환경 부담 제거 목적)
    - 분석 결과는 메모리 보관(서버 재시작 시 유실 허용)
- 최대 파일 크기 가이드: 예) 50MB(또는 200k lines)까지 처리 가능하도록 목표
- 동시 분석 요청 처리: 기본 동기 처리 가능(가산점: 비동기/큐 처리)

## 비기능 요구사항
- 예외 처리: 잘못된 로그 라인/빈 파일/대용량 파일(메모리 폭주 방지) 대응
- 성능: 파일을 한 번에 통째로 메모리에 올리지 않도록 설계(스트리밍/라인 단위 처리)
- 구조: controller/service/parser/client 등 계층 분리
- 로깅: 핵심 이벤트(분석 시작/종료, ipinfo 실패, 파싱 오류) 구조화 로그 권장

## 평가 기준
- 정확성: 파싱/집계/퍼센타일 계산 신뢰도
- 견고함: 오류 라인/외부 API 실패 대응
- 구조/가독성: 계층 분리, 명확한 책임
- 운영 관점: 설정 분리, 로깅, 기본 모니터링 포인트
