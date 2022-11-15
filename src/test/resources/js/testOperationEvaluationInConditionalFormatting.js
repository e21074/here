function doConditionalFormatting() {
    var BigDecimal = Java.type("java.math.BigDecimal");

    let interestWordsJs = 11;
    let interestWordsJsDecimal = 11.11;
    let dateWordsJs = "01.01.2020";
    let monthWordsJs = 1;
    let integerWordsJs = 0.9999;
    let fractionWordsJs = 21.00999;
    let numberWordsMJs = 9.999999;
    let numberWordsFJs = 1.9999999;
    let rubleWordsJs = 10454;

    let fractionIntegerJs = 1;
    let fractionJs = 2.0005;

    let bookmarks = {
        "interestWordsJs": interestWords.eval(interestWordsJs),
        "interestWordsJsDecimal": interestWords.eval(interestWordsJsDecimal),
        "interestWordsJava": interestWords.eval(values.interestWordsJava),
        "interestWordsJavaDecimal": interestWords.eval(values.interestWordsJavaDecimal),

        "interestWordsShortJs": interestWordsShort.eval(interestWordsJs),
        "interestWordsShortJsDecimal": interestWordsShort.eval(interestWordsJsDecimal),
        "interestWordsShortJava": interestWordsShort.eval(values.interestWordsJava),
        "interestWordsShortJavaDecimal": interestWordsShort.eval(values.interestWordsJavaDecimal),

        "dateWordsJs": dateWords.eval(dateWordsJs),
        "dateWordsJava": dateWords.eval(values.dateWordsJava),
        "monthWordsJs": monthWords.eval(monthWordsJs),
        "monthWordsJava": monthWords.eval(values.monthWordsJava),
        "monthWordsShortJs": monthWordsShort.eval(monthWordsJs),
        "monthWordsShortJava": monthWordsShort.eval(values.monthWordsJava),

        "integerWordsJs": integerWords.eval(integerWordsJs),
        "integerWordsJava": integerWords.eval(values.integerWordsJava),
        "fractionWordsJs": fractionWords.eval(fractionWordsJs),
        "fractionWordsJava": fractionWords.eval(values.fractionWordsJava),

        "numberWordsMJs": numberWordsM.eval(numberWordsMJs),
        "numberWordsMJava": numberWordsM.eval(values.numberWordsMJava),
        "numberWordsMJs1": numberWordsM.eval(numberWordsMJs, 1),
        "numberWordsMJava1": numberWordsM.eval(values.numberWordsMJava, 1),
        "numberWordsMJs3": numberWordsM.eval(numberWordsMJs, 3),
        "numberWordsMJava3": numberWordsM.eval(values.numberWordsMJava, 3),
        "numberWordsMJs5": numberWordsM.eval(numberWordsMJs, 5),
        "numberWordsMJava5": numberWordsM.eval(values.numberWordsMJava, 5),
        "numberWordsFJs": numberWordsF.eval(numberWordsFJs),
        "numberWordsFJava": numberWordsF.eval(values.numberWordsFJava),

        "rubleWordsJs": rubleWords.eval(rubleWordsJs),
        "rubleWordsJava": rubleWords.eval(values.rubleWordsJava),
        "rubleWordsShortJs": rubleWordsShort.eval(rubleWordsJs),
        "rubleWordsShortJava": rubleWordsShort.eval(values.rubleWordsJava),
        "rubleWordsLessJs": rubleLessWords.eval(rubleWordsJs),
        "rubleWordsLessJava": rubleLessWords.eval(values.rubleWordsJava),

        "fractionIntegerJs": fraction.eval(fractionIntegerJs),
        "fractionIntegerJava": fraction.eval(values.fractionIntegerJava),
        "fractionIntegerToDoubleJava": fraction.eval(new BigDecimal(String(values.fractionIntegerJava)).doubleValue()),

        "fractionJs": fraction.eval(fractionJs),
        "fractionJava": fraction.eval(values.fractionJava)
    };

    result.setIndexedTables([]);
    result.setBookmarks(bookmarks);
    return result;
}