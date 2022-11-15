package ru.vtb.prfr.report.generation.client.evaluation.impl;

import org.apache.commons.lang3.math.Fraction;
import org.springframework.stereotype.Component;
import org.springframework.util.NumberUtils;
import ru.vtb.prfr.report.generation.client.evaluation.Evaluator;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Функция "Простая дробь" (fraction)
 * Формат отображения: значение числового аргумента в формате простой дроби
 * (наибольший общий делитель числителя и знаменателя должен быть равен 1).
 * В случае отсутствия дробной части значение выводится только целая часть.
 * <p>
 * 1.5  ->  "1 1/2"
 * 1    ->  "1"
 * 0.31 ->  "31/100"
 */
@Component(FractionOperation.SYS_NAME)
public class FractionOperation extends Evaluator<Number> {
    public static final String SYS_NAME = "fraction";

    private static final String ZERO = "0";
    private static final String DOT = ".";
    private static final String SPACE = " ";
    private static final String EMPTY_SPACE = "";

    @Override
    public String doEval(Number value) {
        BigDecimal bigDecimal = NumberUtils.convertNumberToTargetClass(value, BigDecimal.class);
        String[] parts = bigDecimal.toString().split("\\.");
        String wholePart = parts[0];
        String fractionPart = parts.length > 1 ?  ZERO + DOT + parts[1] : ZERO + DOT + ZERO;
        Fraction fraction = Fraction.getFraction(Double.parseDouble(fractionPart));
        fractionPart = !fraction.toProperString().equals(ZERO) ? SPACE + fraction.toProperString() : EMPTY_SPACE;
        return wholePart.equals(ZERO) ? fraction.toProperString() : wholePart + fractionPart;
    }

    @Override
    protected RoundingMode roundingMode() {
        return RoundingMode.HALF_UP;
    }
}
