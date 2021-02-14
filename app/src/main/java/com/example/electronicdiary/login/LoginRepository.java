package com.example.electronicdiary.login;

public class LoginRepository {
    private static volatile LoginRepository instance;
    private LoggedInUser user = null;

    private LoginRepository() {
    }

    public static LoginRepository getInstance() {
        if (instance == null) {
            instance = new LoginRepository();
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public void logout() {
        user = null;
    }

    private void setLoggedInUser(LoggedInUser user) {
        this.user = user;
    }

    public LoginResult<LoggedInUser> login(String username, String password) {
        LoginResult<LoggedInUser> loginResult;
        try {
            //TODO шифрование пароля и сравнивание его с полученным из базы даннных
            if ("xelagurd".equals(username) && "123456".equals(password)) {
                LoggedInUser user =
                        new LoggedInUser(
                                java.util.UUID.randomUUID().toString(),
                                username);
                loginResult = new LoginResult.Success<>(user);
                setLoggedInUser(((LoginResult.Success<LoggedInUser>) loginResult).getData());
            } else
                loginResult = new LoginResult.Error("Login failed");
        } catch (Exception e) {
            loginResult = new LoginResult.Error(e.getMessage());
        }

        return loginResult;
    }
}