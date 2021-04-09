package com.example.electronicdiary.data_classes;

public class User {
    final int id;
    final String firstName;
    final String secondName;
    final boolean isProfessor;

    String login;
    String password;

    User(int id, String firstName, String secondName, boolean isProfessor) {
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
        this.isProfessor = isProfessor;
    }

    User(int id, String firstName, String secondName, String login, String password, boolean isProfessor) {
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
        this.login = login;
        this.password = password;
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
