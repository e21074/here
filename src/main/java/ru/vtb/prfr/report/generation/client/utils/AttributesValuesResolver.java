package ru.vtb.prfr.report.generation.client.utils;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import ru.vtb.prfr.report.generation.client.client.model.JsonPropertyPathElement;
import ru.vtb.prfr.report.generation.client.client.model.ReportAttribute;
import ru.vtb.prfr.report.generation.client.exceptions.internal.InternalFatalException;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Component resolving the attribute values from json tree
 */
@Component
public class AttributesValuesResolver {

    /**
     * Resolves all the attributes presented in reportAttributes list using the data from rootJsonDataNode.
     * The resulting map will contain all the attributes sysNames with corresponding values.
     *
     * @param rootJsonDataNode - arbitrarily nested json structure with initial data
     * @param reportAttributes - list of {@link ReportAttribute} objects to be resolved
     * @return - resulting map with attribute sysName as key and the resolved attribute value as value
     */
    public Map<String, Object> resolveValues(JsonNode rootJsonDataNode, Map<String, ReportAttribute> reportAttributes) {
        Map<String, Object> attributeNameAttributeValueMap = new HashMap<>();
        reportAttributes.forEach((attributeKey, attributeValue) -> {
            JsonNode currentNode = rootJsonDataNode;
            List<JsonPropertyPathElement> processedPath = new ArrayList<>();

            Iterator<JsonPropertyPathElement> pathIterator = attributeValue.toParsedValueFrom().iterator();
            while (pathIterator.hasNext() && currentNode != null) {
                JsonPropertyPathElement currentPathElement = pathIterator.next();
                if (currentPathElement.isArrayIndex()) {
                    if (currentPathElement.isWildCard()) {
                        List<JsonPropertyPathElement> remainingPath = new ArrayList<>();
                        pathIterator.forEachRemaining(remainingPath::add);
                        // collect all values in the array node with same sysName
                        List<Object> values = getTypedArrayNodeValues(currentNode, remainingPath, attributeValue);
                        // find corresponding attribute sysName by value sysName
                        String parentAttributeSysName = findAttributeNameByValueFrom(processedPath, reportAttributes);
                        // put all the array values in the map
                        attributeNameAttributeValueMap.putIfAbsent(parentAttributeSysName, new ArrayList<>());
                        fillListOfMapsByValues(attributeNameAttributeValueMap.get(parentAttributeSysName), attributeKey, values);
                        return;
                    } else {
                        // select the array element
                        currentNode = currentNode.get(currentPathElement.getArrayIndexValue() - 1);
                    }
                } else {
                    // select the next node
                    currentNode = currentNode.get(currentPathElement.getValue());
                }
                processedPath.add(currentPathElement);
            }
            Optional.ofNullable(getTypedNodeValue(currentNode, attributeValue))
                    .ifPresent(value -> attributeNameAttributeValueMap.put(attributeKey, value));
        });
        return attributeNameAttributeValueMap;
    }

    /**
     * Resolves the value presented by valuePath for each of the array element in the arrayNode
     *
     * @param arrayNode - JsonNode containing the array
     * @param valuePath - path to the value inside the array
     * @param attribute - {@link ReportAttribute} of the value
     * @return - list of the resolved values
     */
    private List<Object> getTypedArrayNodeValues(JsonNode arrayNode, List<JsonPropertyPathElement> valuePath, ReportAttribute attribute) {
        List<Object> values = new ArrayList<>();
        arrayNode.elements().forEachRemaining(arrayElementNode -> {
            JsonNode currentNode = arrayElementNode;
            for (JsonPropertyPathElement pathElement : valuePath) {
                if (pathElement.isArrayIndex()) {
                    currentNode = currentNode.get(pathElement.getArrayIndexValue() - 1);
                } else {
                    currentNode = currentNode.get(pathElement.getValue());
                }
            }
            Optional.ofNullable(getTypedNodeValue(currentNode, attribute))
                    .ifPresent(values::add);
        });
        return values;
    }

    /**
     * Finds the corresponding attribute sysName by the valueFrom field
     *
     * @param valueFromPath    - list of {@link JsonPropertyPathElement} to join into valueFrom string
     * @param reportAttributes - list of the report attributes
     * @return - sysName of the found attribute
     */
    private String findAttributeNameByValueFrom(List<JsonPropertyPathElement> valueFromPath, Map<String, ReportAttribute> reportAttributes) {
        String valueFrom = valueFromPath.stream()
                .map(JsonPropertyPathElement::getValue)
                .collect(Collectors.joining("/"));
        return reportAttributes.entrySet()
                .stream()
                .filter(attribute -> attribute.getValue().getValueFrom().equals(valueFrom))
                .findFirst()
                .map(Map.Entry::getKey)
                .orElseThrow(InternalFatalException::new);
    }

    /**
     * Fills the existing list by maps with attributeSysName as key and each of values as value.
     * Creates new maps if not exists. Adds new values for existing maps.
     *
     * @param objectToFill     - {@link List} object to fill the maps
     * @param attributeSysName - sysName of the attribute
     * @param values           - {@link List} of values to fill into the maps
     */
    @SuppressWarnings("unchecked")
    private static void fillListOfMapsByValues(Object objectToFill, String attributeSysName, List<Object> values) {
        List<Map<String, Object>> listOfMaps = (List<Map<String, Object>>) objectToFill;
        AtomicInteger index = new AtomicInteger(0);
        values.forEach(value -> {
            if (index.get() > listOfMaps.size() - 1) {
                listOfMaps.add(new HashMap<>());
            }
            listOfMaps.get(index.getAndIncrement()).putIfAbsent(attributeSysName, value);
        });
    }

    /**
     * Casts JsonNode to the value type specified in ReportAttribute.
     * Sets the default value specified in ReportAttribute if the node is null.
     *
     * @param node      - {@link JsonNode} to get value
     * @param attribute - {@link ReportAttribute} type
     * @return - Optional object with node value or default value with necessary type or Optional.empty()
     */
    private static Object getTypedNodeValue(JsonNode node, ReportAttribute attribute) {
        switch (attribute.getType()) {
            case INTEGER:
                return PrfrJsonOperator.getOrDefaultInteger(node, attribute.getDefaultValue());
            case NUMBER:
                return PrfrJsonOperator.getOrDefaultDouble(node, attribute.getDefaultValue());
            case BOOLEAN:
                return PrfrJsonOperator.getOrDefaultBoolean(node, attribute.getDefaultValue());
            case STRING:
                return PrfrJsonOperator.getOrDefaultString(node, attribute.getDefaultValue());
            default:
                return null;
        }
    }

}