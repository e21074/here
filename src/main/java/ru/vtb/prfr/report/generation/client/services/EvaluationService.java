package ru.vtb.prfr.report.generation.client.services;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.NumberUtils;
import ru.vtb.prfr.report.generation.client.client.model.ReportAttribute;
import ru.vtb.prfr.report.generation.client.evaluation.Evaluator;
import ru.vtb.prfr.report.generation.client.evaluation.impl.NumberFormatOperation;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class EvaluationService {
    @Autowired
    private Map<String, Evaluator> operations;

    /**
     * Applies the provided operation to the value and returns the result.
     *
     * @param operation - system name of the desired operation (bean qualifier)
     * @param value     - input value to be formatted by operation
     * @return - formatted value
     */
    @SuppressWarnings("unchecked")
    public String eval(@NonNull String operation, @NonNull Object value) {
        Evaluator evaluator = operations.get(operation);

        Assert.state(evaluator != null, "Evaluator should have been validated for presence, but not found now. Please, report this to developers");

        if (value instanceof Number) {
            value = NumberUtils.convertNumberToTargetClass((Number) value, Double.class);
        }
        log.trace("eval value {} with operation {} (Evaluator: {})", value, operation, evaluator.getClass().getName());
        return evaluator.eval(value);
    }

    /**
     * Replaces all values in the attributeNameAttributeValueMap by formatted values.
     * Formatting operations are taken from attributeNameAttributeMap.
     * If there is no operation provided for value of type Double it formatted by {@link NumberFormatOperation}
     *
     * @param attributeNameAttributeValueMap - map of resolved values for each attribute sysName
     * @param attributeNameAttributeMap      - map of initial attributes for each attribute sysName
     */
    @SuppressWarnings("unchecked")
    public void applyOperations(Map<String, Object> attributeNameAttributeValueMap,
                                Map<String, ReportAttribute> attributeNameAttributeMap) {
        attributeNameAttributeValueMap.entrySet()
                .forEach(entry -> {
                    Object value = entry.getValue();
                    if (value instanceof List) {
                        // recursively apply operations for an array
                        ((List<Map<String, Object>>) value).forEach(map -> applyOperations(map, attributeNameAttributeMap));
                    } else {
                        // apply operation for single value
                        ReportAttribute attribute = attributeNameAttributeMap.get(entry.getKey());
                        if (attribute != null && attribute.getOperationName() != null) {
                            entry.setValue(eval(attribute.getOperationName(), value));
                        } else if (entry.getValue() instanceof Double) {
                            entry.setValue(formatDouble(value));
                        }
                    }
                });
    }

    public String formatDouble(String value) {
        return formatDouble(Double.valueOf(value));
    }

    private String formatDouble(Object value) {
        return eval(NumberFormatOperation.SYS_NAME, value);
    }
}
