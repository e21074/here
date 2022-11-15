package ru.vtb.prfr.report.generation.client.client.model;

/**
 * This object encapsulates the facsimile information
 *
 * @author Mikhail Polivakha
 */
public class ReportGroupFacsimile {

    /**
     * The report group (or, in other words, the business stream), to which the {@link #facsimile} is bind
     */
    private final String reportGroup;

    /**
     * Facsimile image itself
     */
    private final byte[] facsimile;

    public ReportGroupFacsimile(String reportGroup, byte[] facsimile) {
        this.reportGroup = reportGroup;
        this.facsimile = facsimile;
    }

    public String getReportGroup() {
        return reportGroup;
    }

    public byte[] getFacsimile() {
        return facsimile;
    }

    @Override
    public String toString() {
        return "ReportGroupFacsimile{" +
                "reportGroup='" + reportGroup + '\'' +
                '}';
    }
}