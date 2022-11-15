package ru.vtb.prfr.report.generation.client.evaluation.common;

import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.text.MessageFormat;
import com.ibm.icu.text.RuleBasedNumberFormat;
import org.springframework.util.StringUtils;
import ru.vtb.prfr.report.generation.client.evaluation.Evaluator;
import ru.vtb.prfr.report.generation.client.evaluation.details.InterestDetails;

import static java.lang.String.format;

// TODO is a relationship here is not correct. I mean, that FirsNameOperation for example should not extend this class,
//  rather it should use  AbstractNameOperation (the name also should be changed) as a dependency here, as a private field.

/**
 * Класс, инкапсулирующий операции форматирования значений типа double в строковые значения (с дробной частью) прописью ("Один", "Двадцать целых девять сотых" и тд),
 * окончания ("процент", "процентов" и тд) и в отформатированные строковые значения ("10 345,40", "10 345,00" и тд).
 */
public abstract class AbstractInterestOperation extends Evaluator<Number> {

    private final RuleBasedNumberFormat customWordsFormatter;

    protected DecimalFormat interestDecimalFormat;

    protected DecimalFormat interestDecimalFormatP0;

    protected DecimalFormat interestDecimalFormatP1;

    protected DecimalFormat interestDecimalFormatP2;

    protected DecimalFormat interestDecimalFormatP3;

    protected DecimalFormat interestDecimalFormatP4;
    
    protected DecimalFormat interestDecimalFormatP5;

    {
        customWordsFormatter = new RuleBasedNumberFormat(NUMBER_TO_WORDS_FORMAT, LOCALE_RU);
        customWordsFormatter.setDefaultRuleSet("%interest");

        interestDecimalFormat = new DecimalFormat("###,##0.00###", formatSymbols);
        interestDecimalFormat.setRoundingMode(roundingMode().ordinal());

        interestDecimalFormatP0 = new DecimalFormat("###,##0", formatSymbols);
        interestDecimalFormatP0.setRoundingMode(roundingMode().ordinal());

        interestDecimalFormatP1 = new DecimalFormat("###,##0.0", formatSymbols);
        interestDecimalFormatP1.setRoundingMode(roundingMode().ordinal());

        interestDecimalFormatP2 = new DecimalFormat("###,##0.00", formatSymbols);
        interestDecimalFormatP2.setRoundingMode(roundingMode().ordinal());

        interestDecimalFormatP3 = new DecimalFormat("###,##0.000", formatSymbols);
        interestDecimalFormatP3.setRoundingMode(roundingMode().ordinal());

        interestDecimalFormatP4 = new DecimalFormat("###,##0.0000", formatSymbols);
        interestDecimalFormatP4.setRoundingMode(roundingMode().ordinal());

        interestDecimalFormatP5 = new DecimalFormat("###,##0.00000", formatSymbols);
        interestDecimalFormatP5.setRoundingMode(roundingMode().ordinal());
    }

    private final MessageFormat separatorFormat = new MessageFormat("{0,plural, one{целая} few{целых} other{целых}}", LOCALE_RU);

    private final MessageFormat postfix10thFormat = new MessageFormat("{0,plural, one{десятая} few{десятых} other{десятых}}", LOCALE_RU);

    private final MessageFormat postfix100thFormat = new MessageFormat("{0,plural, one{сотая} few{сотых} other{сотых}}", LOCALE_RU);

    private final MessageFormat postfix1000thFormat = new MessageFormat("{0,plural, one{тысячная} few{тысячных} other{тысячных}}", LOCALE_RU);

    private final MessageFormat postfix10000thFormat = new MessageFormat("{0,plural, one{десятитысячная} few{десятитысячных} other{десятитысячных}}", LOCALE_RU);

    private final MessageFormat postfix100000thFormat = new MessageFormat("{0,plural, one{стотысячная} few{стотысячных} other{стотысячных}}", LOCALE_RU);

    // формат для целого значения
    private final MessageFormat integerPercentFormat = new MessageFormat("{0,plural, one{процент} few{процента} other{процентов}}", LOCALE_RU);

    // формат для дробного значения
    private final MessageFormat decimalPercentFormat = new MessageFormat("{0,plural, one{процента} few{процента} other{процентов}}", LOCALE_RU);

    protected InterestDetails getInterestDetails(Number percent) {
        return getInterestDetails(percent, null);
    }

    protected InterestDetails getInterestDetails(Number percent, Integer precision) {
        DecimalFormat decimalFormat;
        if (precision == null) {
            decimalFormat = getDecimalFormat();
        } else {
            decimalFormat = getDecimalFormat(precision);
        }
        String formattedPercent = decimalFormat.format(percent);
        String[] formattedPercentParts = formattedPercent.split(",");
        String formattedIntegerPart = formattedPercentParts[0];
        String formattedDecimalPart = formattedPercentParts.length > 1 ? formattedPercentParts[1] : "0";
        int formattedDecimalPartValue = Integer.parseInt(formattedDecimalPart);
        String decimalPartWords;
        String numberPartWords;
        String percentPostfix;
        final long percentIntegerPart = percent.longValue();
        final Object[] percentIntegerValue = { percentIntegerPart };
        if (formattedDecimalPartValue == 0) {
            numberPartWords = getIntegerValueFormat().format(percentIntegerPart);
            percentPostfix = integerPercentFormat.format(percentIntegerValue);
            decimalPartWords = "";
        } else {
            numberPartWords = getDecimalValueFormat().format(percentIntegerPart);
            percentPostfix = decimalPercentFormat.format(percentIntegerValue);
            String separator = separatorFormat.format(percentIntegerValue);
            String postfix;
            int dl = formattedDecimalPart.length();
            if (precision != null && precision == 1) {
                postfix = postfix10thFormat.format(new Object[] { formattedDecimalPartValue });
            } else if (dl <= 2) {
                postfix = postfix100thFormat.format(new Object[] { formattedDecimalPartValue });
            } else if (dl == 3) {
                postfix = postfix1000thFormat.format(new Object[] { formattedDecimalPartValue });
            } else if (dl == 4) {
                postfix = postfix10000thFormat.format(new Object[] { formattedDecimalPartValue });
            } else {
                postfix = postfix100000thFormat.format(new Object[] { formattedDecimalPartValue });
            }
            decimalPartWords = format(" %s %s %s", separator, wordsFormatterFemale.format(formattedDecimalPartValue), postfix);
        }

        String percentWords = StringUtils.capitalize(format("%s%s", numberPartWords, decimalPartWords));
        return new InterestDetails(
                formattedPercent,
                formattedIntegerPart,
                formattedDecimalPart,
                formattedDecimalPartValue,
                percentWords,
                percentPostfix
        );
    }

    /** Формат, используемый для форматирования целых значений ("один", "два" и тд */
    protected RuleBasedNumberFormat getIntegerValueFormat() {
        return wordsFormatterMale;
    }

    /** Формат, используемый для форматирования дробных значений ("один", "одна целая" и тд */
    protected RuleBasedNumberFormat getDecimalValueFormat() {
        return customWordsFormatter;
    }

    protected DecimalFormat getDecimalFormat() {
        return interestDecimalFormat;
    }

    protected DecimalFormat getDecimalFormat(Integer precision) {
        if (precision <= 0) {
            return interestDecimalFormatP0;
        }
        switch (precision) {
            case 1: return interestDecimalFormatP1;
            case 2: return interestDecimalFormatP2;
            case 3: return interestDecimalFormatP3;
            case 4: return interestDecimalFormatP4;
            default: return interestDecimalFormatP5;
        }
    }
}
