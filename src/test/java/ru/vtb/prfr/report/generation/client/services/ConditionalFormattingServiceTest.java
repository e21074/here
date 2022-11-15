package ru.vtb.prfr.report.generation.client.services;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import ru.vtb.prfr.report.generation.client.client.model.ConditionalFormatting;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ConditionalFormattingServiceTest {

    @Autowired
    ConditionalFormattingService conditionalFormattingService;
    @Value("classpath:js/testOperationEvaluationInConditionalFormatting.js")
    private Resource conditionalFormattingFormula;

    @Test
    void testOperationEvaluationInConditionalFormatting() throws IOException {
        String script = IOUtils.toString(conditionalFormattingFormula.getInputStream(), StandardCharsets.UTF_8);
        Map<String, Object> nativeValues = new HashMap<>();
        nativeValues.put("interestWordsJava", 22);
        nativeValues.put("interestWordsJavaDecimal", 22.999999);
        nativeValues.put("dateWordsJava", "02.02.2019");
        nativeValues.put("monthWordsJava", 2);
        nativeValues.put("integerWordsJava", 1.9999);
        nativeValues.put("fractionWordsJava", 12.1);
        nativeValues.put("numberWordsMJava", 505);
        nativeValues.put("numberWordsFJava", 2);
        nativeValues.put("rubleWordsJava", 10454.34999);
        nativeValues.put("fractionIntegerJava", 1);
        nativeValues.put("fractionJava", 1.8);

        ConditionalFormatting res = conditionalFormattingService.evalConditionalFormatting(script, nativeValues);

        BiConsumer<String, String> assertEquals = (String expected, String field) ->
                assertEquals(expected, res.getBookmarks().get(field), "for field " + field);

        assertEquals.accept("11,00 (Одиннадцать) процентов", "interestWordsJs");
        assertEquals.accept("11,11 (Одиннадцать целых одиннадцать сотых) процентов", "interestWordsJsDecimal");
        assertEquals.accept("22,00 (Двадцать два) процента", "interestWordsJava");
        assertEquals.accept("22,99999 (Двадцать два целых девяносто девять тысяч девятьсот девяносто девять стотысячных) процента", "interestWordsJavaDecimal");

        assertEquals.accept("Одиннадцать процентов", "interestWordsShortJs");
        assertEquals.accept("Одиннадцать целых одиннадцать сотых процентов", "interestWordsShortJsDecimal");
        assertEquals.accept("Двадцать два процента", "interestWordsShortJava");
        assertEquals.accept("Двадцать два целых девяносто девять сотых процента", "interestWordsShortJavaDecimal");

        assertEquals.accept("«01» января 2020", "dateWordsJs");
        assertEquals.accept("«02» февраля 2019", "dateWordsJava");
        assertEquals.accept("1 (Один) месяц", "monthWordsJs");
        assertEquals.accept("2 (Два) месяца", "monthWordsJava");
        assertEquals.accept("Один месяц", "monthWordsShortJs");
        assertEquals.accept("Два месяца", "monthWordsShortJava");

        assertEquals.accept("0 (Ноль)", "integerWordsJs");
        assertEquals.accept("1 (Один)", "integerWordsJava");
        assertEquals.accept("0 (Ноль)", "fractionWordsJs");
        assertEquals.accept("10 (Десять)", "fractionWordsJava");

        assertEquals.accept("9,99999 (Девять целых девяносто девять тысяч девятьсот девяносто девять стотысячных)", "numberWordsMJs");
        assertEquals.accept("505 (Пятьсот пять)", "numberWordsMJava");
        assertEquals.accept("1,99999 (Одна целая девяносто девять тысяч девятьсот девяносто девять стотысячных)", "numberWordsFJs");
        assertEquals.accept("2 (Две)", "numberWordsFJava");

        assertEquals.accept("9,9 (Девять целых девять десятых)", "numberWordsMJs1");
        assertEquals.accept("505,0 (Пятьсот пять)", "numberWordsMJava1");
        assertEquals.accept("9,999 (Девять целых девятьсот девяносто девять тысячных)", "numberWordsMJs3");
        assertEquals.accept("505,000 (Пятьсот пять)", "numberWordsMJava3");
        assertEquals.accept("9,99999 (Девять целых девяносто девять тысяч девятьсот девяносто девять стотысячных)", "numberWordsMJs5");
        assertEquals.accept("505,00000 (Пятьсот пять)", "numberWordsMJava5");

        assertEquals.accept("10 454,00 (Десять тысяч четыреста пятьдесят четыре и 00/100) рубля", "rubleWordsJs");
        assertEquals.accept("10 454,34 (Десять тысяч четыреста пятьдесят четыре и 34/100) рубля", "rubleWordsJava");
        assertEquals.accept("Десять тысяч четыреста пятьдесят четыре и 00/100 рубля", "rubleWordsShortJs");
        assertEquals.accept("Десять тысяч четыреста пятьдесят четыре и 34/100 рубля", "rubleWordsShortJava");
        assertEquals.accept("10 454,00 (Десять тысяч четыреста пятьдесят четыре и 00/100)", "rubleWordsLessJs");
        assertEquals.accept("10 454,34 (Десять тысяч четыреста пятьдесят четыре и 34/100)", "rubleWordsLessJava");

        assertEquals.accept("1", "fractionIntegerJs");
        assertEquals.accept("1", "fractionIntegerJava");
        assertEquals.accept("1", "fractionIntegerToDoubleJava");
        assertEquals.accept("2 1/2000", "fractionJs");
        assertEquals.accept("1 4/5", "fractionJava");
    }

}