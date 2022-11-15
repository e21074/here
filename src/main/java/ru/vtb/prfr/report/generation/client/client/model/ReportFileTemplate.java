package ru.vtb.prfr.report.generation.client.client.model;

import org.springframework.core.io.Resource;

public class ReportFileTemplate {

    private final String reportSysName;

    private final Resource fileTemplate;

    public ReportFileTemplate(String reportSysName, Resource fileTemplate) {
        this.reportSysName = reportSysName;
        this.fileTemplate = fileTemplate;
    }

    public String getReportSysName() {
        return reportSysName;
    }

    public Resource getFileTemplate() {
        return fileTemplate;
    }
}