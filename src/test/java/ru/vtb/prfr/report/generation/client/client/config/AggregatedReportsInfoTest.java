package ru.vtb.prfr.report.generation.client.client.config;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.vtb.prfr.report.generation.client.client.model.ReportAttribute;
import ru.vtb.prfr.report.generation.client.client.model.ReportMetaInfo;
import ru.vtb.prfr.report.generation.client.client.model.SingleReportGenerationSettings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ExtendWith(SpringExtension.class)
class AggregatedReportsInfoTest {

    @Test
    public void aggregatedReportsInfoTest() {
        SingleReportGenerationSettings first = createForReportWithSysName("Alice", createListOfReportAttributesGivenSize(4));
        SingleReportGenerationSettings second = createForReportWithSysName("Bob", null);
        SingleReportGenerationSettings third = createForReportWithSysName("John", createListOfReportAttributesGivenSize(2));
        SingleReportGenerationSettings fourth = createForReportWithSysName("Andrew", null);
        SingleReportGenerationSettings fifth = createForReportWithSysName("Grey", createListOfReportAttributesGivenSize(1));

        List<SingleReportGenerationSettings> reportsGenerationSettings = new ArrayList<>();
        reportsGenerationSettings.add(first);
        reportsGenerationSettings.add(second);
        reportsGenerationSettings.add(third);
        reportsGenerationSettings.add(fourth);
        reportsGenerationSettings.add(fifth);

        AggregatedReportsInfo aggregatedReportsInfo = new AggregatedReportsInfo(reportsGenerationSettings, null);

        Assertions.assertThat(aggregatedReportsInfo.amountOfReportAttributesConfigs()).isEqualTo(5);
        Assertions.assertThat(aggregatedReportsInfo.amountOfFacsimiles()).isEqualTo(0);
        Assertions.assertThat(aggregatedReportsInfo.getReportGenerationSettingsByReportName("Alice")
                .getReportAttributes().size()).isEqualTo(4);
        Assertions.assertThat(aggregatedReportsInfo.getReportGenerationSettingsByReportName("Bob")
                .getReportAttributes().size()).isEqualTo(0);
        Assertions.assertThat(aggregatedReportsInfo.getReportGenerationSettingsByReportName("Grey")
                .getReportAttributes().size()).isEqualTo(1);
    }

    private static Map<String, ReportAttribute> createListOfReportAttributesGivenSize(Integer size) {
        Map<String, ReportAttribute> reportAttributes = new HashMap<>();
        for (int i = 0; i < size; i++) {
            reportAttributes.put("attr" + i, new ReportAttribute());
        }
        return reportAttributes;
    }

    private static SingleReportGenerationSettings createForReportWithSysName(String sysName, Map<String, ReportAttribute> attributes) {
        ReportMetaInfo reportMetaInfo = new ReportMetaInfo();
        reportMetaInfo.setSysName(sysName);

        SingleReportGenerationSettings settings = new SingleReportGenerationSettings();
        settings.setReportMetaInfo(reportMetaInfo);
        settings.setReportAttributes(attributes);
        return settings;
    }
}