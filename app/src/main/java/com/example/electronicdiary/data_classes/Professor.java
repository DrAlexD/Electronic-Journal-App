package com.example.electronicdiary.data_classes;

import org.jetbrains.annotations.NotNull;

public class Professor extends User {
    public Professor(int id, String firstName, String secondName) {
        super(id, firstName, secondName, true);
    }

    public Professor(int id, String firstName, String secondName, String login, String password) {
        super(id, firstName, secondName, login, password, true);
    }

    @NotNull
    @Override
    public String toString() {
        return "Professor{" + "id=" + id + ", firstName='" + firstName + '\'' + ", secondName='" +
                secondName + '\'' + '}';
    }
}
