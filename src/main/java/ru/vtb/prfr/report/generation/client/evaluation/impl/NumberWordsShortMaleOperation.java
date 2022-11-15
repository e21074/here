package ru.vtb.prfr.report.generation.client.evaluation.impl;

import org.springframework.stereotype.Component;
import ru.vtb.prfr.report.generation.client.evaluation.common.AbstractInterestOperation;
import ru.vtb.prfr.report.generation.client.evaluation.details.InterestDetails;

import java.math.RoundingMode;

/**
 * Функция "Число прописью (муж)" (numberWordsShortM)
 * Формат отображения: [число прописью] - значение соответствующего
 * элемента UNISON прописью, склонение целых числительных, оканчивающихся на "один" и "два" - в мужском роде.
 * <p>
 * Пример: 1        -> "Один"
 * Пример: 1.1      -> "Одна целая десять сотых"
 * Пример: 1.01     -> "Одна целая одна сотая"
 * Пример: 9.012    -> "Девять целых двенадцать тысячных"
 */
@Component(NumberWordsShortMaleOperation.SYS_NAME)
public class NumberWordsShortMaleOperation extends AbstractInterestOperation {
    public static final String SYS_NAME = "numberWordsShortM";

    @Override
    public String doEval(Number value) {
        InterestDetails details = getInterestDetails(value);
        return details.getPercentWords();
    }

    @Override
    protected RoundingMode roundingMode() {
        return RoundingMode.DOWN;
    }
}
