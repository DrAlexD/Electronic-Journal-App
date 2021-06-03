package com.example.electronic_journal.data_classes;

import org.jetbrains.annotations.NotNull;

public class Student extends User {
    private final Group group;

    public Student(String firstName, String secondName, Group group, String username, String password, String role) {
        super(firstName, secondName, username, password, role);
        this.group = group;
    }

    public Student(long id, String firstName, String secondName, Group group, String username, String password, String role) {
        super(id, firstName, secondName, username, password, role);
        this.group = group;
    }

    public Group getGroup() {
        return group;
    }

    @NotNull
    @Override
    public String toString() {
        return "Student{" + "id=" + id + ", firstName='" + firstName + '\'' + ", secondName='" +
                secondName + '\'' + ", group='" + getGroup().getTitle() + '\'' + '}';
    }
}
