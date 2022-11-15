package ru.vtb.prfr.report.generation.client.exceptions.internal;

/**
 * Exception for all internal errors of prfr report generation client that are not expected
 *
 * @author Mikhail Polivakha
 */
public class InternalFatalException extends RuntimeException {

    public InternalFatalException() {
        super();
    }

    public InternalFatalException(String message) {
        super(message);
    }

    public InternalFatalException(Throwable cause) {
        super(cause);
    }

    public InternalFatalException(String message, Throwable cause) {
        super(message, cause);
    }
}