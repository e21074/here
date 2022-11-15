package ru.vtb.prfr.report.generation.client.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.springframework.util.ResourceUtils;
import ru.vtb.prfr.report.generation.client.client.model.ReportConditionalFormatting;
import ru.vtb.prfr.report.generation.client.client.model.SingleReportGenerationSettings;
import ru.vtb.prfr.report.generation.client.client.model.ReportAttribute;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.List;

public class ResourceReader {
    private ResourceReader() {
    }

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
    }

    public static JsonNode readDataJson(String resource) throws IOException {
        return OBJECT_MAPPER.readTree(ResourceUtils.getFile("classpath:" + resource));
    }

    public static Map<String, Object> readBookmarks(String resource) throws IOException {
        return OBJECT_MAPPER.readValue(ResourceUtils.getFile("classpath:" + resource),
                new TypeReference<Map<String, Object>>() {
                });
    }

    public static Map<String, ReportAttribute> readAttributes(String resource) throws IOException {
        return OBJECT_MAPPER.readValue(ResourceUtils.getFile("classpath:" + resource),
                new TypeReference<Map<String, ReportAttribute>>() {
                });
    }

    public static SingleReportGenerationSettings readReportWithConditionalFormatting(
            String reportResource, String scriptResourse
    ) throws IOException {
        SingleReportGenerationSettings reportGenerationSettings = readReport(reportResource);
        String script = FileUtils.readFileToString(
                ResourceUtils.getFile("classpath:" + scriptResourse),
                StandardCharsets.UTF_8
        );
        reportGenerationSettings.setReportConditionalFormatting(
                new ReportConditionalFormatting(reportGenerationSettings.getReportMetaInfo().getSysName(), script)
        );
        return reportGenerationSettings;
    }

    public static SingleReportGenerationSettings readReport(String reportResource) throws IOException {
        return OBJECT_MAPPER.readValue(
                ResourceUtils.getFile("classpath:" + reportResource),
                SingleReportGenerationSettings.class
        );
    }

}
