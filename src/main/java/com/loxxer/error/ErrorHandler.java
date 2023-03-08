package com.loxxer.error;

import java.util.ArrayList;
import java.util.List;

public class ErrorHandler {
    private boolean hadError;
    private List<LoxxerError> errors;

    public ErrorHandler(boolean hadError) {
        this.hadError = hadError;
        this.errors = new ArrayList<LoxxerError>();
    }

    public boolean hasErrors() {
        return this.hadError;
    }

    public void showErrors() {
        for (LoxxerError error : this.errors) {
            System.out.println(error.getErrorMessage());
        }
    }

    public void reportError(LoxxerError error) {
        this.hadError = this.hadError | true;
        this.errors.add(error);
    }
}
