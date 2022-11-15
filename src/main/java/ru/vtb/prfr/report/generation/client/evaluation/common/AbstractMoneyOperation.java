package ru.vtb.prfr.report.generation.client.evaluation.common;

import com.ibm.icu.text.MessageFormat;
import org.springframework.util.StringUtils;
import ru.vtb.prfr.report.generation.client.evaluation.Evaluator;
import ru.vtb.prfr.report.generation.client.evaluation.details.MoneyDetails;

import java.math.RoundingMode;

// TODO is a relationship here is not correct. I mean, that FirsNameOperation for example should not extend this class,
//  rather it should use  AbstractNameOperation (the name also should be changed) as a dependency here, as a private field.

/**
 * Класс, инкапсулирующий операции форматирования значений типа double в строковые денежные значения прописью ("Один", "Двадцать" и тд),
 * окончания ("рубль", "рублей" и тд) и в отформатированные строковые значения ("10 345,40", "10 345,00" и тд).
 */
public abstract class AbstractMoneyOperation extends Evaluator<Number> {

    private final MessageFormat rublePostfixFormat = new MessageFormat("{0,plural, one{рубль} few{рубля} other{рублей}}", LOCALE_RU);
    private final MessageFormat kopeckPostfixFormat = new MessageFormat("{0,plural, one{копейка} few{копейки} other{копеек}}", LOCALE_RU);

    protected MoneyDetails getMoneyDetails(Number moneyValue) {
        final String formattedMoneyValue = decimalFormat.format(moneyValue);
        String[] valueParts = formattedMoneyValue.split(",");
        final String kopeck = valueParts[1];
        final String rublePostfix = rublePostfixFormat.format(new Object[]{moneyValue.longValue()});
        final String kopeckPostfix = kopeckPostfixFormat.format(new Object[]{Integer.parseInt(kopeck)});
        return new MoneyDetails(
                StringUtils.capitalize(wordsFormatterMale.format(moneyValue.longValue())),
                kopeck,
                rublePostfix,
                kopeckPostfix,
                formattedMoneyValue
        );
    }

    @Override
    protected RoundingMode roundingMode() {
        return RoundingMode.DOWN;
    }
}
