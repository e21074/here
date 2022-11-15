package ru.vtb.prfr.report.generation.client.config;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import ru.vtb.prfr.report.generation.client.client.config.AggregatedReportsInfo;
import ru.vtb.prfr.report.generation.client.client.config.ClasspathContentLoader;
import ru.vtb.prfr.report.generation.client.client.model.ReportConditionalFormatting;
import ru.vtb.prfr.report.generation.client.client.model.ReportGroupFacsimile;
import ru.vtb.prfr.report.generation.client.client.model.SingleReportGenerationSettings;
import ru.vtb.prfr.report.generation.client.utils.DirectoryResolverHelper;
import ru.vtb.prfr.report.generation.client.utils.PrfrJsonOperator;

import java.io.IOException;

/**
 * This configuration class is registering {@link AggregatedReportsInfo} bean.
 * Essentially, what it does - it parses classpath configs and creates an instance of {@link AggregatedReportsInfo}
 *
 * @author Mikhail Polivakha
 */
@Configuration
@ComponentScan("ru.vtb.prfr.report.generation.client")
public class ReportAttributesConfig {

    private static final Logger logger = LoggerFactory.getLogger(ReportAttributesConfig.class);

    /**
     * The root directories structure inside the JAR classpath, where PRFR 1875 config attributes and conditional formatting are located
     */
    @Value("${report.config.files.location.attribute:/reports-generation/reports-meta/}")
    private String reportsAttributesConfigLocation;

    /**
     * The root directories structure inside the JAR classpath, where PRFR 1875 facsimile files are located
     */
    @Value("${report.config.files.location.facsimile:/reports-generation/facsimile}")
    private String reportsFacsimileDirectory;

    /**
     * The general name of the file, that contains active attributes. Set globally, for all reports
     */
    @Value("${report.file.name.active-attributes:active_attributes.json}")
    private String activeAttributesFileName;

    /**
     *
     */
    @Value("${report.template.file.location:/reports-generation/templates}")
    private String pathToReportTemplateFiles;

    private final ClasspathContentLoader classpathContentLoader;

    private final DirectoryResolverHelper directoryResolverHelper;

    private final PrfrJsonOperator prfrJsonOperator;


    public ReportAttributesConfig(ClasspathContentLoader classpathContentLoader,
                                  DirectoryResolverHelper directoryResolverHelper,
                                  PrfrJsonOperator prfrJsonOperator) {
        this.classpathContentLoader = classpathContentLoader;
        this.directoryResolverHelper = directoryResolverHelper;
        this.prfrJsonOperator = prfrJsonOperator;
    }

    @Bean("aggregatedReportConfig")
    public AggregatedReportsInfo parseConfiguration() throws IOException {

        AggregatedReportsInfo aggregatedReportsInfo = new AggregatedReportsInfo();

        enrichWithReportAttributesConfiguration(aggregatedReportsInfo);

        enrichWithReportGroupFacsimiles(aggregatedReportsInfo);

        enrichWithTemplateFiles(aggregatedReportsInfo);

        if (logger.isInfoEnabled()) {
            logger.info("Parsed {} prfr reports config files and {} facsimiles", aggregatedReportsInfo.amountOfReportAttributesConfigs(), aggregatedReportsInfo.amountOfFacsimiles());
            logger.info("The AggregatedReportInfo is : {}", aggregatedReportsInfo);
        }

        return aggregatedReportsInfo;
    }

    private void enrichWithTemplateFiles(AggregatedReportsInfo aggregatedReportsInfo) {
        Resource[] resources = classpathContentLoader.loadEntriesInsidePath(pathToReportTemplateFiles+ "/**");
        for (Resource resource : resources) {
            if (isTemplateFile(resource)) {
                String reportSysName = directoryResolverHelper.getFileNameOfClassPathResource(resource);
                aggregatedReportsInfo.addReportTemplateFileForReport(resource, reportSysName);
            }
        }
    }

    private boolean isTemplateFile(Resource resource) {
        return StringUtils.containsAny(resource.getFilename(), ".docx", ".dotx");
    }

    private static boolean isFacsimileImageFile(Resource resource) {
        return StringUtils.containsAny(resource.getFilename(), ".png", ".jpeg", ".jpg");
    }

    private void enrichWithReportGroupFacsimiles(AggregatedReportsInfo aggregatedReportsInfo) throws IOException {
        Resource[] resources = classpathContentLoader.loadEntriesInsidePath(reportsFacsimileDirectory + "/**");

        for (Resource resource : resources) {
            if (isFacsimileImageFile(resource)) {
                ReportGroupFacsimile reportGroupFacsimile = new ReportGroupFacsimile(
                        directoryResolverHelper.getFileNameOfClassPathResource(resource),
                        IOUtils.toByteArray(resource.getInputStream())
                );
                aggregatedReportsInfo.addReportGroupFacsimile(reportGroupFacsimile);
            }
        }
    }

    private void enrichWithReportAttributesConfiguration(AggregatedReportsInfo aggregatedReportsInfo) {
        Resource[] configFiles = classpathContentLoader.loadEntriesInsidePath(
                reportsAttributesConfigLocation + "**/" + activeAttributesFileName
        );

        for (Resource configurationFile : configFiles) {
            SingleReportGenerationSettings singleReportGenerationSettings = createReportGenerationSettingsObject(configurationFile);
            aggregatedReportsInfo.addReportConfigurationSettings(singleReportGenerationSettings);
        }
    }

    private SingleReportGenerationSettings createReportGenerationSettingsObject(Resource configurationFile) {
        SingleReportGenerationSettings singleReportGenerationSettings = prfrJsonOperator.parseJsonFileToObject(configurationFile);
        classpathContentLoader.loadConditionalFormattingResource(singleReportGenerationSettings.getReportMetaInfo().getSysName())
                .ifPresent(js -> singleReportGenerationSettings.setReportConditionalFormatting(
                        new ReportConditionalFormatting(
                                singleReportGenerationSettings.getReportMetaInfo().getSysName(), js
                        )
                ));
        return singleReportGenerationSettings;
    }
}