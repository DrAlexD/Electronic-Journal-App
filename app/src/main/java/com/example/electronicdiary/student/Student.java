package com.example.electronicdiary.student;

import org.jetbrains.annotations.NotNull;

public class Student {
    private final String firstName;
    private final String secondName;
    private String group;

    public Student(String group, String firstName, String secondName) {
        this.group = group;
        this.firstName = firstName;
        this.secondName = secondName;
    }

    public Student(String firstName, String secondName) {
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

    public String getFullName() {
        return firstName + " " + secondName;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
