package ru.vtb.prfr.report.generation.client.utils;

import com.fasterxml.jackson.databind.JsonNode;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.skyscreamer.jsonassert.JSONAssert;
import ru.vtb.prfr.report.generation.client.client.model.ReportAttribute;

import java.io.IOException;
import java.util.Map;


class AttributesValuesResolverTest {
    @ParameterizedTest
    @CsvFileSource(resources = "/data/attribute-resolver/attribute-resolver-data-list.csv", numLinesToSkip = 1, delimiter = ';')
    void attributeResolvingTest(String inputValues, String inputAttributes, String output) throws IOException, JSONException {
        // given
        JsonNode values = ResourceReader.readDataJson("data/attribute-resolver/" + inputValues);
        Map<String, ReportAttribute> attributes = ResourceReader.readAttributes("data/attribute-resolver/" + inputAttributes);
        Map<String, Object> expectedResult = ResourceReader.readBookmarks("data/attribute-resolver/" + output);
        AttributesValuesResolver underTest = new AttributesValuesResolver();

        // when
        Map<String, Object> result = underTest.resolveValues(values, attributes);
        ObjectMapper mapper = new ObjectMapper();
        String expectedResulJson = mapper.writeValueAsString(expectedResult);
        String resulJson = mapper.writeValueAsString(result);
        // then
        JSONAssert.assertEquals(expectedResulJson, resulJson, false);
    }

}