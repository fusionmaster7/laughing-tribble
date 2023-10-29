package com.loxxer.error;

import java.util.ArrayList;
import java.util.List;

public class ErrorHandler {
    private boolean hadError;
    private boolean hadRuntimeError;
    private List<LoxxerError> errors;

    public ErrorHandler(boolean hadError) {
        this.hadError = hadError;
        this.errors = new ArrayList<LoxxerError>();
        this.hadRuntimeError = false;
    }

    public boolean hasErrors() {
        return this.hadError;
    }

    public boolean hasRuntimeErrors() {
        return this.hadRuntimeError;
    }

    public void showErrors() {
        for (LoxxerError error : this.errors) {
            System.out.println(error.getErrorMessage());
        }
    }

    public void reportError(LoxxerError error) {
        if (error.getErrorType() == LoxxerErrorType.RUNTIME_ERROR) {
            this.hadRuntimeError = this.hadRuntimeError | true;
        } else {
            this.hadError = this.hadError | true;

        }
        this.errors.add(error);
    }

}
