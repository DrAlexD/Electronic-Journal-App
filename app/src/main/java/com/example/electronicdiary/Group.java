package com.example.electronicdiary;

import org.jetbrains.annotations.NotNull;

public class Group {
    private final int id;
    private final String title;

    public Group(int id, String title) {
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
        return "Group{" + "id=" + id + ", title='" + title + '\'' + '}';
    }
}
