package ru.vtb.prfr.report.generation.client.evaluation.impl;

import org.springframework.stereotype.Component;
import ru.vtb.prfr.report.generation.client.evaluation.common.AbstractMoneyOperation;
import ru.vtb.prfr.report.generation.client.evaluation.details.MoneyDetails;

/**
 * Функция "Число прописью (с коп)" (rubleKopeckWords)
 * Формат отображения: "[значение элемента] ([целая часть числа прописью]) [рубли] [дробная часть числа прописью] [копейки].
 * <p>
 * Пример: 10454    ->  "10 454,00 (Десять тысяч четыреста пятьдесят четыре рубля 00 копеек)"
 * Пример: 10454.34 ->  "10 454,34 (Десять тысяч четыреста пятьдесят четыре рубля 34 копейки)"
 */
@Component(RubleKopeckWordsOperation.SYS_NAME)
public class RubleKopeckWordsOperation extends AbstractMoneyOperation {
    public static final String SYS_NAME = "rubleKopeckWords";

    @Override
    public String doEval(Number value) {
        MoneyDetails details = getMoneyDetails(value);
        return String.format("%s (%s %s %s %s)", details.getFormattedValue(), details.getRubleWords(),
                details.getRublePostfix(), details.getKopeckNumber(), details.getKopeckPostfix());
    }
}
