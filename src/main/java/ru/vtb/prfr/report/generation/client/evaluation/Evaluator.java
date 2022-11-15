package ru.vtb.prfr.report.generation.client.evaluation;

import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.text.DecimalFormatSymbols;
import com.ibm.icu.text.RuleBasedNumberFormat;

import java.math.RoundingMode;
import java.util.Locale;

/**
 * Evaluator.java - абстрактный класс, описывающий вычисление результата конкретной операции над значением <T>.
 * Содержит общие форматы для конвертации чисел в слова и в строковые значения с разбиением на группы.
 * <p>
 * Пакет /impl содержит реализации Evaluator, каждая конктертная операция реализуется отдельным классом.
 * <p>
 * Пакет /impl/common содержит абстрактные классы, содержащие общие элементы бизнес-логики (вычисление details для сумм, процентов и месяцев).
 * <p>
 * Пакет /impl/details содержит POJO, описывающие части результатов схожих операций.
 * Например, для форматирования суммы используется MoneyDetails, состоящий из копеек, отформатированной суммы, значения суммы прописью и тд.
 */

public abstract class Evaluator<T> {

    protected static final Locale LOCALE_RU = Locale.forLanguageTag("ru");
    protected static final String NUMBER_TO_WORDS_FORMAT = "%default:ноль;один;два;три;четыре;пять;шесть;семь;восемь;девять;10: десять;одиннадцать;"
            + "двенадцать;тринадцать;четырнадцать;15:пятнадцать;шестнадцать;семнадцать;восемнадцать;девятнадцать;"
            + "20: двадцать[ >>];"
            + "30: тридцать[ >>];"
            + "40: сорок[ >>];"
            + "50: пятьдесят[ >>];"
            + "60: шестьдесят[ >>];"
            + "70: семьдесят[ >>];"
            + "80: восемьдесят[ >>];"
            + "90: девяносто[ >>];"
            + "100: сто[ >>];"
            + "200: двести[ >>];"
            + "300: триста[ >>];"
            + "400: четыреста[ >>];"
            + "500: пятьсот[ >>];"
            + "600: шестьсот[ >>];"
            + "700: семьсот[ >>];"
            + "800: восемьсот[ >>];"
            + "900: девятьсот[ >>];"
            + "1000: <%alt< тысяча[ >>];"
            + "2000: <%alt< тысячи[ >>];"
            + "5000: <%alt< $(cardinal,one{тысяча} few{тысячи} other{тысяч})$[ >>];"
            + "1 000 000: << миллион[ >>];"
            + "2 000 000: << миллиона[ >>];"
            + "5 000 000: << $(cardinal,one{миллион} few{миллиона} other{миллионов})$[ >>];"
            + "1 000 000 000: << миллиард[ >>];"
            + "2 000 000 000: << миллиарда[ >>];"
            + "5 000 000 000: << $(cardinal,one{миллиард} few{миллиарда} other{миллиардов})$[ >>];"
            + "1,000,000,000,000: =#,##0=;"

            + "%alt:"
            + "0:=%default=;одна;две;"
            + "=%default=;21:двадцать одна;22:двадцать две;"
            + "=%default=;31:тридцать одна;32:тридцать две;"
            + "=%default=;41:сорок одна;42:сорок две;"
            + "=%default=;51:пятьдесят одна;52:пятьдесят две;"
            + "=%default=;61:шестьдесят одна;62:шестьдесят две;"
            + "=%default=;71:семьдесят одна;72:семьдесят две;"
            + "=%default=;81:восемьдесят одна;82:восемьдесят две;"
            + "=%default=;91:девяносто одна;92:девяносто две;"
            + "=%default=;"
            + "100: сто[ >%alt>];"
            + "200: двести[ >%alt>];"
            + "300: триста[ >%alt>];"
            + "400: четыреста[ >%alt>];"
            + "500: пятьсот[ >%alt>];"
            + "600: шестьсот[ >%alt>];"
            + "700: семьсот[ >%alt>];"
            + "800: восемьсот[ >%alt>];"
            + "900: девятьсот[ >%alt>];"
            + "1000: <%alt< тысяча[ >%alt>];"
            + "2000: <%alt< тысячи[ >%alt>];"
            + "5000: <%alt< $(cardinal,one{тысяча} few{тысячи} other{тысяч})$[ >%alt>];"
            + "1 000 000: <%default< миллион[ >>];"
            + "2 000 000: <%default< миллиона[ >>];"
            + "5 000 000: <%default< $(cardinal,one{миллион} few{миллиона} other{миллионов})$[ >>];"
            + "1 000 000 000: <%default< миллиард[ >>];"
            + "2 000 000 000: <%default< миллиарда[ >>];"
            + "5 000 000 000: <%default< $(cardinal,one{миллиард} few{миллиарда} other{миллиардов})$[ >>];"
            + "1,000,000,000,000: =#,##0=;"

            + "%interest:"
            + "0:=%default=;одна;"
            + "=%default=;21:двадцать одна;"
            + "=%default=;31:тридцать одна;"
            + "=%default=;41:сорок одна;"
            + "=%default=;51:пятьдесят одна;"
            + "=%default=;61:шестьдесят одна;"
            + "=%default=;71:семьдесят одна;"
            + "=%default=;81:восемьдесят одна;"
            + "=%default=;91:девяносто одна;"
            + "=%default=;"
            + "100: сто[ >%interest>];"
            + "200: двести[ >%interest>];"
            + "300: триста[ >%interest>];"
            + "400: четыреста[ >%interest>];"
            + "500: пятьсот[ >%interest>];"
            + "600: шестьсот[ >%interest>];"
            + "700: семьсот[ >%interest>];"
            + "800: восемьсот[ >%interest>];"
            + "900: девятьсот[ >%interest>];"
            + "1000: <%alt< тысяча[ >%interest>];"
            + "2000: <%alt< тысячи[ >%interest>];"
            + "5000: <%alt< $(cardinal,one{тысяча} few{тысячи} other{тысяч})$[ >%interest>];"
            + "1 000 000: <%default< миллион[ >>];"
            + "2 000 000: <%default< миллиона[ >>];"
            + "5 000 000: <%default< $(cardinal,one{миллион} few{миллиона} other{миллионов})$[ >>];"
            + "1 000 000 000: <%default< миллиард[ >>];"
            + "2 000 000 000: <%default< миллиарда[ >>];"
            + "5 000 000 000: <%default< $(cardinal,one{миллиард} few{миллиарда} other{миллиардов})$[ >>];"
            + "1,000,000,000,000: =#,##0=;"
            ;

    protected final RuleBasedNumberFormat wordsFormatterMale;

    protected final RuleBasedNumberFormat wordsFormatterFemale;

    protected final DecimalFormatSymbols formatSymbols;

    protected DecimalFormat decimalFormat;

    {
        wordsFormatterMale = new RuleBasedNumberFormat(NUMBER_TO_WORDS_FORMAT, LOCALE_RU);
        wordsFormatterMale.setDefaultRuleSet("%default");

        wordsFormatterFemale = new RuleBasedNumberFormat(NUMBER_TO_WORDS_FORMAT, LOCALE_RU);
        wordsFormatterFemale.setDefaultRuleSet("%alt");

        formatSymbols = new DecimalFormatSymbols(LOCALE_RU);
        formatSymbols.setDecimalSeparator(',');
        formatSymbols.setGroupingSeparator(' ');

        decimalFormat = new DecimalFormat("###,##0.00", formatSymbols);
        decimalFormat.setRoundingMode(roundingMode().ordinal());
    }

    protected abstract String doEval(T value);

    protected abstract RoundingMode roundingMode();

    public String eval(T value) {
        if (value == null) {
            return null;
        }

        return doEval(value);
    }
}
