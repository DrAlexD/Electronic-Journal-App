package com.example.electronicdiary.data_classes;

import org.jetbrains.annotations.NotNull;

public class Student extends User {
    private final Group group;

    public Student(long id, String firstName, String secondName, Group group, String role) {
        super(id, firstName, secondName, role, false);
        this.group = group;
    }

    public Student(long id, String firstName, String secondName, Group group, String username, String password, String role) {
        super(id, firstName, secondName, username, password, role, false);
        this.group = group;
    }

    public long getGroupId() {
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
