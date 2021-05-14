package com.example.electronicdiary.data_classes;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class Lesson {
    private final Module module;
    private long id;

    private final Date dateAndTime;
    private final boolean isLecture;
    private final int pointsPerVisit;

    public Lesson(Module module, Date dateAndTime, boolean isLecture, int pointsPerVisit) {
        this.module = module;
        this.dateAndTime = dateAndTime;
        this.isLecture = isLecture;
        this.pointsPerVisit = pointsPerVisit;
    }

    public Lesson(long id, Module module, Date dateAndTime, boolean isLecture, int pointsPerVisit) {
        this.id = id;
        this.module = module;
        this.dateAndTime = dateAndTime;
        this.isLecture = isLecture;
        this.pointsPerVisit = pointsPerVisit;
    }

    @NotNull
    @Override
    public String toString() {
        return "Lesson{" + "id=" + id + ", dateAndTime=" + dateAndTime + ", isLecture=" +
                isLecture + ", pointsPerVisit=" + pointsPerVisit + '}';
    }

    public long getId() {
        return id;
    }

    public Module getModule() {
        return module;
    }

    public Date getDateAndTime() {
        return dateAndTime;
    }

    public boolean isLecture() {
        return isLecture;
    }

    public int getPointsPerVisit() {
        return pointsPerVisit;
    }
}
