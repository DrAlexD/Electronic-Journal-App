package com.example.electronicdiary;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Repository {
    private static volatile Repository repository;
    private final Cache cache = new Cache();
    private final Webservice webservice = new Retrofit.Builder().baseUrl("https://api.github.com/").
            addConverterFactory(GsonConverterFactory.create()).build().create(Webservice.class);

    private Repository() {
    }

    public static Repository getInstance() {
        if (repository == null) {
            repository = new Repository();
        }
        return repository;
    }

    public Cache getCache() {
        return cache;
    }

    public void setLastLoggedInUser(int userId) {
        //TODO поиск пользователя по id в таблицах студентов и преподавателей
        cache.setUser(new Professor(1, "xelagurd", "xelagurd"));
    }

    public void logout() {
        cache.setUser(null);
    }

    public Result<User> login(String username, String password) {
        Result<User> result;
        try {
            //TODO шифрование пароля и сравнивание его с полученным из базы даннных
            //TODO 2 поиска - по студентам и по преподавателям
            if ("xelagurd".equals(username) && "123456".equals(password)) {
                cache.setUser(new Professor(1, username, username));
                result = new Result.Success<>(cache.getUser());
            } else
                result = new Result.Error("Login failed");
        } catch (Exception e) {
            result = new Result.Error(e.getMessage());
        }

        return result;
    }
}