package com.principal.trabajofindegrado;

public class ValidationResult {
    private boolean isValid;
    private User user;

    public ValidationResult(boolean isValid, User user) {
        this.isValid = isValid;
        this.user = user;
    }

    public boolean isValid() {
        return isValid;
    }

    public User getUser() {
        return user;
    }
}

