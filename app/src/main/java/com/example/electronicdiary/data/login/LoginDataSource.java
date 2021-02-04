package com.example.electronicdiary.data.login;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {

        try {
            //TODO шифрование пароля и сравнивание его с полученным из базы даннных
            if ("xelagurd".equals(username) && "123456".equals(password)) {
                LoggedInUser user =
                        new LoggedInUser(
                                java.util.UUID.randomUUID().toString(),
                                username);
                return new Result.Success<>(user);
            } else
                return new Result.Error("Login failed");
        } catch (Exception e) {
            return new Result.Error(e.getMessage());
        }
    }

    public void logout() {
        // TODO выход из приложения
    }
}