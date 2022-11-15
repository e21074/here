package ru.vtb.prfr.report.generation.client.client.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.Collections;
import java.util.Map;

/**
 * Summed up report generation settings
 *
 * @author Mikhail Polivakha
 */
public class SingleReportGenerationSettings {

    private ReportMetaInfo reportMetaInfo;

    /**
     * The key is an object name in JSON, and the value is the structure of this object.
     * Example below
     *
     * "Guarantee_Sex": {
     *     "value_from": "guarantees/[*]/guaranteeSex",
     *     "type": "STRING",
     *     "is_required": false,
     *     "default_value": null,
     *     "title": "Пол поручителя/супруга",
     *     "description": null,
     *     "regexp_validation_pattern": null,
     *     "operation_name": null
     *   }
     *
     *   Guarantee_Sex is key
     *   and the structure of Guarantee_Sex is value (ReportAttribute)
     */
    private Map<String, ReportAttribute> reportAttributes;

    /**
     * This property will be set using content of separate from report_attributes.json configuration file
     */
    @JsonIgnore
    private ReportConditionalFormatting reportConditionalFormatting;

    /**
     * This property will be set using content of separate from report_attributes.json configuration file
     */
    @JsonIgnore
    private ReportFileTemplate reportFileTemplate;

    public ReportMetaInfo getReportMetaInfo() {
        return reportMetaInfo;
    }

    @JsonSetter("report")
    public void setReportMetaInfo(ReportMetaInfo reportMetaInfo) {
        this.reportMetaInfo = reportMetaInfo;
    }

    public Map<String, ReportAttribute> getReportAttributes() {
        return reportAttributes;
    }

    @JsonSetter("attributes")
    public void setReportAttributes(Map<String, ReportAttribute> reportAttributes) {
        this.reportAttributes = reportAttributes != null ? reportAttributes : Collections.emptyMap();
    }

    public ReportConditionalFormatting getReportConditionalFormatting() {
        return reportConditionalFormatting;
    }

    public void setReportConditionalFormatting(ReportConditionalFormatting reportConditionalFormatting) {
        this.reportConditionalFormatting = reportConditionalFormatting;
    }

    @Override
    public String toString() {
        return "ReportGenerationSettings{" +
                "reportMetaInfo=" + reportMetaInfo +
                ", reportAttributes=" + reportAttributes +
                ", reportConditionalFormatting=" + reportConditionalFormatting +
                '}';
    }

    public void setReportFileTemplate(ReportFileTemplate reportFileTemplate) {
        this.reportFileTemplate = reportFileTemplate;
    }

    public ReportFileTemplate getReportFileTemplate() {
        return reportFileTemplate;
    }
}