package com.pairoo.business.exceptions;

/**
 * Thrown if a technical problem during registration process happens.
 */
public class RegistrationException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private Code code;

    public RegistrationException(final Throwable cause) {
        super(cause);
    }

    public void setCode(Code code) {
        this.code = code;
    }

    public Code getCode() {
        return code;
    }

    public enum Code {

        SEND_CONFIRMATION_EMAIL;
    }
}
