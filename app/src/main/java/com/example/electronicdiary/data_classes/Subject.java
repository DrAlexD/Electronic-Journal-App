package com.example.electronicdiary.data_classes;

import org.jetbrains.annotations.NotNull;

public class Subject {
    private long id;
    private final String title;

    public Subject(String title) {
        this.title = title;
    }

    public Subject(long id, String title) {
        this.id = id;
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    @NotNull
    @Override
    public String toString() {
        return title;
    }
}
