package ru.vtb.prfr.report.generation.client.evaluation.impl;

import org.springframework.stereotype.Component;
import ru.vtb.prfr.report.generation.client.evaluation.common.AbstractInterestOperation;
import ru.vtb.prfr.report.generation.client.evaluation.details.InterestDetails;

import java.math.RoundingMode;

/**
 * Функция "Число прописью (проц)" (interestWords)
 * Формат отображения: "[значение элемента] ([число прописью]) [проценты]"
 * <p>
 * Пример: 1        -> "1,00 (Один) процент"
 * Пример: 1.2      -> "1,20 (Одна целая двадцать сотых) процента"
 * Пример: 11       -> "11,00 (Одиннадцать) процентов"
 * Пример: 11.1     -> "11,1 (Одиннадцать целых одна десятая) процентов"
 * Пример: 11.11    -> "11,11 (Одиннадцать целых одиннадцать сотых) процентов"
 */
@Component(InterestWordsOperation.SYS_NAME)
public class InterestWordsOperation extends AbstractInterestOperation {
    public static final String SYS_NAME = "interestWords";

    @Override
    public String doEval(Number value) {
        InterestDetails details = getInterestDetails(value);
        return String.format("%s (%s) %s", details.getFormattedValue(), details.getPercentWords(), details.getPostfix());
    }

    @Override
    protected RoundingMode roundingMode() {
        return RoundingMode.DOWN;
    }
}
