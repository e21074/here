package ru.vtb.prfr.report.generation.client.evaluation.impl;

import org.springframework.stereotype.Component;
import ru.vtb.prfr.report.generation.client.evaluation.Evaluator;

import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

/**
 * Функция "Дата (месяц прописью)" (dateWords)
 * Формат отображения: "«[дата]» [месяц прописью] [год]"
 * <p>
 * Пример 01.01.2020 -> "«01» января 2020"
 */
@Component(DateWordsOperation.SYS_NAME)
public class DateWordsOperation extends Evaluator<String> {
    public static final String SYS_NAME = "dateWords";

    private final DateTimeFormatter sourceFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final DateTimeFormatter targetFormat = DateTimeFormatter.ofPattern("«dd» MMMM yyyy", LOCALE_RU);

    @Override
    public String doEval(String value) {
        TemporalAccessor date = sourceFormat.parse(value);
        return targetFormat.format(date);
    }

    @Override
    protected RoundingMode roundingMode() {
        return RoundingMode.UNNECESSARY;
    }
}
