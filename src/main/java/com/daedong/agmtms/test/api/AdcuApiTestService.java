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

//https://1drv.ms/x/c/efa8ab5cd10bbbc6/EcV-RiaAPmVBuyThsse7ez0BOJpwMS8z0ITpRF1-LVU-eQ?e=igAEup
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
        //registOrUpdateVehicleStatus(); // 차량상태 등록/수정 !!(mode입력 추가해줘야함 (작업완료))
        //registFarmland(); //경작지 등록

        //selectFarmland(); //경작지 조회

        //selectFarmlandAsVinidList(); // 차량ID기반 경작지 조회
        //selectFarmlandListAsPoint(); //좌표기반 경작지 조회
        //selectDetailFarmland(); //경작지 상세조회

        //registEquipment(); //작업기 등록
        //selectEquipmentList(); //작업기 리스트 조회
       // selectEquipment(); //작업기 조회

        //registWayPointInfo(); //작업경로 등록
        //registWayPointFile(); //작업경로 파일(RDDF) 등록

        //registLogData(); //작업기록등록 !!!!(유일값 검사 넣어줘야 함)
        //selectWayPointInfo(); //작업경로 조회  !!!(목록에 index 값들 넘겨줘야 함)
        //selectWayPointInfoList(); //작업경로 리스트 조회 !!!(목록에 index 값들 넘겨줘야 함)

        //registDrivingInfo(); //주행데이터 등록
        //selectDrivingInfoFileList(); // 주행데이터 파일리스트 조회
        //deleteContents(); //컨텐츠 삭제;
        //uploadEmergencyStopEvent(); //비상정지 이벤트 관리 (복수 파일 업로드)
        //sendAppPush(); //CASE별 AppPush 발송
        //selectWork(); //작업선택

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
                        "  \"mode\": \"eDrvStatusCmdEmergencyPause\",\n" +
                        "  \"status\": \"normal\",\n" +
                        "  \"gnss\": \"normal\",\n" +
                        "  \"engineControl\": \"normal\",\n" +
                        "  \"missionControl\": \"normal\",\n" +
                        "  \"operationControl\": \"normal\",\n" +
                        "  \"canControl\": \"normal\",\n" +
                        "  \"valveControl\": \"normal\",\n" +
                        "  \"sideBrake\": \"normal\",\n" +
                        "  \"fuel\": \"normal\",\n" +
                        "  \"pto\": \"normal\",\n" +
                        "  \"camera\": \"normal\"\n" +
                        "}");

        validationApis.add(createDefaultHeaders(apiRequest));

    }

    //경작지 등록
    public void registFarmland(){

        ApiRequest apiRequest = createDefaultHeaders(new ApiRequest("/api/adcu/farmland", "POST"))
                .setBody("{\n" +
                        "    \"vinId\": \"S0C8-A0004\",\n" +
                        "    \"plindex\" : 1005,\n" +
                        "    \"name\": \"팜랜드2!!\",\n" +
                        "    \"size\": 1000,\n" +
                        "    \"eqindex\": 2,\n" +
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

    //경작지 조회
    public void selectFarmland() {

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

    // 좌표기반 경작지 조회
    public void selectFarmlandAsVinidList() {

        // 템플릿에서 필요한 Path 파라미터 추출
        String template = "/api/adcu/farmland/list/{vinId}";
        Map<String, String> requiredParams = ApiRequest.extractPathParamsFromTemplate(template);

        logger.info("템플릿: {}", template);
        logger.info("필요한 Path 파라미터: {}", requiredParams.keySet());

        // 올바른 Path 파라미터 설정
        ApiRequest validRequest = new ApiRequest(template, "GET")
                .addPathParam("vinId", "S0C8-A0004");

        // 실제 API 호출 테스트
        this.validationApis.add(createDefaultHeaders(validRequest));

    }

    //작업기 등록
    public void registEquipment(){
        // 차량 정보 수정 (Path 파라미터 사용)
        ApiRequest apiRequest = createDefaultHeaders(new ApiRequest("/api/adcu/equipment", "POST"))
                .setBody("{\n" +
                        "    \"vinId\" : \"OJUN-A0004\",\n" +
                        "    \"eqindex\" : 141,\n" +
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
                        "  \"dtindex\": 4,\n" +
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
                .addQueryParam("vinId", "OJUN-A0004")
                .addQueryParam("plindex", "1")
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
                .addQueryParam("plindex", "1")
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

    // 비상정지 이벤트 관리 API 테스트 (복수 이미지 파일 업로드)
    public void uploadEmergencyStopEvent() {
        logger.info("--- 비상정지 이벤트 관리 API 테스트 시작 (이미지 파일 업로드) ---");
        
        // 테스트용 이벤트 이미지 파일들 생성
        List<File> eventFiles = createTestEventFiles();
        
        if (eventFiles.isEmpty()) {
            logger.error("테스트 이미지 파일 생성 실패로 API 테스트를 건너뜁니다.");
            return;
        }

        // 비상정지 이벤트 업로드 요청 생성
        ApiRequest apiRequest = createDefaultHeaders(new ApiRequest("/api/adcu/vehicle/emergency-stop", "POST"))
                .setContentType("multipart/form-data")
                .addMultipartField("vinId", "S0C8-A0006")
                .addMultipartField("eventType", "1")
                .addMultipartField("eventData", "비상정지 이벤트 테스트 데이터 - 이미지 파일 포함");

        // 복수 이미지 파일 추가
        for (int i = 0; i < eventFiles.size(); i++) {
            File imageFile = eventFiles.get(i);
            if (imageFile != null) {
                apiRequest.addMultipartFile("eventFile", imageFile);
                logger.info("이미지 파일 추가: {} (크기: {} bytes)", 
                        imageFile.getName(), imageFile.length());
            }
        }

        validationApis.add(apiRequest);
        logger.info("--- 비상정지 이벤트 관리 API 테스트 완료 (총 {}개 이미지 파일) ---", 
                eventFiles.stream().filter(f -> f != null).count());
    }

    // 테스트용 이벤트 이미지 파일들 생성
    private List<File> createTestEventFiles() {
        List<File> files = new ArrayList<>();
        
        try {
            // 비상정지 상황 이미지 1 (JPEG)
            File emergencyImage1 = createTestImage("emergency_situation_1", ".jpg", 800, 600, "비상정지 상황 이미지 1");
            files.add(emergencyImage1);

            // 비상정지 상황 이미지 2 (PNG)
            File emergencyImage2 = createTestImage("emergency_situation_2", ".png", 1024, 768, "비상정지 상황 이미지 2");
            files.add(emergencyImage2);

            // 차량 상태 이미지 (JPEG)
            File vehicleStatusImage = createTestImage("vehicle_status", ".jpg", 640, 480, "차량 상태 이미지");
            files.add(vehicleStatusImage);

            // GPS 위치 이미지 (PNG)
            File gpsLocationImage = createTestImage("gps_location", ".png", 1200, 800, "GPS 위치 이미지");
            files.add(gpsLocationImage);

            // 시스템 로그 스크린샷 (JPEG)
            File systemLogImage = createTestImage("system_log_screenshot", ".jpg", 1920, 1080, "시스템 로그 스크린샷");
            files.add(systemLogImage);

            logger.info("테스트 이벤트 이미지 파일 {}개 생성 완료", files.size());
            
        } catch (Exception e) {
            logger.error("테스트 이벤트 이미지 파일 생성 실패", e);
        }
        
        return files;
    }

    // 테스트용 이미지 파일 생성
    private File createTestImage(String baseName, String extension, int width, int height, String text) {
        try {
            File imageFile = File.createTempFile(baseName, extension);
            imageFile.deleteOnExit();

            // Java AWT를 사용하여 간단한 이미지 생성
            java.awt.image.BufferedImage image = new java.awt.image.BufferedImage(width, height, java.awt.image.BufferedImage.TYPE_INT_RGB);
            java.awt.Graphics2D g2d = image.createGraphics();

            // 배경색 설정 (회색)
            g2d.setColor(new java.awt.Color(240, 240, 240));
            g2d.fillRect(0, 0, width, height);

            // 테두리 그리기
            g2d.setColor(java.awt.Color.BLACK);
            g2d.setStroke(new java.awt.BasicStroke(3));
            g2d.drawRect(10, 10, width - 20, height - 20);

            // 텍스트 설정
            g2d.setColor(java.awt.Color.BLACK);
            g2d.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 24));
            
            // 텍스트 중앙 정렬
            java.awt.FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(text);
            int textX = (width - textWidth) / 2;
            int textY = height / 2;
            
            g2d.drawString(text, textX, textY);

            // 추가 정보 표시
            g2d.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 16));
            String info = String.format("크기: %dx%d | 생성시간: %s", width, height, 
                    java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            int infoWidth = fm.stringWidth(info);
            int infoX = (width - infoWidth) / 2;
            g2d.drawString(info, infoX, textY + 40);

            // 파일명 표시
            String fileName = baseName + extension;
            int fileNameWidth = fm.stringWidth(fileName);
            int fileNameX = (width - fileNameWidth) / 2;
            g2d.drawString(fileName, fileNameX, textY + 70);

            g2d.dispose();

            // 이미지를 파일로 저장
            if (extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".jpeg")) {
                javax.imageio.ImageIO.write(image, "JPEG", imageFile);
            } else if (extension.equalsIgnoreCase(".png")) {
                javax.imageio.ImageIO.write(image, "PNG", imageFile);
            } else {
                javax.imageio.ImageIO.write(image, "PNG", imageFile);
            }

            logger.info("테스트 이미지 생성 완료: {} ({}x{})", imageFile.getName(), width, height);
            return imageFile;

        } catch (Exception e) {
            logger.error("테스트 이미지 생성 실패: {}", baseName, e);
            return null;
        }
    }

    //CASE별 AppPUSH 발송
    public void sendAppPush() {

        // 차량 정보 수정 (Path 파라미터 사용)
        ApiRequest apiRequest = createDefaultHeaders(new ApiRequest("/api/adcu/push", "POST"))
                .setBody("{\n" +
                        "    \"vinId\": \"S0C8-A0004\",\n" +
                        "    \"category\": \"eDrvStatusEPauseStartPath\",\n" +
                        "    \"eventType\": \"01\"\n" +
                        "}");

        validationApis.add(createDefaultHeaders(apiRequest));


    }

    public void selectWork(){
        // 차량 정보 수정 (Path 파라미터 사용)
        ApiRequest apiRequest = createDefaultHeaders(new ApiRequest("/api/adcu/select", "POST"))
                .setBody("{\n" +
                        "    \"vinId\": \"OJUN-A0004\",\n" +
                        "    \"plindex\" : 1447,\n" +
                        "    \"eqindex\": 140,\n" +
                        "    \"wpindex\" : 11\n" +
                        "}");

        validationApis.add(createDefaultHeaders(apiRequest));
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
