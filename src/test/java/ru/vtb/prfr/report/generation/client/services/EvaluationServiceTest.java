package ru.vtb.prfr.report.generation.client.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.vtb.prfr.report.generation.client.evaluation.impl.NumberWordsMaleOperation;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
class EvaluationServiceTest {

    @Autowired
    EvaluationService evaluationService;
    @Autowired
    NumberWordsMaleOperation numberWordsMaleOperation;

    @ParameterizedTest
    @CsvFileSource(resources = "/data/evaluation-string-data.csv", numLinesToSkip = 1, delimiter = ';')
    void stringOperationsTest(String operation, String input, String expected) {
        if (input == null) input = "";
        if (expected == null) expected = "";
        assertEquals(expected, evaluationService.eval(operation, input));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/data/evaluation-number-data.csv", numLinesToSkip = 1, delimiter = ';')
    void numberOperationsTest(String operation, String input, String expected) {
        if (input == null) input = "";
        if (expected == null) expected = "";
        assertEquals(expected, evaluationService.eval(operation, Double.valueOf(input)));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/data/evaluation-number-with-precision-data.csv", numLinesToSkip = 1, delimiter = ';')
    void numberWordsWithPrecisionTest(Integer precision, String input, String expected) {
        if (input == null) input = "";
        if (expected == null) expected = "";
        assertEquals(expected, numberWordsMaleOperation.eval(Double.valueOf(input), precision));
    }

    @Test
    void testUnknownOperation() {
        Assertions.assertThrows(
                IllegalStateException.class,
                () -> evaluationService.eval("unknownOperation_qwerty", Math.random())
        );
    }

    @Test
    void testNullOperation() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> evaluationService.eval(null, Math.random())
        );
    }

    @Test
    void testOperationWithNullValue() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> evaluationService.eval("numberWordsF", null)
        );
    }
}