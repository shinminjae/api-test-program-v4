package com.daedong.agmtms.test.report;

import com.daedong.agmtms.test.api.dto.TestResult;
import com.daedong.agmtms.test.monitor.PerformanceCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.util.List;

public class TestReporter {
    private static final Logger logger = LoggerFactory.getLogger(TestReporter.class);

    public void generateHtmlReport(List<TestResult> results) {
        PerformanceCollector collector = new PerformanceCollector();
        long avgTime = collector.getAverageResponseTime(results);
        long maxTime = collector.getMaxResponseTime(results);
        double successRate = collector.getSuccessRate(results);

        try (FileWriter writer = new FileWriter("test-report.html")) {
            writer.write("<html><head><title>API 테스트 리포트</title></head><body>");
            writer.write("<h1>테스트 결과</h1>");
            writer.write("<p>성공률: " + successRate + "%</p>");
            writer.write("<p>평균 응답시간: " + avgTime + "ms</p>");
            writer.write("<p>최대 응답시간: " + maxTime + "ms</p>");
            writer.write("<table border='1'><tr><th>API</th><th>상태</th><th>응답코드</th><th>응답시간</th></tr>");
            for (TestResult r : results) {
                writer.write("<tr><td>" + r.getApi() + "</td><td>" + (r.isSuccess() ? "성공" : "실패") +
                        "</td><td>" + r.getStatusCode() + "</td><td>" + r.getResponseTime() + "ms</td></tr>");
            }
            writer.write("</table></body></html>");
            logger.info("HTML 리포트 생성 완료");
        } catch (Exception e) {
            logger.error("리포트 생성 오류", e);
        }
    }
}
