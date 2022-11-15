package ru.vtb.prfr.report.generation.client.client.model;

public class ReportConditionalFormatting {
    private final String reportSysName;
    private final String formattingJavaScriptCode;

    public ReportConditionalFormatting(String reportSysName, String formattingJavaScriptCode) {
        this.reportSysName = reportSysName;
        this.formattingJavaScriptCode = formattingJavaScriptCode;
    }

    public String getReportSysName() {
        return reportSysName;
    }

    public String getFormattingJavaScriptCode() {
        return formattingJavaScriptCode;
    }

    @Override
    public String toString() {
        return "ReportConditionalFormatting{" +
                "reportSysName='" + reportSysName + '\'' +
                '}';
    }
}