package com.example.electronicdiary.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;
import com.example.electronicdiary.Result;
import com.example.electronicdiary.User;

public class LoginViewModel extends ViewModel {
    private final MutableLiveData<Result<User>> user = new MutableLiveData<>();
    private final MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();

    LiveData<Result<User>> getUser() {
        return user;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    public void login(String username, String password) {
        user.setValue(Repository.getInstance().login(username, password));
    }

    public void loginDataChanged(String username, String password) {
        loginFormState.setValue(new LoginFormState(username, password));
    }
}