package com.example.electronicdiary;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class Lesson {
    private final int id;
    private final int moduleNumber;
    private final int groupId;
    private final int subjectId;
    private final int lecturerId;
    private final int seminarianId;
    private final int semesterId;

    private final Date dateAndTime;
    private final boolean isLecture;
    private final int pointsPerVisit;

    public Lesson(int id, int moduleNumber, int groupId, int subjectId, int lecturerId,
                  int seminarianId, int semesterId, Date dateAndTime, boolean isLecture, int pointsPerVisit) {
        this.id = id;
        this.moduleNumber = moduleNumber;
        this.groupId = groupId;
        this.subjectId = subjectId;
        this.lecturerId = lecturerId;
        this.seminarianId = seminarianId;
        this.semesterId = semesterId;
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

    public int getId() {
        return id;
    }

    public int getModuleNumber() {
        return moduleNumber;
    }

    public int getGroupId() {
        return groupId;
    }


    public int getSubjectId() {
        return subjectId;
    }

    public int getLecturerId() {
        return lecturerId;
    }

    public int getSeminarianId() {
        return seminarianId;
    }

    public int getSemesterId() {
        return semesterId;
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
