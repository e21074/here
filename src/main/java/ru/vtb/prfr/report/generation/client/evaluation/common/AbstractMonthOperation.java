package ru.vtb.prfr.report.generation.client.evaluation.common;

import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.text.MessageFormat;
import org.springframework.util.StringUtils;
import ru.vtb.prfr.report.generation.client.evaluation.Evaluator;
import ru.vtb.prfr.report.generation.client.evaluation.details.MonthDetails;

import java.math.RoundingMode;

// TODO is a relationship here is not correct. I mean, that FirsNameOperation for example should not extend this class,
//  rather it should use  AbstractNameOperation (the name also should be changed) as a dependency here, as a private field.

/**
 * Класс, инкапсулирующий операции форматирования значений типа int в строковые значения прописью ("Один", "Двадцать" и тд),
 * окончания ("месяц", "месяцев" и тд) и в отформатированные строковые значения ("10 345", "10" и тд).
 */
public abstract class AbstractMonthOperation extends Evaluator<Number> {

    private final MessageFormat postfixFormat = new MessageFormat("{0,plural, one{месяц} few{месяца} other{месяцев}}", LOCALE_RU);

    protected DecimalFormat monthsNumberFormat;

    {
        monthsNumberFormat = new DecimalFormat("###,##0", formatSymbols);
        monthsNumberFormat.setRoundingMode(RoundingMode.UNNECESSARY.ordinal()); // форматировать только целочисленные значения
    }

    protected MonthDetails getMonthDetails(Number value) {
        Integer months = value.intValue();
        return new MonthDetails(
                monthsNumberFormat.format(months),
                StringUtils.capitalize(wordsFormatterMale.format(months)),
                postfixFormat.format(new Object[] { months })
        );
    }

    @Override
    protected RoundingMode roundingMode() {
        return RoundingMode.UNNECESSARY;
    }
}
