package com.example.electronicdiary;

import org.jetbrains.annotations.NotNull;

public class Professor extends User {
    public Professor(int id, String firstName, String secondName) {
        super(id, firstName, secondName, true);
    }

    @NotNull
    @Override
    public String toString() {
        return "Professor{" + "id=" + id + ", firstName='" + firstName + '\'' + ", secondName='" +
                secondName + '\'' + '}';
    }
}
