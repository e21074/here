package ru.vtb.prfr.report.generation.client.evaluation.impl;

import org.springframework.stereotype.Component;
import ru.vtb.prfr.report.generation.client.evaluation.common.AbstractNameOperation;

/**
 * Функция "Имя" (nameFIO)
 * Формат отображения: "[значение]", где [значение] - значение соответствующего элемента UNISON между первым и вторым знаками "пробел" ("пробел" не включается).
 * Если пробел в тексте отсутствует, то выводится "".
 * Если второй пробел отсутствует, то выводится значения после "пробела" до конца текста.
 * Текст выводится без корректировок.
 * <p>
 * Пример: Иванов Иван Иванович -> "Иван"
 * Пример: Иванов ИванИванович -> "ИванИванович"
 */
@Component(FirstNameOperation.SYS_NAME)
public class FirstNameOperation extends AbstractNameOperation {
    public static final String SYS_NAME = "nameFIO";

    @Override
    public String doEval(String value) {
        String[] parts = getNameParts(value);
        return parts.length >= 2 ? parts[1] : "";
    }
}
