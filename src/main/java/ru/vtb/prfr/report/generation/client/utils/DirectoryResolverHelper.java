package ru.vtb.prfr.report.generation.client.utils;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Mikhail Polivakha
 */
@Component
public class DirectoryResolverHelper {

    public String getParentDirectoryNameOfTheResource(Resource resource) throws IOException {
        String parentDirectoryOfFacsimileFile = resource
                .getURL()
                .toString()
                .substring(0, resource.getURL().toString().lastIndexOf("/"));

        return parentDirectoryOfFacsimileFile
                .substring(parentDirectoryOfFacsimileFile.lastIndexOf("/") + 1);
    }

    public String getFileNameOfClassPathResource(Resource resource) {
        String name = resource.getFilename();
        return name.substring(0, name.lastIndexOf('.'));
    }
}