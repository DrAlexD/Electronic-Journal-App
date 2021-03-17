package com.example.electronicdiary;

import org.jetbrains.annotations.NotNull;

public class StudentEvent {
    private final String title;
    private final int points;

    public StudentEvent(String title, int points) {
        this.title = title;
        this.points = points;
    }

    @NotNull
    @Override
    public String toString() {
        return "StudentEvent{" +
                "title='" + title + '\'' +
                ", points='" + points + '\'' +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public int getPoints() {
        return points;
    }
}
