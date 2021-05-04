package com.example.electronicdiary.data_classes;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class Lesson {
    private final long id;
    private final int moduleNumber;
    private final long groupId;
    private final long subjectId;
    private final long lecturerId;
    private final long seminarianId;
    private final long semesterId;

    private final Date dateAndTime;
    private final boolean isLecture;
    private final int pointsPerVisit;

    public Lesson(long id, int moduleNumber, long groupId, long subjectId, long lecturerId,
                  long seminarianId, long semesterId, Date dateAndTime, boolean isLecture, int pointsPerVisit) {
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

    public long getId() {
        return id;
    }

    public int getModuleNumber() {
        return moduleNumber;
    }

    public long getGroupId() {
        return groupId;
    }


    public long getSubjectId() {
        return subjectId;
    }

    public long getLecturerId() {
        return lecturerId;
    }

    public long getSeminarianId() {
        return seminarianId;
    }

    public long getSemesterId() {
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
