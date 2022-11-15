package ru.vtb.prfr.report.generation.client.client;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.lang.NonNull;
import ru.vtb.prfr.report.generation.client.exceptions.client.InvalidClientInputException;

import java.nio.file.Path;

/**
 * This is a key interface that holds logic of generation of the report using corresponding configuration
 * Should be used by clients of the library directly, via simple dependency injection mechanism.
 *
 * @author Mikhail Polivakha
 */
public interface ReportsGeneratorClient {

    /**
     * Generates pdf form for the specified report using specified values
     *
     * @param sysName - sys name of the report that should be generated
     * @param rootJsonDataNode - json as jackson {@link JsonNode}. This json should contain properties to be set in the bookmarks,
     *                          where the key - attribute name, and the value - corresponding value
     * @throws InvalidClientInputException in case the provided data json will not pass validation check
     * @return the path to the file on the local file system, where generated report is stored
     */
    default Path generatePDF(@NonNull String sysName,
                             @NonNull JsonNode rootJsonDataNode,
                             @NonNull String pdfFormat) throws InvalidClientInputException {
        throw new UnsupportedOperationException();
    }

    /**
     * Generates word form for the specified report using specified values
     *
     * @param sysName - sys name of the report that should be generated
     * @param rootJsonDataNode - json as jackson {@link JsonNode}. This json should contain properties to be set in the bookmarks,
     *                          where the key - attribute name, and the value - corresponding value
     * @throws InvalidClientInputException in case the provided data json will not pass validation check
     * @return the path to the file on the local file system, where generated report is stored
     */
    default Path generateWord(@NonNull String sysName,
                              @NonNull JsonNode rootJsonDataNode) throws InvalidClientInputException {
        throw new UnsupportedOperationException();
    }

}