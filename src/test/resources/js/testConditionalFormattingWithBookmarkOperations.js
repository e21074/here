function doConditionalFormatting() {
    result.setIndexedTables(["Срок кредита в месяцах: #{#creditTerm}"]);
    result.setNamedTables({
        "table1": ["#{#penalDamagesPercents} годовых"]
    });
    result.setBookmarks({
        "newBookmarkRubleWordsLessJs": rubleLessWords.eval(100.01),
        "creditTerm": values.creditTerm * 2,
        "totalInterest": (values.penalDamagesPercents + values.totalInterest) / 2
    });
    return result;
}