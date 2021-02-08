package com.example.electronicdiary.search;

import org.jetbrains.annotations.NotNull;

public class Student {
    private final String group;
    private final String firstName;
    private final String secondName;

    Student(String group, String firstName, String secondName) {
        this.group = group;
        this.firstName = firstName;
        this.secondName = secondName;
    }

    @NotNull
    @Override
    public String toString() {
        return "Student{" +
                "group='" + group + '\'' +
                ", name='" + firstName + secondName + '\'' +
                '}';
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public String getName() {
        return firstName + " " + secondName;
    }

    public String getGroup() {
        return group;
    }
}
