package com.example.electronicdiary.login;

import org.jetbrains.annotations.NotNull;

/**
 * A generic class that holds a result success w/ data or an error exception.
 */
public class LoginResult<T> {
    // hide the private constructor to limit subclass types (Success, Error)
    private LoginResult() {
    }

    @NotNull
    @Override
    public String toString() {
        if (this instanceof LoginResult.Success) {
            LoginResult.Success<T> success = (LoginResult.Success<T>) this;
            return "Success[data=" + success.getData().toString() + "]";
        } else if (this instanceof LoginResult.Error) {
            LoginResult.Error error = (LoginResult.Error) this;
            return "Error[exception=" + error.getError() + "]";
        }
        return "";
    }

    // Success sub-class
    public final static class Success<T> extends LoginResult<T> {
        private final T data;

        public Success(T data) {
            this.data = data;
        }

        public T getData() {
            return this.data;
        }
    }

    // Error sub-class
    public final static class Error extends LoginResult {
        private final String error;

        public Error(String error) {
            this.error = error;
        }

        public String getError() {
            return this.error;
        }
    }
}