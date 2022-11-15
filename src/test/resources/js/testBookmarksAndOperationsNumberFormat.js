function doConditionalFormatting() {
    result.setBookmarks({"decimal0": 1002, "decimal1": 100000002.1, "decimal2": 1002.12, "decimal3": 1002.123});
    result.setIndexedTables(["setCellText:1;2;3;true;[1002.5]", "setRowText:1;2;false;[1002.1;1200;1002.12]", "setRowText:1;2;false;[3002;abc;3002.12]"]);
    return result;
}