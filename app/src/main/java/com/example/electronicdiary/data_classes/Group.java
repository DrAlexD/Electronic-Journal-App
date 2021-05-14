package com.example.electronicdiary.data_classes;

import org.jetbrains.annotations.NotNull;

public class Group {
    private long id;
    private final String title;

    public Group(String title) {
        this.title = title;
    }

    public Group(long id, String title) {
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
        return "Group{" + "id=" + id + ", title='" + title + '\'' + '}';
    }
}
