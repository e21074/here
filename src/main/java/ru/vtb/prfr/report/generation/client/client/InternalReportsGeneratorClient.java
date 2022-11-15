package ru.vtb.prfr.report.generation.client.client;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.vtb.msa.pdoc.convert.unijson.api.model.PoiJSON;
import ru.vtb.prfr.report.generation.client.client.config.AggregatedReportsInfo;
import ru.vtb.prfr.report.generation.client.client.model.ReportGroupFacsimile;
import ru.vtb.prfr.report.generation.client.client.model.SingleReportGenerationSettings;
import ru.vtb.prfr.report.generation.client.client.model.exception.ValidationExceptionProfile;
import ru.vtb.prfr.report.generation.client.exceptions.client.InvalidClientInputException;
import ru.vtb.prfr.report.generation.client.exceptions.internal.InternalFatalException;
import ru.vtb.prfr.report.generation.client.internal.service.validation.ClientDataValidator;
import ru.vtb.prfr.report.generation.client.services.ConversionService;
import ru.vtb.prfr.report.generation.client.services.DocGenerationClientService;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * Implementation, that is Used to generate the report inside the current K8S cluster, meaning,
 * that the report will be generated without any request to "PRFR-1875 ОПС".
 *
 * @author Mikhail Polivakha
 */
@Slf4j
@Component
public class InternalReportsGeneratorClient implements ReportsGeneratorClient {

    @Autowired
    private ClientDataValidator clientDataValidator;

    @Autowired
    private ConversionService conversionService;

    @Autowired
    private DocGenerationClientService docGenerationClientService;

    @Autowired
    private AggregatedReportsInfo aggregatedReportsInfo;

    @Value("${report.output.directory:/prfr/output/}")
    private String outputDirectory;

    @PostConstruct
    public void init() throws IOException {
        log.info("Ensure output directory for generated report files exists : {}", outputDirectory);
        if (!Files.exists(Paths.get(outputDirectory))) {
            Files.createDirectories(Paths.get(outputDirectory));
        }
    }

    @Override
    public Path generatePDF(@NonNull String reportSysName,
                            @NonNull JsonNode rootJsonDataNode,
                            @NonNull String pdfFormat) {
        return generateReport(reportSysName, rootJsonDataNode, PoiJSON.TargetFormatEnum.PDF, pdfFormat);
    }

    @Override
    public Path generateWord(@NonNull String reportSysName,
                             @NonNull JsonNode rootJsonDataNode) {
        return generateReport(reportSysName, rootJsonDataNode, PoiJSON.TargetFormatEnum.DOCX, null);
    }

    private Path generateReport(String reportSysName, JsonNode rootJsonDataNode, PoiJSON.TargetFormatEnum targetFormatEnum, String pdfFormat) {
        // load report generation settings
        SingleReportGenerationSettings reportGenerationSettings = aggregatedReportsInfo.getReportGenerationSettingsByReportName(reportSysName);
        // validate client request data
        ValidationExceptionProfile validationExceptionProfile = clientDataValidator
                .validateClientRequest(reportGenerationSettings, rootJsonDataNode);
        if (!validationExceptionProfile.noErrorFound()) {
            throw new InvalidClientInputException(validationExceptionProfile);
        }

        ReportGroupFacsimile facsimile = aggregatedReportsInfo.getFacsimileByReportName(reportSysName);
        // generate data tree for document generation
        PoiJSON poiJSON = conversionService.generatePoiJson(reportGenerationSettings, rootJsonDataNode, facsimile);
        // add generated document format and filename
        poiJSON.setTargetName(reportGenerationSettings.getReportMetaInfo().getGeneratedFileName());
        poiJSON.setTargetFormat(targetFormatEnum);
        poiJSON.setPdfFormat(pdfFormat);
        // generate and write target document
        try {
            InputStream inputStream = docGenerationClientService.generateReport(
                    aggregatedReportsInfo.getTemplateFileByReportSysName(reportSysName).getInputStream(),
                    poiJSON
            );
            Path file = Files.createFile(createPathForGeneratedReport(reportSysName, targetFormatEnum));
            return Files.write(file, IOUtils.toByteArray(inputStream));
        } catch (IOException e) {
            throw new InternalFatalException(e);
        }
    }

    private Path createPathForGeneratedReport(String reportSysName, PoiJSON.TargetFormatEnum targetFormatEnum) {
        return Paths.get(
                outputDirectory + File.separator +
                        aggregatedReportsInfo.getReportGenerationSettingsByReportName(reportSysName)
                                .getReportMetaInfo().getGeneratedFileName() +
                        UUID.randomUUID() +
                        "." + targetFormatEnum.getValue()
        );
    }
}
