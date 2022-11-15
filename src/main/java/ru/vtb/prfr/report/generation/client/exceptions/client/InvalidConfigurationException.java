package ru.vtb.prfr.report.generation.client.exceptions.client;

/**
 * Exception, that is thrown in case the client has configured the prfr-preport-generation-client library incorrectly.
 *
 * @author Mikhail Polivakha
 */
public class InvalidConfigurationException extends RuntimeException {

    public InvalidConfigurationException(String message) {
        super(message);
    }

    public InvalidConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}