package com.example.electronicdiary;

public class User {
    final int id;
    final String firstName;
    final String secondName;
    final String login;
    final String password;
    final boolean isProfessor;

    User(int id, String firstName, String secondName, boolean isProfessor) {
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
        this.login = firstName.toLowerCase() + secondName.toLowerCase() + id;
        this.password = "123456" + id;
        this.isProfessor = isProfessor;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public String getFullName() {
        return firstName + " " + secondName;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public boolean isProfessor() {
        return isProfessor;
    }
}
