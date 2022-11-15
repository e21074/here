package ru.vtb.prfr.report.generation.client.evaluation.impl;

import com.ibm.icu.text.DecimalFormat;
import org.springframework.stereotype.Component;
import ru.vtb.prfr.report.generation.client.evaluation.common.AbstractInterestOperation;
import ru.vtb.prfr.report.generation.client.evaluation.details.InterestDetails;

import java.math.RoundingMode;

/**
 * Функция "Число прописью сокр (проц)" (interestWordsShort)
 * Формат отображения: "[число прописью] [проценты]"
 * <p>
 * Пример: 11       -> "Одиннадцать процентов"
 * Пример: 11.1     -> "Одиннадцать целых одна десятая процентов"
 * Пример: 11.11    -> "Одиннадцать целых одиннадцать сотых процентов"
 */
@Component(InterestWordsShortOperation.SYS_NAME)
public class InterestWordsShortOperation extends AbstractInterestOperation {
    public static final String SYS_NAME = "interestWordsShort";

    @Override
    public String doEval(Number value) {
        InterestDetails details = getInterestDetails(value);
        return String.format("%s %s", details.getPercentWords(), details.getPostfix());
    }

    @Override
    protected RoundingMode roundingMode() {
        return RoundingMode.DOWN;
    }

    @Override
    protected DecimalFormat getDecimalFormat() {
        return decimalFormat;
    }
}
