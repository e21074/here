package ru.vtb.prfr.report.generation.client.evaluation.impl;

import org.springframework.stereotype.Component;
import ru.vtb.prfr.report.generation.client.evaluation.common.AbstractNameOperation;

/**
 * Функция "Отчество" (middleFIO)
 * Формат отображения: "[значение]", где [значение] - значение соответствующего элемента UNISON после второго знака "пробел" ("пробел" не включается).
 * Если второй пробел отсутствует или элемент не содержит пробелов, то значение = "".
 * Если в тексте содержится больше пробелов, то инфомрация выводится вместе с пробелами.
 * Текст выводится без корректировок.
 * <p>
 * Пример: Иванов Иван Иванович -> "Иванович"
 */
@Component(MiddleNameOperation.SYS_NAME)
public class MiddleNameOperation extends AbstractNameOperation {
    public static final String SYS_NAME = "middleFIO";

    @Override
    protected String doEval(String value) {
        String[] parts = getNameParts(value);
        return parts.length >= 3 ? parts[2] : "";
    }
}
