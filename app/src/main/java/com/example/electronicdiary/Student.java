package com.example.electronicdiary;

import org.jetbrains.annotations.NotNull;

public class Student extends User {
    private final int groupId;

    public Student(int id, String firstName, String secondName, int groupId) {
        super(id, firstName, secondName, false);
        this.groupId = groupId;
    }

    public int getGroupId() {
        return groupId;
    }

    @NotNull
    @Override
    public String toString() {
        return "Student{" + "id=" + id + ", firstName='" + firstName + '\'' + ", secondName='" +
                secondName + '\'' + ", group='" + groupId + '\'' + '}';
    }
}
