package ru.vtb.prfr.report.generation.client.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import ru.vtb.prfr.report.generation.client.client.model.SingleReportGenerationSettings;
import ru.vtb.prfr.report.generation.client.exceptions.client.InvalidConfigurationException;
import ru.vtb.prfr.report.generation.client.exceptions.internal.InternalFatalException;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.BooleanUtils;

@Component
public class PrfrJsonOperator {

    private final ObjectReader objectReader;
    private final ObjectMapper objectMapper;

    public PrfrJsonOperator() {
        this.objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        this.objectReader = objectMapper.reader();
    }

    public SingleReportGenerationSettings parseJsonFileToObject(Resource configurationFile) {
        try {
            return objectReader.readValue(configurationFile.getInputStream(), SingleReportGenerationSettings.class);
        } catch (IOException e) {
            try {
                throw new InvalidConfigurationException(String.format("Config file, located in the classpath : '%s' is not a valid json", configurationFile.getFile().getAbsolutePath()), e);
            } catch (IOException ex) {
                throw new InternalFatalException(ex);
            }
        }
    }

    public Map<String, Object> fromJsonNode(JsonNode universalJson) {
        return objectMapper.convertValue(universalJson, new TypeReference<Map<String, Object>>() {
        });
    }

    /**
     * Returns the integer value of the node or the default integer value or null if no values found
     *
     * @param node         - JsonNode with value
     * @param defaultValue - default value as String
     * @return - Node value or default value or null
     */
    public static Integer getOrDefaultInteger(JsonNode node, String defaultValue) {
        return Optional.ofNullable(node)
                .map(JsonNode::intValue)
                .orElse(NumberUtils.createInteger(defaultValue));
    }

    /**
     * Returns the double value of the node or the default double value or null if no values found
     *
     * @param node         - JsonNode with value
     * @param defaultValue - default value as String
     * @return - Node value or default value or null
     */
    public static Double getOrDefaultDouble(JsonNode node, String defaultValue) {
        return Optional.ofNullable(node)
                .map(JsonNode::doubleValue)
                .orElse(NumberUtils.createDouble(defaultValue));
    }

    /**
     * Returns the boolean value of the node or the default boolean value or null if no values found
     *
     * @param node         - JsonNode with value
     * @param defaultValue - default value as String
     * @return - Node value or default value or null
     */
    public static Boolean getOrDefaultBoolean(JsonNode node, String defaultValue) {
        return Optional.ofNullable(node)
                .map(JsonNode::booleanValue)
                .orElse(BooleanUtils.toBooleanObject(defaultValue));
    }

    /**
     * Returns the string value of the node or the default value or null if no values found
     *
     * @param node         - JsonNode with value
     * @param defaultValue - default value as String
     * @return - Node value or default value or null
     */
    public static String getOrDefaultString(JsonNode node, String defaultValue) {
        return Optional.ofNullable(node)
                .map(JsonNode::asText)
                .orElse(defaultValue);
    }

}
