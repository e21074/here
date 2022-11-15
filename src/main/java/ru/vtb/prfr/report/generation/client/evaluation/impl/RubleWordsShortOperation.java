package ru.vtb.prfr.report.generation.client.evaluation.impl;

import org.springframework.stereotype.Component;
import ru.vtb.prfr.report.generation.client.evaluation.common.AbstractMoneyOperation;
import ru.vtb.prfr.report.generation.client.evaluation.details.MoneyDetails;

/**
 * Функция "Число прописью сокр (руб)" (rubleWordsShort)
 * Формат отображения: "[число прописью] [рубли]"
 * <p>
 * Пример: 10455    ->  "Десять тысяч четыреста пятьдесят пять и 00/100 рублей"
 * Пример: 10455.34 ->  "Десять тысяч четыреста пятьдесят пять и 34/100 рублей"
 */
@Component(RubleWordsShortOperation.SYS_NAME)
public class RubleWordsShortOperation extends AbstractMoneyOperation {
    public static final String SYS_NAME = "rubleWordsShort";

    @Override
    public String doEval(Number value) {
        MoneyDetails details = getMoneyDetails(value);
        return String.format("%s и %s/100 %s", details.getRubleWords(), details.getKopeckNumber(), details.getRublePostfix());
    }
}
