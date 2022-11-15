package ru.vtb.prfr.report.generation.client.evaluation.impl;

import org.springframework.stereotype.Component;
import ru.vtb.prfr.report.generation.client.evaluation.common.AbstractMonthOperation;
import ru.vtb.prfr.report.generation.client.evaluation.details.MonthDetails;

/**
 * Функция "Число прописью (мес)" (monthWords)
 * Формат отображения: "[значение элемента] ([число прописью]) [месяцы]"
 * <p>
 * Пример: 11 -> "11 (Одиннадцать) месяцев"
 */
@Component(MonthWordsOperation.SYS_NAME)
public class MonthWordsOperation extends AbstractMonthOperation {
    public static final String SYS_NAME = "monthWords";

    @Override
    protected String doEval(Number value) {
        MonthDetails details = getMonthDetails(value);
        return String.format("%s (%s) %s", details.getFormattedValue(), details.getMonthsWords(), details.getPostfix());
    }
}
