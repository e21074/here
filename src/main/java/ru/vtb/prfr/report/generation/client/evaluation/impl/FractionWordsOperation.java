package ru.vtb.prfr.report.generation.client.evaluation.impl;

import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.text.DecimalFormatSymbols;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.vtb.prfr.report.generation.client.evaluation.Evaluator;

import java.math.RoundingMode;

/**
 * Функция "Дробное число прописью" (fractionWords)
 * Формат отображения: "[дробная часть элемента] ([число прописью])"
 * <p>
 * Пример: 2.57     -> "57 (Пятьдесят семь)"
 * Пример: 12.1     -> "10 (Десять)"
 * Пример: 21.01    -> "01 (Одна)"
 */
@Component(FractionWordsOperation.SYS_NAME)
public class FractionWordsOperation extends Evaluator<Number> {
    public static final String SYS_NAME = "fractionWords";

    private final DecimalFormat fractionFormat;

    {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(LOCALE_RU);
        symbols.setDecimalSeparatorString("");

        fractionFormat = new DecimalFormat(".00", symbols);
        fractionFormat.setRoundingMode(RoundingMode.FLOOR.ordinal());
        fractionFormat.setMaximumIntegerDigits(0);
    }

    @Override
    public String doEval(Number value) {
        String penny = fractionFormat.format(value);
        if ("00".equals(penny)) {
            penny = "0";
        }
        String pennyWords = StringUtils.capitalize(wordsFormatterFemale.format(Integer.parseInt(penny)));
        return String.format("%s (%s)", penny, pennyWords);
    }

    @Override
    protected RoundingMode roundingMode() {
        return RoundingMode.HALF_UP;
    }
}
