package ru.vtb.prfr.report.generation.client.client.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class encapsulating the results of running the conditional formatting code.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConditionalFormatting {

    /**
     * IndexedTables element containing a list of operations with table id and other parameters:
     * ["removeTable:idTable;minRows", "removeRow:idTable;idRow", ...]
     */
    private List<String> indexedTables = new ArrayList<>();

    /**
     * NamedTables element containing a map of lists of operations with table caption as key and list of operations as value:
     * { "tableName1": ["removeTable:minRows", "removeRow:idRow"], ... }
     */
    private Map<String, List<String>> namedTables = new HashMap<>();

    /**
     * Map of bookmarks added/modified by the conditional formatting code with bookmark sysName as key and bookmark value as value.
     */
    private Map<String, Object> bookmarks = new HashMap<>();

    private Map<String, String> bookmarksOperation = new HashMap<>();

    /**
     * Map of an images added by the conditional formatting code with image name as key and base64 encoded image file content as value.
     * Facsimile stored in this field with "facsimile" key.
     */
    private Map<String, String> images = new HashMap<>();

    /**
     * Map of barcodes added by the conditional formatting code with barcode name as key and barcode format and value as value.
     * {"barcode1": {"format": "CODE_128", "value": "VALUE"}}
     */
    private Map<String, Map<String, String>> barcodes = new HashMap<>();

    public Map<String, Object> getAdditionallyProvidedBookmarks() {
        return this.bookmarks;
    }
}
