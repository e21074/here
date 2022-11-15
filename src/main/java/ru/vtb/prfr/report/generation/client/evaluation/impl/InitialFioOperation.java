package ru.vtb.prfr.report.generation.client.evaluation.impl;

import org.springframework.stereotype.Component;
import ru.vtb.prfr.report.generation.client.evaluation.common.AbstractNameOperation;

/**
 * Функция "Инициалы" (initialFIO)
 * Формат отображения: "[фамилия] [И].[О].", где [фамилия] - значение соответствующего элемента UNISON до первого знака "пробел" ("пробел" не включается).
 * Если пробел в тексте отсутствует, то выводится все значение элемента.
 * [И] - первый знак после первого пробела соответствующего элемента UNISON. Если пробел в тексте отсутствует или после пробела больше нет знаков, то выводится "[фамилия]"
 * [О] - первый знак после второго пробела соответствующего элемента UNISON. Если второй пробел в тексте отсутствует или текст заканчивается на второй пробел, то выводится только "[фамилия] [И]."
 * <p>
 * Иванов Иван Иванович     -> "Иванов И.И."
 * Иванов Иван              -> "Иванов И."
 * Иванов                   -> "Иванов"
 */
@Component(InitialFioOperation.SYS_NAME)
public class InitialFioOperation extends AbstractNameOperation {
    public static final String SYS_NAME = "initialFIO";

    @Override
    public String doEval(String value) {
        String[] parts = getNameParts(value);
        String fio = parts.length > 0 ? parts[0] + " " : "";
        if (parts.length > 1) {
            fio += parts[1].charAt(0) + ".";
        }
        if (parts.length > 2) {
            fio += parts[2].charAt(0) + ".";
        }
        return fio.trim();
    }
}
