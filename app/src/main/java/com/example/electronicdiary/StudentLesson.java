package com.example.electronicdiary;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class StudentLesson {
    private final Date date;
    private final int points;

    public StudentLesson(Date date, int points) {
        this.date = date;
        this.points = points;
    }

    @NotNull
    @Override
    public String toString() {
        return "StudentEvent{" +
                "title='" + date + '\'' +
                ", points='" + points + '\'' +
                '}';
    }

    public String getDate() {
        return date.getDate() + "." + (date.getMonth() + 1);
    }

    public int getPoints() {
        return points;
    }
}
