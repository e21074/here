package ru.vtb.prfr.report.generation.client.evaluation.impl;

import org.springframework.stereotype.Component;
import ru.vtb.prfr.report.generation.client.evaluation.common.AbstractInterestOperation;
import ru.vtb.prfr.report.generation.client.evaluation.details.InterestDetails;

import java.math.RoundingMode;

/**
 * Функция "Число прописью (муж)" (numberWordsM)
 * Формат отображения: " [значение элемента] ([число прописью])"
 * <p>
 * Пример: 1        -> "1 (Один)"
 * Пример: 1.1      -> "1,10 (Одна целая десять сотых)"
 * Пример: 1.01     -> "1,01 (Одна целая одна сотая)"
 * Пример: 9.012    -> "9,012 (Девять целых двенадцать тысячных)"
 */
@Component(NumberWordsMaleOperation.SYS_NAME)
public class NumberWordsMaleOperation extends AbstractInterestOperation {
    public static final String SYS_NAME = "numberWordsM";

    @Override
    public String doEval(Number value) {
        InterestDetails details = getInterestDetails(value);
        return parse(details, null);
    }

    public String eval(Number value, Integer precision) {
        if (value == null) {
            return null;
        }
        InterestDetails details = getInterestDetails(value, precision);
        return parse(details, precision);
    }

    private String parse(InterestDetails details, Integer precision) {
        String formattedValue = details.getFormattedValue();
        if (details.getFormattedDecimalPartValue() == 0 && (precision == null || precision == 0)) {
            formattedValue = details.getFormattedIntegerPart();
        }
        return String.format("%s (%s)", formattedValue, details.getPercentWords());
    }

    @Override
    protected RoundingMode roundingMode() {
        return RoundingMode.DOWN;
    }
}
