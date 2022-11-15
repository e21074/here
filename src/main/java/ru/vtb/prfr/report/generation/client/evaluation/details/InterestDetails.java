package ru.vtb.prfr.report.generation.client.evaluation.details;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterestDetails {

    /** Отформатированное по паттерну значение с разделением на группы ("10 454,00", "10 454,34" и тд) */
    private String formattedValue;

    /** Отформатированная по паттерну целая часть значения */
    private String formattedIntegerPart;

    /** Отформатированная по паттерну дробная часть значения */
    private String formattedDecimalPart;

    /** Отформатированная по паттерну дробная часть значения */
    private Integer formattedDecimalPartValue;

    /** Значение (с дробной частью) прописью ("Один", "Две целых одна сотая", "Одиннадцать" и тд) */
    private String percentWords;

    /** Слово "процент" в соответствующем склонении */
    private String postfix;
}
