package ru.vtb.prfr.report.generation.client.exceptions.client;

import ru.vtb.prfr.report.generation.client.client.model.exception.ValidationExceptionProfile;
import ru.vtb.prfr.report.generation.client.internal.service.validation.ClientDataValidator;

/**
 * Thrown to the client in case the validation stage failed
 *
 * @see ClientDataValidator
 * @author Mikhail Polivakha
 */
public class InvalidClientInputException extends RuntimeException {

    private final ValidationExceptionProfile validationExceptionProfile;

    public InvalidClientInputException(ValidationExceptionProfile validationExceptionProfile) {
        this.validationExceptionProfile = validationExceptionProfile;
    }

    @Override
    public String getMessage() {
        StringBuilder accumulatedErrorMessage = new StringBuilder(
                String.format("Encountered '%s' errors: ", validationExceptionProfile.amountOfErrorsFound()) + System.lineSeparator()
        );
        validationExceptionProfile.getValidationExceptions().forEach(e -> {
            accumulatedErrorMessage.append(e.getErrorMessage()).append(System.lineSeparator());
        });
        return accumulatedErrorMessage.toString();
    }
}