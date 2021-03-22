package com.example.electronicdiary;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class Event {
    private final int id;
    private final int moduleNumber;
    private final int groupId;
    private final int subjectId;
    private final int lecturerId;
    private final int seminarianId;
    private final int semesterId;

    private final String type;
    private final int number;
    private final Date startDate;
    private final Date deadlineDate;
    private final int minPoints;
    private final int maxPoints;

    public Event(int id, int moduleNumber, int groupId, int subjectId, int lecturerId, int seminarianId,
                 int semesterId, String type, int number, Date startDate, Date deadlineDate, int minPoints, int maxPoints) {
        this.id = id;
        this.moduleNumber = moduleNumber;
        this.groupId = groupId;
        this.subjectId = subjectId;
        this.lecturerId = lecturerId;
        this.seminarianId = seminarianId;
        this.semesterId = semesterId;
        this.type = type;
        this.number = number;
        this.startDate = startDate;
        this.deadlineDate = deadlineDate;
        this.minPoints = minPoints;
        this.maxPoints = maxPoints;
    }

    @NotNull
    @Override
    public String toString() {
        return "Event{" + "type='" + type + '\'' + ", number=" + number + ", startDate=" + startDate +
                ", deadlineDate=" + deadlineDate + ", minPoints=" + minPoints + ", maxPoints=" + maxPoints + '}';
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

    public String getTitle() {
        return type + number;
    }

    public String getType() {
        return type;
    }

    public int getNumber() {
        return number;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getDeadlineDate() {
        return deadlineDate;
    }

    public int getMinPoints() {
        return minPoints;
    }

    public int getMaxPoints() {
        return maxPoints;
    }
}
