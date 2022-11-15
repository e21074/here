package ru.vtb.prfr.report.generation.client.evaluation.impl;

import org.springframework.stereotype.Component;
import ru.vtb.prfr.report.generation.client.evaluation.common.AbstractNameOperation;

/**
 * Функция "Фамилия" (lastnameFIO)
 * Формат отображения: "[значение]", где [значение] - значение соответствующего элемента UNISON до первого знака "пробел" ("пробел" не включается).
 * Если пробел в тексте отсутствует, то выводится все значение элемента. Текст выводится без корректировок.
 * <p>
 * Пример: Иванов Иван Иванович -> "Иванов"
 */
@Component(LastNameOperation.SYS_NAME)
public class LastNameOperation extends AbstractNameOperation {
    public static final String SYS_NAME = "lastnameFIO";

    @Override
    protected String doEval(String value) {
        String[] parts = getNameParts(value);
        return parts.length >= 1 ? parts[0] : "";
    }
}
