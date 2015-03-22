package com.pairoo.business.exceptions;

/**
 * Thrown if a technical problem happens.
 */
public class ReportProfileException extends Exception {
    private static final long serialVersionUID = 1L;

    public ReportProfileException(final Throwable cause) {
	super(cause);
    }
}
