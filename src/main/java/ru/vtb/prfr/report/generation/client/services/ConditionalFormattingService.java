package ru.vtb.prfr.report.generation.client.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.proxy.ProxyObject;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.vtb.prfr.report.generation.client.client.model.ConditionalFormatting;
import ru.vtb.prfr.report.generation.client.evaluation.Evaluator;
import ru.vtb.prfr.report.generation.client.exceptions.client.InvalidConfigurationException;
import ru.vtb.prfr.report.generation.client.utils.PrfrJsonOperator;

import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConditionalFormattingService {
    private static final String LANG_JS = "js";

    private final Map<String, Evaluator> operations;
    private final PrfrJsonOperator prfrJsonOperator;
    private final ObjectMapper objectMapper;

    /**
     * Applies provided JS code to the provided data using the GraalVM Polyglot engine
     * @param jsCode - JavaScript code to be applied
     * @param rootJsonDataNode - the initial data to process
     * @return - {@link ConditionalFormatting} object containing the results of processing the data
     */
    public ConditionalFormatting evalConditionalFormatting(String jsCode, JsonNode rootJsonDataNode) {
        Map<String, Object> initialValuesMap = prfrJsonOperator.fromJsonNode(rootJsonDataNode);
        return evalConditionalFormatting(jsCode, initialValuesMap);
    }

    /**
     * Applies provided JS code to the provided data using the GraalVM Polyglot engine
     * @param jsCode - JavaScript code to be applied
     * @param initialValuesMap - the initial data to process
     * @return - {@link ConditionalFormatting} object containing the results of processing the data
     */
    public ConditionalFormatting evalConditionalFormatting(String jsCode, Map<String, Object> initialValuesMap) {
        if (!StringUtils.hasLength(jsCode)) {
            return new ConditionalFormatting(emptyList(), emptyMap(), emptyMap(), emptyMap(), emptyMap(), emptyMap());
        }

        try (Context context = Context.newBuilder(LANG_JS).allowAllAccess(true).build()) {
            Value bindings = context.getBindings(LANG_JS);
            bindings.putMember("result", new ConditionalFormatting());
            // добавить все значения закладок в контекст скрипта
            bindings.putMember("values", ProxyObject.fromMap(initialValuesMap));
            // добавить ссылки на сервисы вычисления операций форматирования
            operations.forEach(bindings::putMember);

            jsCode = String.format("%s doConditionalFormatting();", jsCode);
            Source source = Source.create(LANG_JS, jsCode);
            ConditionalFormatting result = context.eval(source).as(ConditionalFormatting.class);
            // We need to disconnect result from Polyglot context. In other case we'll get an error using it outside this method
            // TODO Consider implement mapping method to avoid mapping through JSON
            String content = objectMapper.writeValueAsString(result);
            log.debug("Conditional formatting result: {}", content);
            return objectMapper.readValue(content, ConditionalFormatting.class);
        } catch (PolyglotException | JsonProcessingException e) {
            log.debug("Script execution error {}, script {}", e.getMessage(), jsCode, e);
            throw new InvalidConfigurationException("Conditional formatting script execution error", e);
        }
    }
}
