package com.example.electronicdiary.data_classes;

import org.jetbrains.annotations.NotNull;

public class Professor extends User {
    public Professor(long id, String firstName, String secondName, String role) {
        super(id, firstName, secondName, role, true);
    }

    public Professor(long id, String firstName, String secondName, String username, String password, String role) {
        super(id, firstName, secondName, username, password, role, true);
    }

    @NotNull
    @Override
    public String toString() {
        return "Professor{" + "id=" + id + ", firstName='" + firstName + '\'' + ", secondName='" +
                secondName + '\'' + '}';
    }
}
