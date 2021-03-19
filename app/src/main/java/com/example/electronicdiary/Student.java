package com.example.electronicdiary;

import org.jetbrains.annotations.NotNull;

public class Student extends User {
    private final String group;

    public Student(int id, String group, String firstName, String secondName) {
        super(id, firstName, secondName, false);
        this.group = group;
    }

    public String getGroup() {
        return group;
    }

    @NotNull
    @Override
    public String toString() {
        return "Student{" + "id=" + id + ", firstName='" + firstName + '\'' + ", secondName='" +
                secondName + '\'' + ", group='" + group + '\'' + '}';
    }
}
