# API 테스트 프로그램 v4

로그인하여 토큰을 받아와서 각 사이트의 API를 테스트하는 프로그램입니다.

## 주요 기능

### 🔧 리팩토링된 구조
- **명확한 헤더/파라미터 설정**: 체이닝 방식으로 쉽게 API 요청 구성
- **구조화된 응답 처리**: `ApiResponse` 클래스로 개별 필드 접근 가능
- **멀티파트 데이터 지원**: 파일 업로드 등 복잡한 요청 처리
- **사이트별 API 테스트 서비스**: 각 도메인별로 분리된 테스트 클래스

### 🚀 지원하는 API 타입
1. **ADCU API** (`AdcuApiTestService`)
   - 차량 관련 API
   - 고객 관련 API
   - 통계 관련 API
   - 리포트 관련 API
   - 주행 데이터 API (멀티파트 데이터 예제 포함)

2. **관리 시스템 API** (`ManagementApiTestService`)
   - 사용자 관리 API
   - 시스템 설정 API
   - 알림 관리 API
   - 감사 로그 API

3. **모바일 API** (`MobileApiTestService`)
   - 모바일 차량 API
   - 모바일 작업 API
   - 모바일 위치 API
   - 모바일 알림 API
   - 모바일 동기화 API

## 사용법

### 기본 실행
```bash
# 모든 API 테스트 실행
./gradlew run

# 특정 환경에서 실행
./gradlew run --args="--env=tms-dev"

# 특정 API 타입만 테스트
./gradlew run --args="--env=tms-dev --api=adcu"
./gradlew run --args="--env=tms-dev --api=management"
./gradlew run --args="--env=tms-dev --api=mobile"
```

### 명령행 옵션
- `--env=<환경명>`: 테스트할 환경 지정 (기본값: tms-dev)
- `--api=<API타입>`: 테스트할 API 타입 지정 (기본값: all)
  - `adcu`: ADCU API만 테스트
  - `management`: 관리 시스템 API만 테스트
  - `mobile`: 모바일 API만 테스트
  - `pathparam`: Path 파라미터 예제 API만 테스트
  - `all`: 모든 API 테스트

### Path 파라미터 예제 실행
```bash
# Path 파라미터 사용법 예제 실행
./gradlew run --args="--env=tms-dev --api=pathparam"
```

## API 요청 설정 예제

### 기본 GET 요청
```java
ApiRequest request = new ApiRequest("/vehicle/status/list", "GET")
    .addQueryParam("page", "1")
    .addQueryParam("size", "10")
    .addHeader("X-Custom-Header", "value");
```

### POST 요청 with JSON Body
```java
ApiRequest request = new ApiRequest("/vehicle/register", "POST")
    .setBody("{\"vinId\":\"TEST_VIN_002\",\"model\":\"TEST_MODEL\",\"year\":2024}")
    .addHeader("Content-Type", "application/json");
```

### Path 파라미터 사용 예제
```java
// 단일 Path 파라미터
ApiRequest request = new ApiRequest("/vehicle/{vinId}/detail", "GET")
    .addPathParam("vinId", "TEST_VIN_001")
    .addQueryParam("includeHistory", "true");

// 다중 Path 파라미터
ApiRequest request = new ApiRequest("/vehicle/{vinId}/alarm/{alarmId}", "PUT")
    .addPathParam("vinId", "TEST_VIN_001")
    .addPathParam("alarmId", "ALARM_001")
    .setBody("{\"threshold\":6000,\"enabled\":false}");

// Path 파라미터와 쿼리 파라미터 조합
ApiRequest request = new ApiRequest("/vehicle/{vinId}/drivingInfo", "GET")
    .addPathParam("vinId", "TEST_VIN_001")
    .addQueryParam("startDate", "2024-01-01")
    .addQueryParam("endDate", "2024-12-31")
    .addQueryParam("page", "1")
    .addQueryParam("size", "20");
```

### 멀티파트 데이터 요청 (파일 업로드)
```java
File kmlFile = new File("driving_data.kml");
ApiRequest request = new ApiRequest("/api/adcu/vehicle/drivingInfo", "POST")
    .setContentType("multipart/form-data")
    .addMultipartField("vinId", "TEST_VIN_001")
    .addMultipartField("plindex", "1")
    .addMultipartField("eqindex", "1")
    .addMultipartFile("file", kmlFile);
```

## 응답 처리 예제

### 기본 응답 정보 접근
```java
ApiResponse response = executeApiCall(request);

// 기본 정보
int statusCode = response.getStatusCode();
boolean isSuccess = response.isSuccess();
long responseTime = response.getResponseTime();

// API 응답 구조에 맞는 필드들
String apiId = response.getApiId();
Integer code = response.getCode();
String message = response.getMessage();
Long serverTime = response.getServerTime();
String traceId = response.getTraceId();
```

### JSON 응답 데이터 접근
```java
// 단일 필드 접근
String fieldValue = response.getString("fieldName");
Integer intValue = response.getInteger("fieldName");
Long longValue = response.getLong("fieldName");
Boolean boolValue = response.getBoolean("fieldName");

// 중첩된 필드 접근
String nestedValue = response.getNestedString("data.user.name");
Integer nestedInt = response.getNestedInteger("data.user.age");

// 배열 데이터 접근
List<JsonNode> dataList = response.getDataList();
for (JsonNode item : dataList) {
    String name = item.get("name").asText();
    Integer age = item.get("age").asInt();
}
```

## 멀티파트 데이터 예제

### 주행 데이터 등록 API (KML 파일 업로드)
```java
// 테스트용 KML 파일 생성
File testKmlFile = createTestKmlFile();

// 멀티파트 요청 생성
ApiRequest request = new ApiRequest("/api/adcu/vehicle/drivingInfo", "POST")
    .setContentType("multipart/form-data")
    .addMultipartField("vinId", "TEST_VIN_001")
    .addMultipartField("plindex", "1")
    .addMultipartField("eqindex", "1")
    .addMultipartFile("file", testKmlFile);

// API 호출 및 응답 처리
ApiResponse response = executeApiCall(request);
if (response.isSuccess()) {
    // 응답 데이터 분석
    String originalFileName = response.getNestedString("data.originalFileName");
    String fileName = response.getNestedString("data.fileName");
    Integer fileSize = response.getNestedInteger("data.fileSize");
    Integer drivingInfoId = response.getNestedInteger("data.drivingInfoId");
}
```

## 테스트 결과 분석

### 응답 데이터 분석
```java
// 각 API 테스트 서비스에서 사용 가능
public void analyzeResponses() {
    for (TestResult result : results) {
        if (result.isSuccess() && result.getApiResponse() != null) {
            ApiResponse response = result.getApiResponse();
            
            logger.info("API: {}", result.getApi());
            logger.info("  - 상태 코드: {}", response.getStatusCode());
            logger.info("  - 응답 시간: {}ms", response.getResponseTime());
            logger.info("  - API ID: {}", response.getApiId());
            logger.info("  - 응답 코드: {}", response.getCode());
            logger.info("  - 메시지: {}", response.getMessage());
            
            // 데이터 필드 분석
            if (response.getData() != null) {
                List<JsonNode> dataList = response.getDataList();
                logger.info("  - 데이터 개수: {}", dataList.size());
            }
        }
    }
}
```

## 설정 파일

### application.yml
```yaml
test:
  retry-count: 3
  concurrent-requests: 5

environments:
  tms-dev:
    api-url: "https://dev-api.example.com"
    auth-url: "https://dev-auth.example.com"
    credentials:
      userId: "testuser"
      password: "testpass"
```

## 빌드 및 실행

```bash
# 프로젝트 빌드
./gradlew build

# 테스트 실행
./gradlew test

# 애플리케이션 실행
./gradlew run

# JAR 파일 생성
./gradlew jar
```

## 로그 및 리포트

- **콘솔 로그**: 실시간 테스트 진행 상황 및 결과
- **HTML 리포트**: 상세한 테스트 결과 리포트 생성
- **응답 분석**: 각 API 응답의 구조화된 데이터 분석

## 주요 개선사항

1. **명확한 API 요청 구성**: 체이닝 방식으로 헤더, 파라미터, 바디 설정
2. **구조화된 응답 처리**: JSON 응답의 개별 필드에 쉽게 접근
3. **멀티파트 데이터 지원**: 파일 업로드 등 복잡한 요청 처리
4. **사이트별 분리**: 각 도메인별로 독립적인 테스트 서비스
5. **상세한 응답 분석**: API 응답의 모든 필드 분석 및 로깅
6. **확장 가능한 구조**: 새로운 API 타입 쉽게 추가 가능 