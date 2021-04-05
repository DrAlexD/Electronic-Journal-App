package com.example.electronicdiary;

import org.jetbrains.annotations.NotNull;

public class Student extends User {
    private final Group group;

    public Student(int id, String firstName, String secondName, Group group) {
        super(id, firstName, secondName, false);
        this.group = group;
    }

    public int getGroupId() {
        return group.getId();
    }

    public String getGroupTitle() {
        return group.getTitle();
    }

    @NotNull
    @Override
    public String toString() {
        return "Student{" + "id=" + id + ", firstName='" + firstName + '\'' + ", secondName='" +
                secondName + '\'' + ", group='" + getGroupTitle() + '\'' + '}';
    }
}
