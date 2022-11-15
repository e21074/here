package ru.vtb.prfr.report.generation.client.evaluation.impl;

import com.ibm.icu.text.RuleBasedNumberFormat;
import org.springframework.stereotype.Component;
import ru.vtb.prfr.report.generation.client.evaluation.common.AbstractInterestOperation;
import ru.vtb.prfr.report.generation.client.evaluation.details.InterestDetails;

import java.math.RoundingMode;

/**
 * Число прописью сокр (жен) (numberWordsShortF)
 * Формат отображения: [число прописью] - значение соответствующего
 * элемента UNISON прописью, склонение целых числительных, оканчивающихся на "одна" и "две" - в женском роде.
 * <p>
 * Пример: 1        -> "Одна"
 * Пример: 1.1      -> "Одна целая десять сотых"
 * Пример: 1.01     -> "Одна целая одна сотая"
 * Пример: 9.012    -> "Девять целых двенадцать тысячных"
 */
@Component(NumberWordsShortFemaleOperation.SYS_NAME)
public class NumberWordsShortFemaleOperation extends AbstractInterestOperation {
    public static final String SYS_NAME = "numberWordsShortF";

    @Override
    public String doEval(Number value) {
        InterestDetails details = getInterestDetails(value);
        return details.getPercentWords();
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
