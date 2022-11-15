package ru.vtb.prfr.report.generation.client.client.config;

import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import ru.vtb.prfr.report.generation.client.client.model.*;

import java.util.*;

/**
 * This class represents a parsed and aggregated configuration of PRFR client.
 * Should be used internally by other services inside library to access varios
 * configuration fields.
 * <p>
 * Also note, that bean of this class will be automatically registered in the client's {@link ApplicationContext},
 * and hence you can just autowired it.
 *
 * @author Mikhail Polivakha
 */
public class AggregatedReportsInfo {
    private final List<SingleReportGenerationSettings> singleReportGenerationSettings;

    private final List<ReportGroupFacsimile> reportGroupFacsimiles;

    public AggregatedReportsInfo(List<SingleReportGenerationSettings> singleReportGenerationSettings,
                                 List<ReportGroupFacsimile> reportGroupFacsimiles) {
        this.reportGroupFacsimiles = orElseNewArrayList(reportGroupFacsimiles);
        this.singleReportGenerationSettings = orElseNewArrayList(singleReportGenerationSettings);
    }

    private static <T> List<T> orElseNewArrayList(List<T> reportGroupFacsimiles) {
        return Optional.ofNullable(reportGroupFacsimiles).orElse(new ArrayList<>());
    }

    public AggregatedReportsInfo() {
        this(new ArrayList<>(), new ArrayList<>());
    }

    public SingleReportGenerationSettings getReportGenerationSettingsByReportName(String reportSysName) {
        return this.singleReportGenerationSettings
                .stream()
                .filter(it -> Objects.equals(it.getReportMetaInfo().getSysName(), reportSysName))
                .findFirst()
                .orElse(null);
    }

    public void addReportTemplateFileForReport(Resource templateFile, String reportSysName) {
        this.getReportGenerationSettingsByReportName(reportSysName).setReportFileTemplate(new ReportFileTemplate(reportSysName, templateFile));
    }

    public ReportGroupFacsimile getFacsimileByReportName(String reportSysName) {
        String reportGroup = this.getReportGenerationSettingsByReportName(reportSysName).getReportMetaInfo().getReportGroup();
        return this.reportGroupFacsimiles
                .stream()
                .filter(reportGroupFacsimile -> Objects.equals(reportGroupFacsimile.getReportGroup(), reportGroup))
                .findFirst()
                .orElse(null);
    }

    public Resource getTemplateFileByReportSysName(String reportSysName) {
        return this.getReportGenerationSettingsByReportName(reportSysName).getReportFileTemplate().getFileTemplate();
    }

    public Map<String, ReportAttribute> getJsonAttributesForReport(String reportSysName) {
        return this.singleReportGenerationSettings
                .stream()
                .filter(settings -> {
                    String currentSysName = Optional
                            .ofNullable(settings)
                            .map(SingleReportGenerationSettings::getReportMetaInfo)
                            .map(ReportMetaInfo::getSysName)
                            .orElse(null);

                    return Objects.equals(currentSysName, reportSysName);
                })
                .findFirst()
                .map(SingleReportGenerationSettings::getReportAttributes)
                .orElse(Collections.emptyMap());
    }
    public void addReportConfigurationSettings(SingleReportGenerationSettings singleReportGenerationSettings) {
        this.singleReportGenerationSettings.add(singleReportGenerationSettings);
    }

    public void addReportGroupFacsimile(ReportGroupFacsimile reportGroupFacsimile) {
        this.reportGroupFacsimiles.add(reportGroupFacsimile);
    }

    public int amountOfReportAttributesConfigs() {
        return this.singleReportGenerationSettings.size();
    }

    public int amountOfFacsimiles() {
        return this.reportGroupFacsimiles.size();
    }

    @Override
    public String toString() {
        return "AggregatedReportInfo{" +
                "reportGenerationSettings=" + singleReportGenerationSettings +
                ", reportGroupFacsimiles=" + reportGroupFacsimiles +
                '}';
    }
}