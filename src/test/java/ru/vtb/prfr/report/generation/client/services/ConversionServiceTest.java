package ru.vtb.prfr.report.generation.client.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.vtb.msa.pdoc.convert.unijson.api.model.PoiJSON;
import ru.vtb.prfr.report.generation.client.utils.ResourceReader;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ConversionServiceTest {

    @Autowired
    EvaluationService evaluationService;

    @Autowired
    ConditionalFormattingService conditionalFormattingService;

    @Autowired
    ConversionService conversionService;

    @Test
    void testConditionalFormattingWithBookmarkOperations() throws Exception {

        PoiJSON poiJSON = conversionService.generatePoiJson(
                ResourceReader.readReportWithConditionalFormatting(
                        "reports/testConditionalFormattingWithBookmarkOperations.json",
                        "js/testConditionalFormattingWithBookmarkOperations.js"
                ),
                ResourceReader.readDataJson("data/testConditionalFormattingWithBookmarkOperations.json"),
                null
        );

        Map<String, Object> bookmarks = poiJSON.getBookmarks();
        assertEquals("202 (Двести два) месяца", bookmarks.get("creditTerm"));
        assertEquals("11,26 (Одиннадцать целых двадцать шесть сотых) процентов", bookmarks.get("totalInterest"));
        assertEquals("11,02 (Одиннадцать целых две сотых) процентов", bookmarks.get("penalDamagesPercents"));

        assertEquals("Срок кредита в месяцах: 202 (Двести два) месяца", poiJSON.getIndexedTables().get(0));

        List<String> namedTablesOperations = poiJSON.getNamedTables().get("table1");
        assertEquals("11,02 (Одиннадцать целых две сотых) процентов годовых", namedTablesOperations.get(0));

        assertEquals("100,01 (Сто и 01/100)", bookmarks.get("newBookmarkRubleWordsLessJs"));
    }

    @Test
    void testBookmarksAndOperationsNumberFormat() throws IOException {
        PoiJSON poiJSON = conversionService.generatePoiJson(
                ResourceReader.readReportWithConditionalFormatting(
                        "reports/testBookmarksAndOperationsNumberFormat.json",
                        "js/testBookmarksAndOperationsNumberFormat.js"
                ),
                ResourceReader.readDataJson("data/testBookmarksAndOperationsNumberFormat.json"),
                null
        );

        Map<String, Object> bookmarks = poiJSON.getBookmarks();
        assertEquals(1002, bookmarks.get("decimal0"));
        assertEquals("100 000 002,10", bookmarks.get("decimal1"));
        assertEquals("1 002,12", bookmarks.get("decimal2"));
        assertEquals("1 002,123", bookmarks.get("decimal3"));
        assertEquals(2002, bookmarks.get("bookmarkDecimal0"));
        assertEquals("2 002,10", bookmarks.get("bookmarkDecimal1"));
        assertEquals("2 002,12", bookmarks.get("bookmarkDecimal2"));
        assertEquals("2 002,123", bookmarks.get("bookmarkDecimal3"));

        List<String> ops = poiJSON.getIndexedTables();
        String opsMessage = "IndexedTables: " + ops.stream().collect(Collectors.joining(", "));
        assertTrue(ops.contains("setCellText:1;2;3;true;[1 002,50]"), opsMessage);
        assertTrue(ops.contains("setRowText:1;2;false;[1 002,10;1200;1 002,12]"), opsMessage);
        assertTrue(ops.contains("setRowText:1;2;false;[3002;abc;3 002,12]"), opsMessage);
    }

    @Test
    void testArrayAttributes() throws IOException {
        Map<String, Object> expectedBookmarks = ResourceReader.readBookmarks("results/expectedBookmarks.json");

        PoiJSON poiJSON = conversionService.generatePoiJson(
                ResourceReader.readReport("reports/testArrayAttributes.json"),
                ResourceReader.readDataJson("data/testArrayAttributes.json"),
                null
        );

        List<Map> actualGuaranteesList = (List<Map>) poiJSON.getBookmarks().get("Guarantees_List");
        List<Map> expectedGuaranteesList = (List<Map>) expectedBookmarks.get("Guarantees_List");
        assertIterableEquals(expectedGuaranteesList, actualGuaranteesList, "with value " + poiJSON);
        String actualFirstGuaranteeFIO = (String) poiJSON.getBookmarks().get("First_Guarantee_FIO");
        String expectedFirstGuaranteeFIO = (String) expectedBookmarks.get("First_Guarantee_FIO");
        assertEquals(expectedFirstGuaranteeFIO, actualFirstGuaranteeFIO);
    }

    @Test
    public void testImagesInConditionalFormatting() throws IOException {
        PoiJSON poiJSON = conversionService.generatePoiJson(
                ResourceReader.readReportWithConditionalFormatting(
                        "reports/testImagesWithJSScript.json",
                        "js/testImagesWithJSScript.js"
                ),
                ResourceReader.readDataJson("data/testImagesInConditionalFormatting.json"),
                null
        );

        Map<String, String> images64 = poiJSON.getImages64();
        assertEquals("/image1formula", images64.get("image1"));
        assertEquals("/image2formula", images64.get("image2"));
    }
}