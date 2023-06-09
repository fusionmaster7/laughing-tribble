package com.loxxer.error;

/**
 * Interface for Error Type for each phase in the interpreter process
 */
public interface ILoxxerError {
    public String getErrorMessage();

    public LoxxerErrorType getErrorType();
}
