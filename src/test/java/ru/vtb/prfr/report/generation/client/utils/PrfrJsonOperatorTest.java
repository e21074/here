package ru.vtb.prfr.report.generation.client.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
class PrfrJsonOperatorTest {

    @Test
    void parseJsonFileToObject() {
        PrfrJsonOperator prfrJsonOperator = new PrfrJsonOperator();

        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode root = objectMapper.createObjectNode();
        root.put("field1", "value1");
        root.put("field2", "value2");

        ObjectNode childNode = objectMapper.createObjectNode();
        root.set("innerJson", childNode);

        childNode.put("innerField1", "valueOfInnerField1");
        childNode.put("innerField2", "valueOfInnerField2");

        Map<String, Object> universalJson = prfrJsonOperator.fromJsonNode(root);
        Assertions.assertThat(universalJson).hasSize(3);
        Assertions.assertThat(universalJson.get("innerJson")).isInstanceOf(Map.class);
        Assertions.assertThat(universalJson.get("field1")).isEqualTo("value1");
    }

    @Test
    void getOrDefaultIntegerWithNodeValue() {
        JsonNode node = new IntNode(42);
        String defaultValue = "33";

        Integer value = PrfrJsonOperator.getOrDefaultInteger(node, defaultValue);

        assertEquals(42, value);
    }

    @Test
    void getOrDefaultIntegerWithDefaultValue() {
        JsonNode node = null;
        String defaultValue = "33";

        Integer value = PrfrJsonOperator.getOrDefaultInteger(node, defaultValue);

        assertEquals(33, value);
    }

    @Test
    void getOrDefaultIntegerWithNull() {
        JsonNode node = null;
        String defaultValue = null;

        Integer value = PrfrJsonOperator.getOrDefaultInteger(node, defaultValue);

        assertNull(value);
    }

    @Test
    void getOrDefaultDoubleWithNodeValue() {
        JsonNode node = new DoubleNode(9.5);
        String defaultValue = "3.3";

        Double value = PrfrJsonOperator.getOrDefaultDouble(node, defaultValue);

        assertEquals(9.5, value);
    }

    @Test
    void getOrDefaultDoubleWithDefaultValue() {
        JsonNode node = null;
        String defaultValue = "3.3";

        Double value = PrfrJsonOperator.getOrDefaultDouble(node, defaultValue);

        assertEquals(3.3, value);
    }

    @Test
    void getOrDefaultDoubleWithNull() {
        JsonNode node = null;
        String defaultValue = null;

        Double value = PrfrJsonOperator.getOrDefaultDouble(node, defaultValue);

        assertNull(value);
    }

    @Test
    void getOrDefaultBooleanWithNodeValue() {
        JsonNode node = BooleanNode.getTrue();
        String defaultValue = "false";

        Boolean value = PrfrJsonOperator.getOrDefaultBoolean(node, defaultValue);

        assertEquals(true, value);
    }

    @Test
    void getOrDefaultBooleanWithDefaultValue() {
        JsonNode node = null;
        String defaultValue = "false";

        Boolean value = PrfrJsonOperator.getOrDefaultBoolean(node, defaultValue);

        assertEquals(false, value);
    }

    @Test
    void getOrDefaultBooleanWithNull() {
        JsonNode node = null;
        String defaultValue = null;

        Boolean value = PrfrJsonOperator.getOrDefaultBoolean(node, defaultValue);

        assertNull(value);
    }

    @Test
    void getOrDefaultStringWithNodeValue() {
        JsonNode node = new TextNode("node");
        String defaultValue = "default";

        String value = PrfrJsonOperator.getOrDefaultString(node, defaultValue);

        assertEquals("node", value);
    }

    @Test
    void getOrDefaultStringWithDefaultValue() {
        JsonNode node = null;
        String defaultValue = "default";

        String value = PrfrJsonOperator.getOrDefaultString(node, defaultValue);

        assertEquals("default", value);
    }

}