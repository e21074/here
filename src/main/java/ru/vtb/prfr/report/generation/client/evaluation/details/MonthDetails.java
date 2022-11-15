package ru.vtb.prfr.report.generation.client.evaluation.details;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthDetails {

    /** Отформатированное по паттерну значение с разделением на группы ("10 454", "11" и тд) */
    private String formattedValue;

    /** Значение прописью ("Один", "Два", "Одиннадцать" и тд) */
    private String monthsWords;

    /** Слово "месяц" в соответствующем склонении */
    private String postfix;
}
