package com.daedong.agmtms.test.api;

import com.daedong.agmtms.test.api.dto.ApiRequest;
import com.daedong.agmtms.test.api.dto.ApiResponse;
import com.daedong.agmtms.test.api.dto.TestResult;
import com.daedong.agmtms.test.config.TestConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
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
        //selectFarmland(); //경작지 조회
        //selectDetailFarmland(); //경작지 상세조회
        //selectFarmlandListAsPoint(); //좌표기반 경작지 조회
        //registEquipment(); //작업기 등록
        //selectEquipmentList(); //작업기 리스트 조회
        //selectEquipment(); //작업기 조회
        //registWayPointInfo(); //작업경로 등록
        //registWayPointFile(); //작업경로 파일(RDDF) 등록
        //registLogData(); //작업기록등록
        //selectWayPointInfo(); //작업경로 조회
        //selectWayPointInfoList(); //작업경로 리스트 조회
        //registDrivingInfo(); //주행데이터 등록
        //selectDrivingInfoFileList() // 주행데이터 파일리스트 조회
        //selectDrivingInfoFileList();
        //deleteContents(); //컨텐츠 삭제;

        executeConcurrentTests(this.validationApis);

        logger.info("=== ADCU API 전체 테스트 완료 ===");
    }

    // 단말기 펌웨어 버전조회
    public void selectFirmWareVersion() {

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


    // 경작지 상세 조회
    public void selectDetailFarmland() {

        // 템플릿에서 필요한 Path 파라미터 추출
        String template = "/api/adcu/farmland/detail/{vinId}/{plindex}";
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

    // 좌표기반 경작지 조회
    public void selectFarmlandListAsPoint() {

        // 템플릿에서 필요한 Path 파라미터 추출
        String template = "/api/adcu/farmland/list";
        Map<String, String> requiredParams = ApiRequest.extractPathParamsFromTemplate(template);

        logger.info("템플릿: {}", template);
        logger.info("필요한 Path 파라미터: {}", requiredParams.keySet());

        // 올바른 Path 파라미터 설정
        ApiRequest validRequest = new ApiRequest(template, "GET")
                .addQueryParam("vinId", "R0C8-A0004")
                .addQueryParam("latitude", "208416.0")
                .addQueryParam("longitude", "434698.0");

        // 실제 API 호출 테스트
        this.validationApis.add(createDefaultHeaders(validRequest));

    }

    //작업기 등록
    public void registEquipment(){
        // 차량 정보 수정 (Path 파라미터 사용)
        ApiRequest apiRequest = createDefaultHeaders(new ApiRequest("/api/adcu/equipment", "POST"))
                .setBody("{\n" +
                        "    \"vinId\" : \"OJUN-A0004\",\n" +
                        "    \"eqindex\" : 140,\n" +
                        "    \"name\": \"test\",\n" +
                        "    \"category\": \"test01\",\n" +
                        "    \"type\": \"tt\",\n" +
                        "    \"w\": 10,\n" +
                        "    \"l\": 20,\n" +
                        "    \"v\": 30,\n" +
                        "    \"d\": 40,\n" +
                        "    \"o\": 50,\n" +
                        "    \"preset\": true,\n" +
                        "    \"h\": 70,\n" +
                        "    \"ridgerWidth\": 80,\n" +
                        "    \"ditchWidth\": 90,\n" +
                        "    \"hydraulicRadius\": 100,\n" +
                        "    \"extra\": \"extraa\"\n" +
                        "}");

        validationApis.add(createDefaultHeaders(apiRequest));

    }

    // 작업기 리스트 조회
    public void selectEquipmentList() {

        // 템플릿에서 필요한 Path 파라미터 추출
        String template = "/api/adcu/equipment/list";
        Map<String, String> requiredParams = ApiRequest.extractPathParamsFromTemplate(template);

        logger.info("템플릿: {}", template);
        logger.info("필요한 Path 파라미터: {}", requiredParams.keySet());

        // 올바른 Path 파라미터 설정
        ApiRequest validRequest = new ApiRequest(template, "GET")
                .addQueryParam("vinId", "S0C8-A0004");

        // 실제 API 호출 테스트
        this.validationApis.add(createDefaultHeaders(validRequest));

    }

    // 작업기 조회
    public void selectEquipment() {

        // 템플릿에서 필요한 Path 파라미터 추출
        String template = "/api/adcu/equipment/{vinId}/{eqindex}";
        Map<String, String> requiredParams = ApiRequest.extractPathParamsFromTemplate(template);

        logger.info("템플릿: {}", template);
        logger.info("필요한 Path 파라미터: {}", requiredParams.keySet());

        // 올바른 Path 파라미터 설정
        ApiRequest validRequest = new ApiRequest(template, "GET")
                .addPathParam("vinId", "S0C8-A0004")
                .addPathParam("eqindex", "1");

        boolean isValid = validRequest.validatePathParams();
        logger.info("올바른 Path 파라미터 검증 결과: {}", isValid);
        // 실제 API 호출 테스트
        this.validationApis.add(createDefaultHeaders(validRequest));

    }

    //작업경로 등록
    public void registWayPointInfo(){
        // 차량 정보 수정 (Path 파라미터 사용)
        ApiRequest apiRequest = createDefaultHeaders(new ApiRequest("/api/adcu/wayPointInfo", "POST"))
                .setBody("{\n" +
                        "    \"vinId\": \"S0C8-A0006\",\n" +
                        "    \"wpindex\": 1,\n" +
                        "    \"eqindex\": 1,\n" +
                        "    \"plindex\": 122,\n" +
                        "    \"overlap\": 0,\n" +
                        "    \"type\": \"eRouteTypeMax\",\n" +
                        "    \"path\": {\n" +
                        "        \"cnt\": 0,\n" +
                        "        \"path\": [\n" +
                        "            {\"x\": 0.0, \"y\": 0.0},\n" +
                        "            {\"x\": 10.0, \"y\": 20.0}\n" +
                        "        ]\n" +
                        "    },\n" +
                        "    \"file\": \"/path/to/rddf/file\",\n" +
                        "    \"list\": {\n" +
                        "        \"cnt\": 0,\n" +
                        "        \"linewp\": [\n" +
                        "            {\n" +
                        "                \"point\": [\n" +
                        "                    {\"x\": 0.0, \"y\": 0.0},\n" +
                        "                    {\"x\": 1.0, \"y\": 1.0}\n" +
                        "                ]\n" +
                        "            }\n" +
                        "        ]\n" +
                        "    },\n" +
                        "    \"PatternBoundary\": {\n" +
                        "        \"cnt\": 0,\n" +
                        "        \"path\": [\n" +
                        "            {\"x\": 0.0, \"y\": 0.0},\n" +
                        "            {\"x\": 5.0, \"y\": 5.0}\n" +
                        "        ]\n" +
                        "    },\n" +
                        "    \"StartPathCount\": 0,\n" +
                        "    \"dateWork\": 0,\n" +
                        "    \"dateCreate\": 0,\n" +
                        "    \"isSeparation\": false,\n" +
                        "    \"isMediumSplit\": false,\n" +
                        "    \"mediumSplitPoints\": [\n" +
                        "        {\"x\": 0.0, \"y\": 0.0},\n" +
                        "        {\"x\": 10.0, \"y\": 10.0}\n" +
                        "    ],\n" +
                        "    \"ABPoints\": [\n" +
                        "        {\"x\": 0.0, \"y\": 0.0},\n" +
                        "        {\"x\": 20.0, \"y\": 20.0}\n" +
                        "    ],\n" +
                        "    \"curveStartIndex\": -1,\n" +
                        "    \"curveEndIndex\": -1,\n" +
                        "    \"curveDirType\": \"eCurveForward\",\n" +
                        "    \"userLogID\": -1,\n" +
                        "    \"extra\": \"추가 데이터\"\n" +
                        "}");

        validationApis.add(createDefaultHeaders(apiRequest));

    }

    //작업경로 파일 등록
    public void registWayPointFile(){
        File testKmlFile = null;
        FileOutputStream fos = null;
        InputStream is = null;
        try {
            is = AdcuApiTestService.class.getClassLoader().getResourceAsStream("file/20250104072341.dlog.rddf");
            if (is != null) {
                testKmlFile = File.createTempFile("temp", ".rddf");
                testKmlFile.deleteOnExit(); // 프로그램 종료시 임시 파일 삭제
                fos = new FileOutputStream(testKmlFile);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, length);
                }
                fos.flush();
            }
        } catch (Exception e) {
            logger.error("파일 생성 중 오류 발생: " + e.getMessage(), e);
        } finally {
            try {
                if (is != null) is.close();
                if (fos != null) fos.close();
            } catch (Exception e) {
                logger.error("스트림 닫기 실패: " + e.getMessage(), e);
            }
        }
        if (testKmlFile == null) {
            return;
        }

        validationApis.add(createDefaultHeaders(new ApiRequest("/api/adcu/wayPointInfo/rddf", "POST"))
                .setContentType("multipart/form-data")
                .addMultipartField("vinId", "S0C8-A0006")
                .addMultipartField("wpindex", "1")
                .addMultipartFile("file", testKmlFile));
    }

    //작업기록 등록
    public void registLogData(){
        // 차량 정보 수정 (Path 파라미터 사용)
        ApiRequest apiRequest = createDefaultHeaders(new ApiRequest("/api/adcu/adcuLogdata", "POST"))
                .setBody("{\n" +
                        "  \"vinId\": \"ADCU-A0001\",\n" +
                        "  \"dtindex\": 2,\n" +
                        "  \"posGPS\": {\n" +
                        "    \"x\": 127.1234567,\n" +
                        "    \"y\": 37.5678901\n" +
                        "  },\n" +
                        "  \"posTM\": {\n" +
                        "    \"x\": 127.1234567,\n" +
                        "    \"y\": 37.5678901\n" +
                        "  },\n" +
                        "  \"wayType\": \"STRAIGHT\",\n" +
                        "  \"lbo\": 0.5,\n" +
                        "  \"curve\": false,\n" +
                        "  \"backDirect\": false,\n" +
                        "  \"speed\": 8.5,\n" +
                        "  \"pto\": true,\n" +
                        "  \"reqRpm\": 1800,\n" +
                        "  \"resRpm\": 1780,\n" +
                        "  \"heading\": 45.2,\n" +
                        "  \"errorDistance\": 0.15,\n" +
                        "  \"cmd\": \"FORWARD\",\n" +
                        "  \"utc\": 1703123456789,\n" +
                        "  \"reqGearStatus\": 1,\n" +
                        "  \"resGearStatus\": 1,\n" +
                        "  \"shuttleLever\": 1,\n" +
                        "  \"reqGearStep\": 3,\n" +
                        "  \"resGearStep\": 3,\n" +
                        "  \"lineHeading\": 45.0,\n" +
                        "  \"headingOffset\": 0.2,\n" +
                        "  \"imuPitch\": 2,\n" +
                        "  \"imuRoll\": 1,\n" +
                        "  \"imuYaw\": 0,\n" +
                        "  \"imuChanging\": false,\n" +
                        "  \"nHitchCurrPos\": 50,\n" +
                        "  \"emergency\": 0,\n" +
                        "  \"wpindex\": 1,\n" +
                        "  \"statusNMEA\": 1,\n" +
                        "  \"reqCurvature\": 0.0,\n" +
                        "  \"resCurvature\": 0.0,\n" +
                        "  \"nLevelPos\": 0,\n" +
                        "  \"nDraftRatio\": 0,\n" +
                        "  \"nPlowingPos\": 0,\n" +
                        "  \"nEngineLoad\": 75,\n" +
                        "  \"bequip\": true,\n" +
                        "  \"brpm\": true,\n" +
                        "  \"adMode\": \"AUTO\",\n" +
                        "  \"drvCmd\": \"FORWARD\",\n" +
                        "  \"routeType\": \"STRAIGHT\",\n" +
                        "  \"templatePathCnt\": 1,\n" +
                        "  \"statusPVED\": 1,\n" +
                        "  \"posTMIMU\": {\n" +
                        "    \"x\": 127.1234567,\n" +
                        "    \"y\": 37.5678901\n" +
                        "  },\n" +
                        "  \"subTransmission\": 1,\n" +
                        "  \"lowSpeed\": 0,\n" +
                        "  \"bSuperLowGearCheckDone\": false,\n" +
                        "  \"bHitchUpDnError\": false,\n" +
                        "  \"extra\": \"test_data\"\n" +
                        "}");

        validationApis.add(createDefaultHeaders(apiRequest));

    }

    // 작업경로 조회
    public void selectWayPointInfo() {

        // 템플릿에서 필요한 Path 파라미터 추출
        String template = "/api/adcu/wayPointInfo/{vinId}/{wpindex}";
        Map<String, String> requiredParams = ApiRequest.extractPathParamsFromTemplate(template);

        logger.info("템플릿: {}", template);
        logger.info("필요한 Path 파라미터: {}", requiredParams.keySet());

        // 올바른 Path 파라미터 설정
        ApiRequest validRequest = new ApiRequest(template, "GET")
                .addPathParam("vinId", "S0C8-A0006")
                .addPathParam("wpindex", "1");

        boolean isValid = validRequest.validatePathParams();
        logger.info("올바른 Path 파라미터 검증 결과: {}", isValid);
        // 실제 API 호출 테스트
        this.validationApis.add(createDefaultHeaders(validRequest));

    }


    // 작업경로 리스트 조회
    public void selectWayPointInfoList() {

        // 템플릿에서 필요한 Path 파라미터 추출
        String template = "/api/adcu/wayPointInfo/list";
        Map<String, String> requiredParams = ApiRequest.extractPathParamsFromTemplate(template);

        logger.info("템플릿: {}", template);
        logger.info("필요한 Path 파라미터: {}", requiredParams.keySet());

        // 올바른 Path 파라미터 설정
        ApiRequest validRequest = new ApiRequest(template, "GET")
                .addQueryParam("vinId", "S0C8-A0010")
               // .addQueryParam("plindex", "1")
                .addQueryParam("page", "1")
                .addQueryParam("size", "100")
                .addQueryParam("sortBy", "date_create")
                .addQueryParam("sortDirection","desc")
                .addQueryParam("status","active");

        // 실제 API 호출 테스트
        this.validationApis.add(createDefaultHeaders(validRequest));

    }

    //작업경로 파일 등록
    public void registDrivingInfo(){

        File testKmlFile = createTestKmlFile();

        validationApis.add(createDefaultHeaders(new ApiRequest("/api/adcu/vehicle/drivingInfo", "POST"))
                .setContentType("multipart/form-data")
                .addMultipartField("vinId", "S0C8-A0006")
                .addMultipartField("plindex", "1")
                .addMultipartField("eqindex", "1")
                .addMultipartFile("file", testKmlFile));
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

    public void selectDrivingInfoFileList(){
        // 템플릿에서 필요한 Path 파라미터 추출
        String template = "/api/adcu/files";
        Map<String, String> requiredParams = ApiRequest.extractPathParamsFromTemplate(template);

        logger.info("템플릿: {}", template);
        logger.info("필요한 Path 파라미터: {}", requiredParams.keySet());

        // 올바른 Path 파라미터 설정
        ApiRequest validRequest = new ApiRequest(template, "GET")
                .addQueryParam("vinId", "S0C8-A0004")
               // .addQueryParam("plindex", "1")
                .addQueryParam("page", "1")
                .addQueryParam("size", "100");

        // 실제 API 호출 테스트
        this.validationApis.add(createDefaultHeaders(validRequest));

    }

    public void deleteContents(){

        // 템플릿에서 필요한 Path 파라미터 추출
        String template = "/api/adcu/contents/delete";
        Map<String, String> requiredParams = ApiRequest.extractPathParamsFromTemplate(template);

        logger.info("템플릿: {}", template);
        logger.info("필요한 Path 파라미터: {}", requiredParams.keySet());

        /*ApiRequest validRequest = new ApiRequest(template, "DELETE")
                .setBody("{\n" +
                        "    \"delType\": \"equipment\",\n" +
                        "    \"vinId\": \"OJUN-A0003\",\n" +
                        "    \"index\": 6\n" +
                        "}");*/

        /*ApiRequest validRequest = new ApiRequest(template, "DELETE")
                .setBody("{\n" +
                        "    \"delType\": \"farmland\",\n" +
                        "    \"vinId\": \"OJUN-A0004\",\n" +
                        "    \"index\": 1\n" +
                        "}");
         */

        ApiRequest validRequest = new ApiRequest(template, "DELETE")
                .setBody("{\n" +
                        "    \"delType\": \"wayPointInfo\",\n" +
                        "    \"vinId\": \"OJUN-A0004\",\n" +
                        "    \"index\": 1\n" +
                        "}");

        // 실제 API 호출 테스트
        this.validationApis.add(createDefaultHeaders(validRequest));


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
