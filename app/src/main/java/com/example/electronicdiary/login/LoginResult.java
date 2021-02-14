package com.example.electronicdiary.login;

import org.jetbrains.annotations.NotNull;

class LoginResult<T> {
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

    public final static class Success<T> extends LoginResult<T> {
        private final T data;

        public Success(T data) {
            this.data = data;
        }

        public T getData() {
            return this.data;
        }
    }

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