package ru.vtb.prfr.report.generation.client.evaluation.impl;

import com.ibm.icu.text.DecimalFormat;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.vtb.prfr.report.generation.client.evaluation.Evaluator;

import java.math.RoundingMode;

/**
 * Функция "Целое число прописью" (integerWords)
 * Формат отображения: " [целая часть элемента] ([число прописью])"
 * <p>
 * Пример: 2.57     -> "2 (Два)"
 * Пример: 12.1     -> "12 (Двеннадцать)"
 * Пример: 21.01    -> "21 (Двадцать один)"
 */
@Component(IntegerWordsOperation.SYS_NAME)
public class IntegerWordsOperation extends Evaluator<Number> {
    public static final String SYS_NAME = "integerWords";

    protected DecimalFormat numberFormat;

    {
        numberFormat = new DecimalFormat("###,##0", formatSymbols);
        numberFormat.setRoundingMode(RoundingMode.UNNECESSARY.ordinal());
    }

    @Override
    public String doEval(Number value) {
        long numberPart = value.longValue();
        String formattedValue = numberFormat.format(numberPart);
        String numberWords = StringUtils.capitalize(wordsFormatterMale.format(numberPart));
        return String.format("%s (%s)", formattedValue, numberWords);
    }

    @Override
    protected RoundingMode roundingMode() {
        return RoundingMode.UNNECESSARY;
    }
}
