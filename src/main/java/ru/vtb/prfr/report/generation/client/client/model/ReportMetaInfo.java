package ru.vtb.prfr.report.generation.client.client.model;


import com.fasterxml.jackson.annotation.JsonSetter;

public class ReportMetaInfo {
    private String sysName;
    private String generatedFileName;
    private String description;
    private String reportGroup;

    public String getSysName() {
        return sysName;
    }

    @JsonSetter("report_sys_name")
    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getGeneratedFileName() {
        return generatedFileName;
    }

    @JsonSetter("file_label")
    public void setGeneratedFileName(String generatedFileName) {
        this.generatedFileName = generatedFileName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonSetter("report_group")
    public void setReportGroup(String reportGroup) {
        this.reportGroup = reportGroup;
    }

    public String getReportGroup() {
        return reportGroup;
    }

    @Override
    public String toString() {
        return "ReportMetaInfo{" +
                "sysName='" + sysName + '\'' +
                ", generatedFileName='" + generatedFileName + '\'' +
                ", description='" + description + '\'' +
                ", reportGroup='" + reportGroup + '\'' +
                '}';
    }
}