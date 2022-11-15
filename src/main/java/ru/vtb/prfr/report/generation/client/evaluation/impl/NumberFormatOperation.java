package ru.vtb.prfr.report.generation.client.evaluation.impl;

import com.ibm.icu.text.DecimalFormat;
import org.springframework.stereotype.Component;
import ru.vtb.prfr.report.generation.client.evaluation.Evaluator;

import java.math.RoundingMode;

/**
 * Функция "Форматирование числа" (numberFormat)
 * Операция для форматирования чисел по следующим правилам:
 * - если число без указания дробной части, то по умолчанию отображать без 00 после запятой;
 * - если число с 1 знаком после запятой, то по умолчанию дополнять "0" до 2-х знаков;
 * - если число с 2 до 5, то отображать столько знаков, сколько пришло;
 * - разбивать число на разряды, разделитель " "
 * - указывать "," разделителем дробной и целой частей
 * <p>
 * Пример: 2.0     -> "2,00"
 * Пример: 2000.0     -> "2 000,00"
 * Пример: 1002.1     -> "1 002,10"
 * Пример: 1002.12     -> "1 002,12"
 * Пример: 1002.123     -> "1 002,123"
 */
@Component(NumberFormatOperation.SYS_NAME)
public class NumberFormatOperation extends Evaluator<Number> {
    public static final String SYS_NAME = "numberFormat";

    {
        decimalFormat = new DecimalFormat("###,##0.00###", formatSymbols);
        decimalFormat.setRoundingMode(roundingMode().ordinal());
    }

    @Override
    protected String doEval(Number value) {
        return decimalFormat.format(value.doubleValue());
    }

    @Override
    protected RoundingMode roundingMode() {
        return RoundingMode.DOWN;
    }
}
