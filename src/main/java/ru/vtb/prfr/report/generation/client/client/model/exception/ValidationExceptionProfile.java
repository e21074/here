package ru.vtb.prfr.report.generation.client.client.model.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * Class, that encapsulates information about exceptions occurred during validation
 *
 * @author Mikhail Polivakha
 */
public class ValidationExceptionProfile {
    private final List<ValidationException> validationExceptions;

    public ValidationExceptionProfile() {
        this.validationExceptions = new ArrayList<>();
    }

    public void addValidationException(ValidationException validationException) {
        this.validationExceptions.add(validationException);
    }

    public boolean noErrorFound() {
        return validationExceptions.isEmpty();
    }

    public int amountOfErrorsFound() {
        return validationExceptions.size();
    }

    public static ValidationExceptionProfile createEmptyBookmarkJsonProvidedForReport(String reportSysName) {
        ValidationExceptionProfile validationExceptionProfile = new ValidationExceptionProfile();
        validationExceptionProfile.addValidationException(new ValidationException("ALL", String.format("Provided bookmarks json for report '%s' is empty", reportSysName)));
        return validationExceptionProfile;
    }

    public List<ValidationException> getValidationExceptions() {
        return validationExceptions;
    }
}