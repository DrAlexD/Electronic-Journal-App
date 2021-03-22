package com.example.electronicdiary;

import org.jetbrains.annotations.NotNull;

public class Subject {
    private final int id;
    private final String title;

    public Subject(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    @NotNull
    @Override
    public String toString() {
        return "Subject{" + "id=" + id + ", title='" + title + '\'' + '}';
    }
}
