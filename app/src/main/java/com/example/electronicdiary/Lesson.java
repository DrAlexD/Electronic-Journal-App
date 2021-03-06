package com.example.electronicdiary;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class Lesson {
    private final Date date;
    private final int points;

    public Lesson(Date date, int points) {
        this.date = date;
        this.points = points;
    }

    @NotNull
    @Override
    public String toString() {
        return "Event{" +
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
