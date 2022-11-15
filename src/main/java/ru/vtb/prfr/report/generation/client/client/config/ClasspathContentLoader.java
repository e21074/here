package ru.vtb.prfr.report.generation.client.client.config;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Component;
import ru.vtb.prfr.report.generation.client.exceptions.client.InvalidConfigurationException;
import ru.vtb.prfr.report.generation.client.exceptions.internal.InternalFatalException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * This class is responsible for finding resources in classpath.
 *
 * @author Mikhail Polivakha
 */
@Component
public final class ClasspathContentLoader {

    @Autowired
    private ResourceLoader resourceLoader;

    /**
     * The root directories structure inside the JAR classpath, where PRFR 1875 config attributes and conditional formatting are located
     */
    @Value("${report.config.files.location.attribute:/reports-generation/reports-meta/}")
    private String reportsAttributesConfigLocation;

    /**
     * The general name of the file, that contains conditional formatting. Set globally, for all reports
     */
    @Value("${report.file.name.conditional-formatting:conditional_formatting.js}")
    private String conditionalFormattingFileName;

    @Value("${report.file.name.open-api-spec:}")
    private String openApiSpecFileLocationInClasspath;

    /**
     * Contains cached files. if the file already was loaded then for performance reasons we will took file content from cache
     * key - the name of the file
     * value - the file content itself
     */
    private final ConcurrentMap<String, String> localCache;

    public ClasspathContentLoader() {
        localCache = new ConcurrentHashMap<>();
    }

    /**
     * Finds a resources in current JAR classpath.
     * @param foldersStructureInsideJar - folders structure <b>from root of the classpath</b>, where to seek for resources.
     *                                    May also accept wildcards and other regexp patterns.
     * @return the array of all found resources in {@code foldersStructureInsideJar}, also considering the patterns
     */
    public Resource[] loadEntriesInsidePath(String foldersStructureInsideJar) {
        try {
            return ResourcePatternUtils
                    .getResourcePatternResolver(resourceLoader)
                    .getResources("classpath:" + foldersStructureInsideJar);
        } catch (IOException e) {
            throw new InternalFatalException(e);
        }
    }

    /**
     * Load file of conditional formatting for report with specified sys name
     * @param reportSysName - sys name of the report, for whom conditional formatting should be loaded
     * @return - conditional formatting javascript script file as string
     */
    public Optional<String> loadConditionalFormattingResource(String reportSysName) {
        String pathToConditionalFormattingFile = reportsAttributesConfigLocation + reportSysName + "/" + conditionalFormattingFileName;
        return loadOptionalResourceLocatedInClasspath(pathToConditionalFormattingFile);
    }

    /**
     * @return the raw content of open api spec file
     */
    public String readOpenApiFile() {
        localCache.putIfAbsent(openApiSpecFileLocationInClasspath, loadResourceLocatedInClasspath(openApiSpecFileLocationInClasspath));
        return localCache.get(openApiSpecFileLocationInClasspath);
    }

    private String loadResourceLocatedInClasspath(String pathToFile) {
        try {
            return IOUtils.toString(new ClassPathResource(pathToFile).getInputStream(), StandardCharsets.UTF_8);
        } catch (FileNotFoundException e) {
            throw new InvalidConfigurationException(
                    String.format("File '%s' was not found", pathToFile)
            );
        } catch (IOException e) {
            throw new InternalFatalException(e);
        }
    }

    private Optional<String> loadOptionalResourceLocatedInClasspath(String pathToFile) {
        try {
            return Optional.of(IOUtils.toString(new ClassPathResource(pathToFile).getInputStream(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            return Optional.empty();
        }
    }
}