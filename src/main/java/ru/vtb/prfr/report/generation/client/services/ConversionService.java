package ru.vtb.prfr.report.generation.client.services;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.expression.Expression;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.vtb.msa.pdoc.convert.unijson.api.model.PoiJSON;
import ru.vtb.msa.pdoc.convert.unijson.api.model.PoiJSONBarcodes;
import ru.vtb.prfr.report.generation.client.client.model.*;
import ru.vtb.prfr.report.generation.client.utils.AttributesValuesResolver;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConversionService {
    private final Pattern setCellTextPattern = Pattern.compile("setCellText:\\d+;\\d+;\\d+;(true|false);\\[(\\d+\\.\\d+)]");

    private final EvaluationService evaluationService;
    private final AttributesValuesResolver attributesValuesResolver;
    private final ConditionalFormattingService conditionalFormattingService;

    /**
     * Generates PoiJson object with necessary data for report generation
     *
     * @param reportGenerationSettings - object with all necessary template and attributes data
     * @param rootJsonDataNode         - user data for report generation
     * @param facsimile                - facsimile data
     * @return - PoiJson object
     */
    public PoiJSON generatePoiJson(SingleReportGenerationSettings reportGenerationSettings,
                                   JsonNode rootJsonDataNode,
                                   ReportGroupFacsimile facsimile) {
        String javaScriptCode = getJavaScriptConditionalFormattingCode(reportGenerationSettings);
        ConditionalFormatting formatting = conditionalFormattingService.evalConditionalFormatting(javaScriptCode, rootJsonDataNode);

        PoiJSON poiJSON = new PoiJSON();
        poiJSON.setBookmarksOperation(formatting.getBookmarksOperation());

        poiJSON.setBookmarks(
                aggregateBookmarks(
                        rootJsonDataNode,
                        reportGenerationSettings.getReportAttributes(),
                        formatting.getAdditionallyProvidedBookmarks()
                )
        );

        StandardEvaluationContext evaluationContext = getStandardEvaluationContext(poiJSON.getBookmarks());
        poiJSON.setIndexedTables(resolveVariablesList(formatting.getIndexedTables(), evaluationContext));
        poiJSON.setNamedTables(createNamedTables(formatting.getNamedTables(), evaluationContext));

        addFacsimile(formatting.getImages(), facsimile);

        poiJSON.setImages64(formatting.getImages());

        poiJSON.setBarcodes(createBarcodes(formatting.getBarcodes()));
        return poiJSON;
    }

    /**
     * Extracts conditional formatting script from report generation settings
     *
     * @param reportGenerationSettings - report generation settings
     * @return - JS code of the conditional formatting or null
     */
    private static String getJavaScriptConditionalFormattingCode(SingleReportGenerationSettings reportGenerationSettings) {
        return Optional.ofNullable(reportGenerationSettings.getReportConditionalFormatting())
                .map(ReportConditionalFormatting::getFormattingJavaScriptCode)
                .orElse(null);
    }

    /**
     * Creates map of bookmarks using json data, list of attributes and additional bookmarks from conditional formatting
     *
     * @param rootJsonDataNode                             - json data
     * @param reportAttributes                             - list of report attributes
     * @param additionalBookmarksFromConditionalFormatting - additional bookmarks from conditional formatting
     * @return - map of bookmarks
     */
    private Map<String, Object> aggregateBookmarks(JsonNode rootJsonDataNode,
                                                   Map<String, ReportAttribute> reportAttributes,
                                                   Map<String, Object> additionalBookmarksFromConditionalFormatting) {
        // resolve values tree
        Map<String, Object> attributeNameAttributeValueMap = attributesValuesResolver.resolveValues(rootJsonDataNode, reportAttributes);
        // apply conditional formatting bookmarks
        additionalBookmarksFromConditionalFormatting.entrySet()
                .stream()
                .filter(e -> e.getValue() != null)
                .forEach(e -> attributeNameAttributeValueMap.put(e.getKey(), e.getValue()));
        // apply attribute operations
        evaluationService.applyOperations(attributeNameAttributeValueMap, reportAttributes);
        return attributeNameAttributeValueMap;
    }

    /**
     * Resolves all table instructions for indexed tables based on the SPEL expressions and data in evaluation context
     * @param indexedTables - list of table instructions
     * @param evaluationContext - evaluation context data
     * @return - list of resolved table instructions
     */
    private List<String> resolveVariablesList(List<String> indexedTables, StandardEvaluationContext evaluationContext) {
        return indexedTables.stream()
                .map(operation -> resolveVariables(operation, evaluationContext))
                .collect(Collectors.toList());
    }

    /**
     * Resolves all table instructions for named tables based on the SPEL expressions and data in evaluation context
     * @param namedTables - map of table instructions
     * @param evaluationContext - evaluation context data
     * @return - map of resolved table instructions
     */
    private Map<String, List<String>> createNamedTables(Map<String, List<String>> namedTables, StandardEvaluationContext evaluationContext) {
        Map<String, List<String>> namedTablesResult = new HashMap<>(namedTables);
        namedTablesResult.replaceAll((k, v) -> resolveVariablesList(v, evaluationContext));
        return namedTablesResult;
    }

    /**
     * Encodes provided facsimile by base64 encoder and adds it into the images map
     *
     * @param conditionalFormattingImages - map of base64 encoded images
     * @param facsimile                   - facsimile to add
     */
    private void addFacsimile(Map<String, String> conditionalFormattingImages, ReportGroupFacsimile facsimile) {
        if (conditionalFormattingImages.containsKey("facsimile") &&
                facsimile != null &&
                facsimile.getFacsimile() != null &&
                facsimile.getFacsimile().length > 0) {
            String encodedString = Base64.getEncoder().encodeToString(facsimile.getFacsimile());
            conditionalFormattingImages.put("facsimile", encodedString);
        }
    }

    /**
     * Converts map of barcodes data into PoiJson format
     *
     * @param barcodes - barcodes data
     * @return - barcodes data in PoiJson format
     */
    private Map<String, PoiJSONBarcodes> createBarcodes(Map<String, Map<String, String>> barcodes) {
        return barcodes.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> new PoiJSONBarcodes()
                                .format(e.getValue().get("format"))
                                .value(e.getValue().get("value"))
                ));
    }

    /**
     * Creates SPEL evaluation context with data assigned to it
     *
     * @param data - data for context
     * @return - context object
     */
    private StandardEvaluationContext getStandardEvaluationContext(Map<String, Object> data) {
        StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
        evaluationContext.setVariables(data);
        return evaluationContext;
    }

    /**
     * Resolves all variables presented in the value based on the SPEL expressions and data in evaluation context
     * @param value - value to parse
     * @param context - evaluation context data
     * @return - resolved value
     */
    private String resolveVariables(String value, StandardEvaluationContext context) {
        if (!StringUtils.hasLength(value)) {
            return value;
        }

        if (value.contains("#{#")) {
            Expression expression = new SpelExpressionParser().parseExpression(value, new TemplateParserContext());
            value = (String) expression.getValue(context);
        }

        if (!StringUtils.hasLength(value)) {
            return value;
        }

        Matcher setCellMatcher = setCellTextPattern.matcher(value);
        if (setCellMatcher.matches()) {
            // Если в операции TextValue - это число, то применить к этому значению операцию NUMBER_FORMAT
            // Пример значения: "setCellText:idTable;idRow;idCol;CopyStyleBefore;[TextValue]"
            return new StringBuilder(value)
                    .replace(setCellMatcher.start(2), setCellMatcher.end(2), evaluationService.formatDouble(setCellMatcher.group(2)))
                    .toString();
        }

        if (value.startsWith("setRowText:")) {
            // Если в операции TextValue - это массив элементов, среди которых есть числа, то применить к этим числам операцию NUMBER_FORMAT
            // Пример значения: "setRowText:idTable;idRow;CopyStyleBefore;[cell1Text;cell2Text;...]"
            // Пример значения: "setRowText:1;2;true;[Text value;10.1;1002.2;Text value]" -> "setRowText:1;2;true;[Text value;10,10;1 002,20;Text value]"
            String operationElements = value.substring(value.indexOf("[") + 1, value.indexOf("]"));
            String formattedElements = Arrays.stream(operationElements.split(";"))
                    .map(element -> {
                        if (NumberUtils.isCreatable(element) && !NumberUtils.isDigits(element)) {
                            return evaluationService.formatDouble(element);
                        }
                        return element;
                    })
                    .collect(Collectors.joining(";"));
            return value.substring(0, value.indexOf("[") + 1) + formattedElements + "]";
        }

        return value;
    }
}
