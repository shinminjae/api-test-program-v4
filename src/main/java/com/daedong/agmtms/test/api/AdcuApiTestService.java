package com.daedong.agmtms.test.api;

import com.daedong.agmtms.test.api.dto.ApiRequest;
import com.daedong.agmtms.test.api.dto.ApiResponse;
import com.daedong.agmtms.test.api.dto.TestResult;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdcuApiTestService extends BaseApiTestService {
    private static final Logger logger = LoggerFactory.getLogger(AdcuApiTestService.class);
    private List<ApiRequest> validationApis = new ArrayList<>();


    public AdcuApiTestService(String env, String token) {
        super(env, token);
    }

    @Override
    public void testTmsAdcu() {

        logger.info("=== ADCU API 전체 테스트 시작 ===");

        //selectFirmWareVersion(); //차량 단말기 펌웨어 버전조회
        //registOrUpdateVehicleStatus(); // 차량상태 등록/수정
        //registFarmland(); //경작지 등록
        selectPl(); //경작지 조회

        executeConcurrentTests(this.validationApis);

        logger.info("=== ADCU API 전체 테스트 완료 ===");
    }

    // ADCU API 테스트
    public void selectFirmWareVersion() {
//

        logger.info("--- 차량 단말기 펌웨어 버전조회 테스트 시작 ---");
        //List<ApiRequest> adcuApis = new ArrayList<>();

        // 템플릿에서 필요한 Path 파라미터 추출
        String template = "/api/adcu/firmware/check-latest/{currentVersion}";
        Map<String, String> requiredParams = ApiRequest.extractPathParamsFromTemplate(template);

        logger.info("템플릿: {}", template);
        logger.info("필요한 Path 파라미터: {}", requiredParams.keySet());

        // 올바른 Path 파라미터 설정
        ApiRequest validRequest = new ApiRequest(template, "GET")
                .addPathParam("currentVersion", "1.0");

        boolean isValid = validRequest.validatePathParams();
        logger.info("올바른 Path 파라미터 검증 결과: {}", isValid);

        this.validationApis.add(createDefaultHeaders(validRequest));

    }

    //차량상태 등록/수정
    public void registOrUpdateVehicleStatus(){
//			
        logger.info("--- 차량상태 등록/수정 시작 ---");

        // 차량 정보 수정 (Path 파라미터 사용)
        ApiRequest apiRequest = createDefaultHeaders(new ApiRequest("/api/adcu/vehicle/status", "PUT"))
                .setBody("{\n" +
                        "  \"vinId\": \"ADCU-99999\",\n" +
                        "  \"status\": \"normal\",\n" +
                        "  \"faultCount\": \"0\",\n" +
                        "  \"gnss\": \"normal\",\n" +
                        "  \"engineControl\": \"normal\",\n" +
                        "  \"missionControl\": \"normal\",\n" +
                        "  \"operationControl\": \"normal\",\n" +
                        "  \"joystickControl\": \"normal\",\n" +
                        "  \"valveControl\": \"normal\",\n" +
                        "  \"sideBrake\": \"normal\",\n" +
                        "  \"fuel\": \"normal\",\n" +
                        "  \"pto\": \"normal\",\n" +
                        "  \"camera\": \"normal\",\n" +
                        "  \"canControl\": \"normal\"\n" +
                        "}");

        validationApis.add(createDefaultHeaders(apiRequest));


    }

    //경작지 등록
    public void registFarmland(){
//
        logger.info("--- 차량상태 등록/수정 시작 ---");

        // 차량 정보 수정 (Path 파라미터 사용)
        ApiRequest apiRequest = createDefaultHeaders(new ApiRequest("/api/adcu/farmland", "POST"))
                .setBody("{\n" +
                        "    \"vinId\": \"S0C8-A0004\",\n" +
                        "    \"plindex\" : 1002,\n" +
                        "    \"name\": \"팜랜드2!!\",\n" +
                        "    \"size\": 1000,\n" +
                        "    \"eqindex\": 2,\n" +
                        "    \"totalWorkTime\": 0,\n" +
                        "    \"dateWork\": 1709898253,\n" +
                        "    \"dateCreate\": 1709898253,\n" +
                        "    \"plpath\": {\n" +
                        "        \"plb\": {\n" +
                        "            \"cnt\": 0,\n" +
                        "            \"path\": [\n" +
                        "                {\"x\": 0.0, \"y\": 0.0},\n" +
                        "                {\"x\": 10.5, \"y\": 20.5}\n" +
                        "            ]\n" +
                        "        },\n" +
                        "        \"psb\": {\n" +
                        "            \"cnt\": 0,\n" +
                        "            \"path\": [\n" +
                        "                {\"x\": 0.0, \"y\": 0.0},\n" +
                        "                {\"x\": 5.0, \"y\": 15.0}\n" +
                        "            ]\n" +
                        "        }\n" +
                        "    },\n" +
                        "    \"type\": \"ePlowlandTypePoint\",\n" +
                        "    \"curve\": {\n" +
                        "        \"cnt\": 0,\n" +
                        "        \"points\": [\n" +
                        "            {\"start\": 0, \"end\": 1}\n" +
                        "        ]\n" +
                        "    },\n" +
                        "    \"extra\": \"추가 데이터\"\n" +
                        "}");

        validationApis.add(createDefaultHeaders(apiRequest));


    }

    // 경작지 조회
    public void selectPl() {

        logger.info("--- selectPl 테스트 시작 ---");

        List<ApiRequest> adcuApis = new ArrayList<>();

        // Path 파라미터 테스트 예제
        logger.info("Path 파라미터 테스트 예제:");

        // 템플릿에서 필요한 Path 파라미터 추출
        String template = "/api/adcu/farmland/{vinId}/{plindex}";
        Map<String, String> requiredParams = ApiRequest.extractPathParamsFromTemplate(template);

        logger.info("템플릿: {}", template);
        logger.info("필요한 Path 파라미터: {}", requiredParams.keySet());

        // 올바른 Path 파라미터 설정
        ApiRequest validRequest = new ApiRequest(template, "GET")
                .addPathParam("vinId", "S0C8-A0004")
                .addPathParam("plindex", "1002");

        boolean isValid = validRequest.validatePathParams();
        logger.info("올바른 Path 파라미터 검증 결과: {}", isValid);
        // 실제 API 호출 테스트
        this.validationApis.add(createDefaultHeaders(validRequest));

    }


        // 차량 관련 API 테스트
    public void testVehicleApis() {
        logger.info("--- 차량 관련 API 테스트 시작 ---");
        
        List<ApiRequest> vehicleApis = new ArrayList<>();
        
        // 차량 상태 목록 조회
        vehicleApis.add(createDefaultHeaders(new ApiRequest("/vehicle/status/list", "GET"))
                .addQueryParam("page", "1")
                .addQueryParam("size", "10"));
        
        // 차량 상세 정보 조회 (Path 파라미터 사용)
        vehicleApis.add(createDefaultHeaders(new ApiRequest("/vehicle/{vinId}/detail", "GET"))
                .addPathParam("vinId", "TEST_VIN_001")
                .addQueryParam("includeHistory", "true"));
        
        // 차량 등록
        vehicleApis.add(createDefaultHeaders(new ApiRequest("/vehicle/register", "POST"))
                .setBody("{\"vinId\":\"TEST_VIN_002\",\"model\":\"TEST_MODEL\",\"year\":2024}"));
        
        // 차량 정보 수정 (Path 파라미터 사용)
        vehicleApis.add(createDefaultHeaders(new ApiRequest("/vehicle/{vinId}", "PUT"))
                .addPathParam("vinId", "TEST_VIN_001")
                .setBody("{\"model\":\"UPDATED_MODEL\",\"year\":2024,\"status\":\"ACTIVE\"}"));
        
        // 차량 삭제 (Path 파라미터 사용)
        vehicleApis.add(createDefaultHeaders(new ApiRequest("/vehicle/{vinId}", "DELETE"))
                .addPathParam("vinId", "TEST_VIN_002"));
        
        // 차량 작업 이력 조회 (Path 파라미터 사용)
        vehicleApis.add(createDefaultHeaders(new ApiRequest("/vehicle/{vinId}/work-history", "GET"))
                .addPathParam("vinId", "TEST_VIN_001")
                .addQueryParam("startDate", "2024-01-01")
                .addQueryParam("endDate", "2024-12-31"));
        
        executeConcurrentTests(vehicleApis);
        logger.info("--- 차량 관련 API 테스트 완료 ---");
    }

    // 고객 관련 API 테스트
    public void testCustomerApis() {
        logger.info("--- 고객 관련 API 테스트 시작 ---");
        
        List<ApiRequest> customerApis = new ArrayList<>();
        
        // 고객 목록 조회
        customerApis.add(createDefaultHeaders(new ApiRequest("/customer/list", "GET"))
                .addQueryParam("page", "1")
                .addQueryParam("size", "20")
                .addQueryParam("searchType", "name")
                .addQueryParam("searchValue", "테스트"));
        
        // 고객 상세 정보 조회 (Path 파라미터 사용)
        customerApis.add(createDefaultHeaders(new ApiRequest("/customer/{customerId}", "GET"))
                .addPathParam("customerId", "CUST_001"));
        
        // 고객 등록
        customerApis.add(createDefaultHeaders(new ApiRequest("/customer/register", "POST"))
                .setBody("{\"name\":\"테스트고객\",\"phone\":\"010-1234-5678\",\"email\":\"test@example.com\"}"));
        
        // 고객 정보 수정 (Path 파라미터 사용)
        customerApis.add(createDefaultHeaders(new ApiRequest("/customer/{customerId}", "PUT"))
                .addPathParam("customerId", "CUST_001")
                .setBody("{\"name\":\"수정된고객명\",\"phone\":\"010-9876-5432\",\"email\":\"updated@example.com\"}"));
        
        // 고객 삭제 (Path 파라미터 사용)
        customerApis.add(createDefaultHeaders(new ApiRequest("/customer/{customerId}", "DELETE"))
                .addPathParam("customerId", "CUST_002"));
        
        // 고객 차량 목록 조회 (Path 파라미터 사용)
        customerApis.add(createDefaultHeaders(new ApiRequest("/customer/{customerId}/vehicles", "GET"))
                .addPathParam("customerId", "CUST_001")
                .addQueryParam("status", "ACTIVE"));
        
        executeConcurrentTests(customerApis);
        logger.info("--- 고객 관련 API 테스트 완료 ---");
    }

    // 통계 관련 API 테스트
    public void testStatisticsApis() {
        logger.info("--- 통계 관련 API 테스트 시작 ---");
        
        List<ApiRequest> statisticsApis = new ArrayList<>();
        
        // 차량 통계
        statisticsApis.add(createDefaultHeaders(new ApiRequest("/statistics/vehicle", "GET"))
                .addQueryParam("startDate", "2024-01-01")
                .addQueryParam("endDate", "2024-12-31"));
        
        // 작업 통계
        statisticsApis.add(createDefaultHeaders(new ApiRequest("/statistics/work", "GET"))
                .addQueryParam("period", "month")
                .addQueryParam("year", "2024"));
        
        // 수익 통계
        statisticsApis.add(createDefaultHeaders(new ApiRequest("/statistics/revenue", "GET"))
                .addQueryParam("startDate", "2024-01-01")
                .addQueryParam("endDate", "2024-12-31")
                .addQueryParam("groupBy", "month"));
        
        executeConcurrentTests(statisticsApis);
        logger.info("--- 통계 관련 API 테스트 완료 ---");
    }

    // 리포트 관련 API 테스트
    public void testReportApis() {
        logger.info("--- 리포트 관련 API 테스트 시작 ---");
        
        List<ApiRequest> reportApis = new ArrayList<>();
        
        // 일일 리포트
        reportApis.add(createDefaultHeaders(new ApiRequest("/report/daily", "GET"))
                .addQueryParam("date", "2024-01-15"));
        
        // 월간 리포트
        reportApis.add(createDefaultHeaders(new ApiRequest("/report/monthly", "GET"))
                .addQueryParam("year", "2024")
                .addQueryParam("month", "1"));
        
        // 연간 리포트
        reportApis.add(createDefaultHeaders(new ApiRequest("/report/yearly", "GET"))
                .addQueryParam("year", "2024"));
        
        executeConcurrentTests(reportApis);
        logger.info("--- 리포트 관련 API 테스트 완료 ---");
    }

    // 주행 데이터 관련 API 테스트 (멀티파트 데이터 예제 포함)
    public void testDrivingDataApis() {
        logger.info("--- 주행 데이터 관련 API 테스트 시작 ---");
        
        List<ApiRequest> drivingDataApis = new ArrayList<>();
        
        // 주행 데이터 목록 조회
        drivingDataApis.add(createDefaultHeaders(new ApiRequest("/vehicle/drivingInfo/list", "GET"))
                .addQueryParam("vinId", "TEST_VIN_001")
                .addQueryParam("startDate", "2024-01-01")
                .addQueryParam("endDate", "2024-01-31"));
        
        // 주행 데이터 상세 조회 (Path 파라미터 사용)
        drivingDataApis.add(createDefaultHeaders(new ApiRequest("/vehicle/drivingInfo/{drivingInfoId}", "GET"))
                .addPathParam("drivingInfoId", "12345"));
        
        // 주행 데이터 등록 (멀티파트 데이터 예제)
        drivingDataApis.add(createDrivingDataRegistrationRequest());
        
        // 주행 데이터 수정 (Path 파라미터 사용)
        drivingDataApis.add(createDefaultHeaders(new ApiRequest("/vehicle/drivingInfo/{drivingInfoId}", "PUT"))
                .addPathParam("drivingInfoId", "12345")
                .setBody("{\"description\":\"수정된 주행 데이터\",\"tags\":[\"test\",\"updated\"]}"));
        
        // 주행 데이터 삭제 (Path 파라미터 사용)
        drivingDataApis.add(createDefaultHeaders(new ApiRequest("/vehicle/drivingInfo/{drivingInfoId}", "DELETE"))
                .addPathParam("drivingInfoId", "12346"));
        
        // 특정 차량의 주행 데이터 목록 조회 (Path 파라미터 사용)
        drivingDataApis.add(createDefaultHeaders(new ApiRequest("/vehicle/{vinId}/drivingInfo", "GET"))
                .addPathParam("vinId", "TEST_VIN_001")
                .addQueryParam("startDate", "2024-01-01")
                .addQueryParam("endDate", "2024-01-31")
                .addQueryParam("page", "1")
                .addQueryParam("size", "20"));
        
        // 주행 데이터 파일 다운로드 (Path 파라미터 사용)
        drivingDataApis.add(createDefaultHeaders(new ApiRequest("/vehicle/drivingInfo/{drivingInfoId}/download", "GET"))
                .addPathParam("drivingInfoId", "12345")
                .addQueryParam("format", "kml"));
        
        // 주행 데이터 분석 결과 조회 (Path 파라미터 사용)
        drivingDataApis.add(createDefaultHeaders(new ApiRequest("/vehicle/drivingInfo/{drivingInfoId}/analysis", "GET"))
                .addPathParam("drivingInfoId", "12345")
                .addQueryParam("analysisType", "performance"));
        
        executeConcurrentTests(drivingDataApis);
        logger.info("--- 주행 데이터 관련 API 테스트 완료 ---");
    }

    // 주행 데이터 등록 요청 생성 (멀티파트 데이터 예제)
    private ApiRequest createDrivingDataRegistrationRequest() {
        // 테스트용 KML 파일 생성 (실제 환경에서는 실제 파일을 사용)
        File testKmlFile = createTestKmlFile();
        
        return createDefaultHeaders(new ApiRequest("/api/adcu/vehicle/drivingInfo", "POST"))
                .setContentType("multipart/form-data")
                .addMultipartField("vinId", "TEST_VIN_001")
                .addMultipartField("plindex", "1")
                .addMultipartField("eqindex", "1")
                .addMultipartFile("file", testKmlFile);
    }

    // 테스트용 KML 파일 생성
    private File createTestKmlFile() {
        try {
            File tempFile = File.createTempFile("test_driving_data", ".kml");
            tempFile.deleteOnExit();
            
            // 간단한 KML 파일 내용 생성
            String kmlContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n" +
                    "  <Document>\n" +
                    "    <name>Test Driving Data</name>\n" +
                    "    <Placemark>\n" +
                    "      <name>Test Track</name>\n" +
                    "      <LineString>\n" +
                    "        <coordinates>\n" +
                    "          127.0,37.0,0\n" +
                    "          127.001,37.001,0\n" +
                    "          127.002,37.002,0\n" +
                    "        </coordinates>\n" +
                    "      </LineString>\n" +
                    "    </Placemark>\n" +
                    "  </Document>\n" +
                    "</kml>";
            
            java.nio.file.Files.write(tempFile.toPath(), kmlContent.getBytes());
            return tempFile;
        } catch (Exception e) {
            logger.error("테스트 KML 파일 생성 실패", e);
            return null;
        }
    }

    // 응답 데이터 분석 및 로깅
    public void analyzeResponses() {
        logger.info("=== 응답 데이터 분석 ===");
        
        for (TestResult result : results) {
            if (result.isSuccess() && result.getApiResponse() != null) {
                ApiResponse response = result.getApiResponse();
                
                logger.info("result: {}", result.toString());
                logger.info("response: {}", response.toString());

                // 데이터 필드 분석
                if (response.getData() != null) {
                    logger.info("  - 데이터 존재: true");
                    List<JsonNode> dataList = response.getDataList();
                    if (!dataList.isEmpty()) {
                        logger.info("  - 데이터 개수: {}", dataList.size());
                        // 첫 번째 데이터 항목의 필드들 출력
                        JsonNode firstItem = dataList.get(0);
                        firstItem.fieldNames().forEachRemaining(fieldName -> {
                            logger.info("    - {}: {}", fieldName, firstItem.get(fieldName));
                        });
                    }
                } else {
                    logger.info("  - 데이터 존재: false");
                }
                logger.info("---");
            }
        }
    }
}
