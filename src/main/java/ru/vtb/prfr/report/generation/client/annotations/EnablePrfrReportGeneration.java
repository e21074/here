package ru.vtb.prfr.report.generation.client.annotations;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.vtb.prfr.report.generation.client.config.ReportAttributesConfig;

import java.lang.annotation.*;

/**
 * Enables prfr-generation-client functionality
 *
 * In order to start work with prfr-generation-client, please, put this
 * annotation on any class, marked {@link Configuration}.
 *
 * @author Mikhail Polivakha
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ReportAttributesConfig.class)
public @interface EnablePrfrReportGeneration {
}