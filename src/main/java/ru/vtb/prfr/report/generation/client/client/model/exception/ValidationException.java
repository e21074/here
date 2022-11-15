package ru.vtb.prfr.report.generation.client.client.model.exception;

/**
 * Contains single validation exception
 *
 * @see ValidationExceptionProfile
 * @author Mikhail Polivakha
 */
public class ValidationException {

    /**
     * Represents the report attribute that have faild validation
     */
    private String offendingReportAttributeName;

    /**
     * Error message
     */
    private String errorMessage;

    public ValidationException(String offendingReportProperyPath, String errorMessage) {
        this.offendingReportAttributeName = offendingReportProperyPath;
        this.errorMessage = errorMessage;
    }

    public String getOffendingReportAttributeName() {
        return offendingReportAttributeName;
    }

    public void setOffendingReportAttributeName(String offendingReportAttributeName) {
        this.offendingReportAttributeName = offendingReportAttributeName;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
