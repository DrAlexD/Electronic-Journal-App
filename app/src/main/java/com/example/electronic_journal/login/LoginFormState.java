package com.example.electronic_journal.login;

import androidx.annotation.Nullable;

import com.example.electronic_journal.R;

public class LoginFormState {
    @Nullable
    private final Integer usernameError;
    @Nullable
    private final Integer passwordError;
    private final boolean isDataValid;

    public LoginFormState(String username, String password) {
        boolean isUsernameValid = !username.trim().isEmpty();
        boolean isPasswordValid = password.trim().length() > 5;

        this.usernameError = !isUsernameValid ? R.string.invalid_empty_field : null;
        this.passwordError = !isPasswordValid ? R.string.invalid_password : null;
        this.isDataValid = isUsernameValid && isPasswordValid;
    }

    @Nullable
    public Integer getUsernameError() {
        return usernameError;
    }

    @Nullable
    public Integer getPasswordError() {
        return passwordError;
    }

    public boolean isDataValid() {
        return isDataValid;
    }
}