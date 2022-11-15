package ru.vtb.prfr.report.generation.client.evaluation.impl;

import com.ibm.icu.text.RuleBasedNumberFormat;
import org.springframework.stereotype.Component;
import ru.vtb.prfr.report.generation.client.evaluation.common.AbstractInterestOperation;
import ru.vtb.prfr.report.generation.client.evaluation.details.InterestDetails;

import java.math.RoundingMode;

/**
 * Функция "Число прописью (жен)" (numberWordsF)
 * Формат отображения: " [значение элемента] ([число прописью])"
 * <p>
 * Пример: 2        -> "2 (Две)"
 * Пример: 2.1      -> "2,10 (Две целых десять сотых)"
 * Пример: 2.011    -> "2,01 (Две целых одна сотая)"
 */
@Component(NumberWordsFemaleOperation.SYS_NAME)
public class NumberWordsFemaleOperation extends AbstractInterestOperation {
    public static final String SYS_NAME = "numberWordsF";

    @Override
    public String doEval(Number value) {
        InterestDetails details = getInterestDetails(value);
        String formattedValue = details.getFormattedValue();
        if (details.getFormattedDecimalPartValue() == 0) {
            // скрыть дробную часть, если отформатированное значение - целое
            formattedValue = details.getFormattedIntegerPart();
        }
        return String.format("%s (%s)", formattedValue, details.getPercentWords());
    }

    @Override
    protected RoundingMode roundingMode() {
        return RoundingMode.DOWN;
    }

    @Override
    protected RuleBasedNumberFormat getIntegerValueFormat() {
        return wordsFormatterFemale;
    }

    @Override
    protected RuleBasedNumberFormat getDecimalValueFormat() {
        return wordsFormatterFemale;
    }
}
