package com.example.electronicdiary.data_classes;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class Event {
    private final long id;
    private final int moduleNumber;
    private final long groupId;
    private final long subjectId;
    private final long lecturerId;
    private final long seminarianId;
    private final long semesterId;

    private final String type;
    private final int number;
    private final Date startDate;
    private final Date deadlineDate;
    private final int minPoints;
    private final int maxPoints;

    public Event(long id, int moduleNumber, long groupId, long subjectId, long lecturerId, long seminarianId,
                 long semesterId, String type, int number, Date startDate, Date deadlineDate, int minPoints, int maxPoints) {
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
