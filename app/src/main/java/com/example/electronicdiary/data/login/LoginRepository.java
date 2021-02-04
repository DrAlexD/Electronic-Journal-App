package com.example.electronicdiary.data.login;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {

    private static volatile LoginRepository instance;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private LoggedInUser user = null;

    // private constructor : singleton access
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
        // TODO выход из приложения
    }

    private void setLoggedInUser(LoggedInUser user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
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