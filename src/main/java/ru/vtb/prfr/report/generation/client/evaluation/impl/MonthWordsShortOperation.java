package ru.vtb.prfr.report.generation.client.evaluation.impl;

import org.springframework.stereotype.Component;
import ru.vtb.prfr.report.generation.client.evaluation.common.AbstractMonthOperation;
import ru.vtb.prfr.report.generation.client.evaluation.details.MonthDetails;

/**
 * Функция "Число прописью сокр (мес)" (monthWordsShort)
 * Формат отображения: "[число прописью] [месяцы]"
 * <p>
 * Пример: 11 -> "Одиннадцать месяцев"
 */
@Component(MonthWordsShortOperation.SYS_NAME)
public class MonthWordsShortOperation extends AbstractMonthOperation {
    public static final String SYS_NAME = "monthWordsShort";

    @Override
    protected String doEval(Number value) {
        MonthDetails details = getMonthDetails(value);
        return String.format("%s %s", details.getMonthsWords(), details.getPostfix());
    }
}
