# API Test Program v4

대동공업 TMS(Transport Management System) API 테스트 자동화 프로그램입니다.

## 📋 프로젝트 개요

이 프로그램은 대동공업의 TMS 시스템 API를 자동으로 테스트하고 성능을 측정하는 도구입니다. 다중 환경(개발, 스테이징, 운영)을 지원하며, 병렬 처리와 재시도 로직을 통해 안정적인 테스트를 수행합니다.

## 🏗️ 아키텍처

```
src/main/java/com/daedong/agmtms/test/
├── MainApplication.java          # 메인 애플리케이션 진입점
├── api/                          # API 테스트 관련
│   ├── ApiTestService.java       # API 테스트 서비스
│   └── dto/
│       ├── ApiRequest.java       # API 요청 DTO
│       └── TestResult.java       # 테스트 결과 DTO
├── auth/                         # 인증 관련
│   ├── AuthService.java          # 인증 서비스
│   └── dto/
│       └── TokenResponse.java    # 토큰 응답 DTO
├── config/                       # 설정 관리
│   └── TestConfig.java           # 테스트 설정 로더
├── monitor/                      # 모니터링 및 성능 측정
│   ├── PerformanceCollector.java # 성능 데이터 수집기
│   └── TestMonitor.java          # 테스트 모니터
└── report/                       # 리포트 생성
    └── TestReporter.java         # HTML 리포트 생성기
```

## 🚀 주요 기능

### 1. 다중 환경 지원
- 개발(dev), 스테이징(staging), 운영(prod) 환경 설정
- 환경별 URL 및 인증 정보 분리 관리

### 2. 자동 인증 처리
- 사용자 ID/비밀번호를 통한 자동 로그인
- JWT 토큰 발급 및 관리
- API 요청 시 자동 토큰 헤더 추가

### 3. API 테스트 자동화
- **차량 상태 조회**: `/vehicle/status/list`
- **고객 목록 조회**: `/customer/list`
- **차량 통계 조회**: `/statistics/vehicle`
- **일일 리포트 조회**: `/report/daily`

### 4. 성능 모니터링
- 응답 시간 측정 및 통계
- 성공률 계산
- 실시간 테스트 진행 상황 로깅

### 5. 병렬 처리
- 동시 요청 처리 (기본값: 5개)
- ExecutorService를 활용한 스레드 풀 관리

### 6. 재시도 로직
- 실패 시 자동 재시도 (기본값: 3회)
- 지수 백오프 전략

### 7. 리포트 생성
- HTML 형태의 테스트 결과 리포트
- 성공률, 평균 응답시간, 최대 응답시간 등 통계 정보

## 🛠️ 기술 스택

- **Java 11+**
- **Gradle** - 빌드 도구
- **OkHttp3** - HTTP 클라이언트
- **Jackson** - JSON/YAML 처리
- **SLF4J + Logback** - 로깅
- **JUnit 5** - 테스트 프레임워크

## 📦 의존성

```gradle
dependencies {
    implementation 'com.squareup.okhttp3:okhttp:4.12.0'
    implementation 'com.fasterxml.jackson.core:jackson-databind:2.15.2'
    implementation 'com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.15.2'
    implementation 'org.slf4j:slf4j-api:2.0.9'
    implementation 'ch.qos.logback:logback-classic:1.4.11'
    testImplementation 'org.junit.jupiter:junit-jupiter:5.10.0'
}
```

## ⚙️ 설정

### application.yml
```yaml
environments:
  dev:
    auth-url: https://tms-agri-dev.daedong.co.kr/api/auth
    api-url: https://tms-agri-dev.daedong.co.kr/api/tms
    credentials:
      userId: "mjha"
      password: "123qwer@"

test-config:
  timeout: 30000
  retry-count: 3
  concurrent-requests: 5
```

## 🚀 실행 방법

### 1. 빌드
```bash
./gradlew build
```

### 2. 실행
```bash
# 기본 환경(dev)으로 실행
./gradlew run

# 특정 환경으로 실행
./gradlew run --args="--env=dev"
./gradlew run --args="--env=staging"
./gradlew run --args="--env=prod"
```

### 3. JAR 파일로 실행
```bash
# JAR 파일 생성
./gradlew jar

# JAR 파일 실행
java -jar build/libs/api-test-program-v4-1.0.1.jar --env=dev
```

## 📊 출력 결과

### 콘솔 로그
```
INFO  - API 테스트 프로그램 시작 - 환경: dev
INFO  - 토큰 발급 성공: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
INFO  - [MONITOR] /vehicle/status/list - 성공 (245ms)
INFO  - [SUCCESS] /vehicle/status/list (245ms): {"status":"success",...}
INFO  - [MONITOR] /customer/list - 성공 (189ms)
INFO  - [SUCCESS] /customer/list (189ms): {"customers":[...]}
INFO  - HTML 리포트 생성 완료
INFO  - 테스트 완료
```

### HTML 리포트 (test-report.html)
- 테스트 결과 요약
- API별 상세 결과 테이블
- 성능 통계 정보

## 🔧 설정 옵션

| 설정 | 기본값 | 설명 |
|------|--------|------|
| `timeout` | 30000ms | API 요청 타임아웃 |
| `retry-count` | 3 | 실패 시 재시도 횟수 |
| `concurrent-requests` | 5 | 동시 요청 수 |

## 📁 프로젝트 구조

```
api-test-program-v4/
├── build.gradle                 # Gradle 빌드 설정
├── gradle/                      # Gradle Wrapper
├── src/
│   └── main/
│       ├── java/                # Java 소스 코드
│       └── resources/
│           └── application.yml  # 환경 설정 파일
├── test-report.html            # 생성된 테스트 리포트
└── README.md                   # 프로젝트 문서
```

## 🔒 보안 고려사항

- 인증 정보는 `application.yml` 파일에 저장
- 프로덕션 환경에서는 환경 변수나 외부 설정 관리 도구 사용 권장
- 토큰은 메모리에만 저장되며 프로그램 종료 시 자동 삭제

## 🐛 문제 해결

### 일반적인 문제들

1. **인증 실패**
   - 사용자 ID/비밀번호 확인
   - 네트워크 연결 상태 확인

2. **API 호출 실패**
   - 서버 상태 확인
   - URL 설정 확인
   - 네트워크 방화벽 설정 확인

3. **빌드 오류**
   - Java 버전 확인 (Java 11+ 필요)
   - Gradle Wrapper 권한 확인

## 📈 성능 최적화

- 동시 요청 수 조정으로 처리량 향상
- 타임아웃 설정으로 응답 대기 시간 최적화
- 재시도 횟수 조정으로 안정성과 성능 균형

## 🤝 기여하기

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 라이선스

이 프로젝트는 대동공업 내부 사용을 위한 프로젝트입니다.

## 📞 문의

프로젝트 관련 문의사항이 있으시면 개발팀에 연락해주세요.

---

**버전**: 1.0.1  
**최종 업데이트**: 2024년 