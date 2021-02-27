package com.example.electronicdiary.search;

import org.jetbrains.annotations.NotNull;

public class Professor {
    private final String firstName;
    private final String secondName;

    public Professor(String firstName, String secondName) {
        this.firstName = firstName;
        this.secondName = secondName;
    }

    @NotNull
    @Override
    public String toString() {
        return "Professor{" + "name='" + firstName + secondName + '\'' + '}';
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
}
