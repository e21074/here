package ru.vtb.prfr.report.generation.client.client.model;

/**
 * Enum representing all possible report attribute types
 *
 * @author Artur Kovin
 */
public enum AttributeType {
    /**
     * String literal
     */
    STRING("STRING"),
    /**
     * Number in decimal format. Could be with floating point. Parsed internally as Double
     */
    NUMBER("NUMBER"),
    /**
     * Integer number in decimal format. Parsed internally as Integer
     */
    INTEGER("INTEGER"),
    /**
     * Boolean value (true or false)
     */
    BOOLEAN("BOOLEAN"),
    /**
     * Array value. Attributes with this type represents the container bookmark for array of objects.
     * Each of these objects contain some other attributes.
     */
    ARRAY("ARRAY");

    private final String value;

    AttributeType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}