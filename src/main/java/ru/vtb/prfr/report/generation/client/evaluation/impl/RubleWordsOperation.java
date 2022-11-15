package ru.vtb.prfr.report.generation.client.evaluation.impl;

import org.springframework.stereotype.Component;
import ru.vtb.prfr.report.generation.client.evaluation.common.AbstractMoneyOperation;
import ru.vtb.prfr.report.generation.client.evaluation.details.MoneyDetails;

/**
 * Функция "Число прописью (руб)" (rubleWords)
 * Формат отображения: "[значение элемента] ([число прописью]) [рубли].
 * <p>
 * Пример: 10454    ->  "10 454,00 (Десять тысяч четыреста пятьдесят четыре и 00/100) рубля"
 * Пример: 10454.34 ->  "10 454,34 (Десять тысяч четыреста пятьдесят четыре и 34/100) рубля"
 */
@Component(RubleWordsOperation.SYS_NAME)
public class RubleWordsOperation extends AbstractMoneyOperation {
    public static final String SYS_NAME = "rubleWords";

    @Override
    public String doEval(Number value) {
        MoneyDetails details = getMoneyDetails(value);
        return String.format("%s (%s и %s/100) %s", details.getFormattedValue(), details.getRubleWords(), details.getKopeckNumber(), details.getRublePostfix());
    }
}
