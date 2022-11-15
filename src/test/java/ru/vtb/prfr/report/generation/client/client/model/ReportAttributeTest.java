package ru.vtb.prfr.report.generation.client.client.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
class ReportAttributeTest {

    @Test
    void toParsedValueFromSingleSimpleProperty() {
        List<JsonPropertyPathElement> jsonPropertyPathElements = getJsonPropertyPathFromInput("SomeProperty");

        Assertions.assertThat(jsonPropertyPathElements).hasSize(1);
        Assertions.assertThat(jsonPropertyPathElements.get(0)).isEqualTo(new JsonPropertyPathElement("SomeProperty").isArrayIndex(false));
    }

    @Test
    void toParsedValueFromThreeSimpleProperties() {
        List<JsonPropertyPathElement> jsonPropertyPathElements = getJsonPropertyPathFromInput("someProperty/anotherProperty/thirdProperty");

        Assertions.assertThat(jsonPropertyPathElements).hasSize(3);
        Assertions.assertThat(jsonPropertyPathElements.get(0)).isEqualTo(new JsonPropertyPathElement("someProperty").isArrayIndex(false));
        Assertions.assertThat(jsonPropertyPathElements.get(1)).isEqualTo(new JsonPropertyPathElement("anotherProperty").isArrayIndex(false));
        Assertions.assertThat(jsonPropertyPathElements.get(2)).isEqualTo(new JsonPropertyPathElement("thirdProperty").isArrayIndex(false));
    }

    @Test
    void toParsedValueFromFourProperties() {
        List<JsonPropertyPathElement> jsonPropertyPathElements = getJsonPropertyPathFromInput("someProperty/anotherProperty/[0]/thirdProperty/[1]/lastOne");

        Assertions.assertThat(jsonPropertyPathElements).hasSize(6);
        Assertions.assertThat(jsonPropertyPathElements.get(0)).isEqualTo(new JsonPropertyPathElement("someProperty").isArrayIndex(false));
        Assertions.assertThat(jsonPropertyPathElements.get(1)).isEqualTo(new JsonPropertyPathElement("anotherProperty").isArrayIndex(false));
        Assertions.assertThat(jsonPropertyPathElements.get(2)).isEqualTo(new JsonPropertyPathElement("[0]").isArrayIndex(true));
        Assertions.assertThat(jsonPropertyPathElements.get(3)).isEqualTo(new JsonPropertyPathElement("thirdProperty").isArrayIndex(false));
        Assertions.assertThat(jsonPropertyPathElements.get(4)).isEqualTo(new JsonPropertyPathElement("[1]").isArrayIndex(true));
        Assertions.assertThat(jsonPropertyPathElements.get(5)).isEqualTo(new JsonPropertyPathElement("lastOne").isArrayIndex(false));
    }

    @Test
    void toParsedOnlyArray() {
        List<JsonPropertyPathElement> jsonPropertyPathElements = getJsonPropertyPathFromInput("someProperty/[1]");

        Assertions.assertThat(jsonPropertyPathElements.size()).isEqualTo(2);
        Assertions.assertThat(jsonPropertyPathElements.get(0)).isEqualTo(new JsonPropertyPathElement("someProperty").isArrayIndex(false));
        Assertions.assertThat(jsonPropertyPathElements.get(1)).isEqualTo(new JsonPropertyPathElement("[1]").isArrayIndex(true));
    }

    @Test
    void testArrayIndexesValidAllocation() {
        List<JsonPropertyPathElement> jsonPropertyPathElementFromInput = getJsonPropertyPathFromInput("first/field/[6]/third/[14]");

        Assertions.assertThat(jsonPropertyPathElementFromInput).hasSize(5);
        Assertions.assertThat(jsonPropertyPathElementFromInput.get(0).isArrayIndex()).isFalse();
        Assertions.assertThat(jsonPropertyPathElementFromInput.get(1).isArrayIndex()).isFalse();
        Assertions.assertThat(jsonPropertyPathElementFromInput.get(2).isArrayIndex()).isTrue();
        Assertions.assertThat(jsonPropertyPathElementFromInput.get(3).isArrayIndex()).isFalse();
        Assertions.assertThat(jsonPropertyPathElementFromInput.get(4).isArrayIndex()).isTrue();
    }

    @Test
    void testStartingSlashMakesNoDifference() {
        List<JsonPropertyPathElement> first = getJsonPropertyPathFromInput("/first/field/[6]/third/[14]");
        List<JsonPropertyPathElement> second = getJsonPropertyPathFromInput("first/field/[6]/third/[14]");

        Assertions.assertThat(first).isEqualTo(second);
    }

    @Test
    void getArrayIndexTest() {
        List<JsonPropertyPathElement> jsonPropertyPathElementFromInput = getJsonPropertyPathFromInput("first/field/[6]/third/[14]");
        Assertions.assertThat(jsonPropertyPathElementFromInput.get(2).getArrayIndexValue()).isEqualTo(6);
        Assertions.assertThat(jsonPropertyPathElementFromInput.get(4).getArrayIndexValue()).isEqualTo(14);
        Assertions.assertThatThrownBy(() -> jsonPropertyPathElementFromInput.get(1).getArrayIndexValue()).isInstanceOf(IllegalStateException.class);
    }

    private static List<JsonPropertyPathElement> getJsonPropertyPathFromInput(String valueFrom) {
        ReportAttribute reportAttribute = new ReportAttribute();
        reportAttribute.setValueFrom(valueFrom);
        return reportAttribute.toParsedValueFrom();
    }
}