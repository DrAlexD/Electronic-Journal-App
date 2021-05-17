package com.example.electronicdiary.data_classes;

import org.jetbrains.annotations.NotNull;

public class Professor extends User {
    public Professor(String firstName, String secondName, String username, String password, String role) {
        super(firstName, secondName, username, password, role);
    }

    public Professor(long id, String firstName, String secondName, String username, String password, String role) {
        super(id, firstName, secondName, username, password, role);
    }

    @NotNull
    @Override
    public String toString() {
        return "Professor{" + "id=" + id + ", firstName='" + firstName + '\'' + ", secondName='" +
                secondName + '\'' + '}';
    }
}
