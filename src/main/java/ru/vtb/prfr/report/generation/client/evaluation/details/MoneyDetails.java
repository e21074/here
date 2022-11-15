package ru.vtb.prfr.report.generation.client.evaluation.details;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoneyDetails {

    /** Значение прописью ("Один", "Два", "Одиннадцать" и тд) */
    private String rubleWords;

    /** Значение дробной части суммы, копейки ("01", "11" и тд) */
    private String kopeckNumber;

    /** Слово "рубли" в соответствующем склонении */
    private String rublePostfix;

    /** Слово "копейки" в соответствующем склонении */
    private String kopeckPostfix;

    /** Отформатированное по паттерну значение с разделением на группы ("10 454,00", "10 454,34" и тд) */
    private String formattedValue;
}
