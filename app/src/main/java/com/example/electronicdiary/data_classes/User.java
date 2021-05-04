package com.example.electronicdiary.data_classes;

public class User {
    final long id;
    final String firstName;
    final String secondName;
    final String role;
    final boolean isProfessor;
    String username;
    String password;

    User(long id, String firstName, String secondName, String role, boolean isProfessor) {
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
        this.role = role;
        this.isProfessor = isProfessor;
    }

    User(long id, String firstName, String secondName, String username, String password, String role, boolean isProfessor) {
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
        this.username = username;
        this.password = password;
        this.role = role;
        this.isProfessor = isProfessor;
    }

    public long getId() {
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

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public boolean isProfessor() {
        return isProfessor;
    }
}
