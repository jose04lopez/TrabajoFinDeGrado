package com.principal.trabajofindegrado;

public class ValidationResult {
    private boolean isValid;
    private User user;
    private String userId;

    public ValidationResult(boolean isValid, User user, String userId) {
        this.isValid = isValid;
        this.user = user;
        this.userId = userId;
    }

    public boolean isValid() {
        return isValid;
    }

    public User getUser() {
        return user;
    }

    public String getUserId() {
        return userId;
    }
}


