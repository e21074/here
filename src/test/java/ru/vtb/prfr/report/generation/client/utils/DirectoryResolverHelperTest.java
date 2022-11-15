package ru.vtb.prfr.report.generation.client.utils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class DirectoryResolverHelperTest {

    @Test
    void getParentDirectoryNameOfTheResource() throws IOException {
        DirectoryResolverHelper directoryResolverHelper = new DirectoryResolverHelper();
        String parentDir = directoryResolverHelper.getParentDirectoryNameOfTheResource(
                new FileSystemResource("test-files/directoryResolverHelperTest")
        );
        Assertions.assertThat(parentDir).isEqualTo("test-files");
    }
}