package ru.vtb.prfr.report.generation.client.client.model;

import com.fasterxml.jackson.annotation.JsonSetter;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ReportAttribute {

    private String sysName;

    private AttributeType type;
    private String title;
    private String description;
    private Boolean isRequired;
    private String defaultValue;
    private String regExpValidationPattern;
    private String operationName;

    /**
     * Holds the path to the property in the Universal Json, from which the value should be taken.
     * <p>
     * Example: root/nextElement/arrayElement/[1]
     * <p>
     * In that example, we have initial property 'root', which itself is an inner json object, then inside this we have another 'nextElement'
     * json object, and then in that json object there is an array property, called 'arrayElement', we need element in the index 1
     * <p>
     * {
     *     "root" : {
     *         "nextElement" : {
     *             "arrayElement" : [1, <b>6</b>, 8, 2, 5]
     *         }
     *     }
     * }
     */
    private String valueFrom;

    public String getSysName() {
        return sysName;
    }

    @JsonSetter("attribute_sys_name")
    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isRequired() {
        return isRequired;
    }

    public AttributeType getType() {
        return type;
    }

    public void setType(AttributeType type) {
        this.type = type;
    }

    @JsonSetter("is_required")
    public void setIsRequired(Boolean isRequired) {
        this.isRequired = isRequired;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    @JsonSetter("default_value")
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getRegExpValidationPattern() {
        return regExpValidationPattern;
    }

    @JsonSetter("regexp_validation_pattern")
    public void setRegExpValidationPattern(String regExpValidationPattern) {
        this.regExpValidationPattern = regExpValidationPattern;
    }

    public String getValueFrom() {
        return valueFrom;
    }

    @JsonSetter("value_from")
    public void setValueFrom(String valueFrom) {
        this.valueFrom = cleanUp(valueFrom);
    }

    public String getOperationName() {
        return operationName;
    }

    @JsonSetter("operation_name")
    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getValueSysName() {
        String from = getValueFrom();
        return from != null ? from : getSysName();
    }

    public List<JsonPropertyPathElement> toParsedValueFrom() {
        Assert.state(StringUtils.hasText(valueFrom), "'valueFrom' is empty for current report attribute. Should not be on this stage, report to developers");

        List<JsonPropertyPathElement> result = new ArrayList<>();

        String[] parts = this.valueFrom.split("/");

        if (parts.length == 1) {
            result.add(new JsonPropertyPathElement(parts[0]).isArrayIndex(false));
            return result;
        }

        for (String currentVal : parts) {
            JsonPropertyPathElement propertyPath = new JsonPropertyPathElement(currentVal).isArrayIndex(currentVal.contains("[") && currentVal.contains("]"));
            result.add(propertyPath);
        }
        return result;
    }

    private String cleanUp(String valueFrom) {
        if (valueFrom != null && valueFrom.startsWith("/")) {
            return valueFrom.substring(1);
        }
        return valueFrom;
    }

    @Override
    public String toString() {
        return "ReportAttribute{" +
                "sysName='" + sysName + '\'' +
                ", type=" + type +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", isRequired=" + isRequired +
                ", defaultValue='" + defaultValue + '\'' +
                ", valueFrom='" + valueFrom + '\'' +
                '}';
    }

}