package ru.vtb.prfr.report.generation.client.client.model;

import org.springframework.util.Assert;

import java.util.Objects;

/**
 * Abstraction over Json property. Provides a convenient operation over json property.
 *
 * @see ReportAttribute#toParsedValueFrom()
 * @author Mikhail Polivakha
 */
public class JsonPropertyPathElement {

    /**
     * Either the name of the property or index of the element in array;
     */
    private String value;
    private Boolean isArray;

    public JsonPropertyPathElement(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Integer getArrayIndexValue() {
        Assert.notNull(value, "value on this stage must not be null, please, report to developers");

        if (isArrayIndex()) {
            return Integer.parseInt(value.substring(1, value.length() - 1));
        } else {
            throw new IllegalStateException("Cannot get array index value, because current value is the name of the json property, not an array index");
        }
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean isArrayIndex() {
        return isArray;
    }

    public Boolean isWildCard() {
        return "[*]".equals(value);
    }

    public JsonPropertyPathElement isArrayIndex(Boolean array) {
        this.isArray = array;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JsonPropertyPathElement that = (JsonPropertyPathElement) o;
        return value.equals(that.value) && isArray.equals(that.isArray);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, isArray);
    }

    @Override
    public String toString() {
        return "JsonPropertyPath{" +
                "name='" + value + '\'' +
                ", isArray=" + isArray +
                '}';
    }
}