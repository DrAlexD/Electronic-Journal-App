package com.example.electronicdiary.data_classes;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class StudentEvent {
    private final int attemptNumber;
    private final long eventId;
    private final int moduleNumber;
    private final long studentId;
    private final long groupId;
    private final long subjectId;
    private final long lecturerId;
    private final long seminarianId;
    private final long semesterId;

    private final boolean isAttended;
    private final int variantNumber;

    private Date finishDate = null;
    private int earnedPoints = -1;
    private int bonusPoints = -1;
    private boolean isHaveCredit = false;

    public StudentEvent(int attemptNumber, long eventId, int moduleNumber, long studentId, long groupId, long subjectId,
                        long lecturerId, long seminarianId, long semesterId, boolean isAttended, int variantNumber,
                        Date finishDate, int earnedPoints, int bonusPoints, boolean isHaveCredit) {
        this.attemptNumber = attemptNumber;
        this.eventId = eventId;
        this.moduleNumber = moduleNumber;
        this.studentId = studentId;
        this.groupId = groupId;
        this.subjectId = subjectId;
        this.lecturerId = lecturerId;
        this.seminarianId = seminarianId;
        this.semesterId = semesterId;
        this.isAttended = isAttended;
        this.variantNumber = variantNumber;
        this.finishDate = finishDate;
        this.earnedPoints = earnedPoints;
        this.bonusPoints = bonusPoints;
        this.isHaveCredit = isHaveCredit;
    }

    public StudentEvent(int attemptNumber, long eventId, int moduleNumber, long studentId, long groupId, long subjectId,
                        long lecturerId, long seminarianId, long semesterId, boolean isAttended, int variantNumber) {
        this.attemptNumber = attemptNumber;
        this.eventId = eventId;
        this.moduleNumber = moduleNumber;
        this.studentId = studentId;
        this.groupId = groupId;
        this.subjectId = subjectId;
        this.lecturerId = lecturerId;
        this.seminarianId = seminarianId;
        this.semesterId = semesterId;
        this.isAttended = isAttended;
        this.variantNumber = variantNumber;
    }

    @NotNull
    @Override
    public String toString() {
        return "StudentEvent{" + "attemptNumber=" + attemptNumber + ", isAttended=" + isAttended +
                ", variantNumber=" + variantNumber + ", finishDate=" + finishDate +
                ", earnedPoints=" + earnedPoints + ", bonusPoints=" + bonusPoints +
                ", isHaveCredit=" + isHaveCredit + '}';
    }

    public int getAttemptNumber() {
        return attemptNumber;
    }

    public long getEventId() {
        return eventId;
    }

    public int getModuleNumber() {
        return moduleNumber;
    }

    public long getStudentId() {
        return studentId;
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

    public boolean isAttended() {
        return isAttended;
    }

    public int getVariantNumber() {
        return variantNumber;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public int getEarnedPoints() {
        return earnedPoints;
    }

    public void setEarnedPoints(int earnedPoints) {
        this.earnedPoints = earnedPoints;
    }

    public int getBonusPoints() {
        return bonusPoints;
    }

    public void setBonusPoints(int bonusPoints) {
        this.bonusPoints = bonusPoints;
    }

    public boolean isHaveCredit() {
        return isHaveCredit;
    }

    public void setHaveCredit(boolean haveCredit) {
        isHaveCredit = haveCredit;
    }
}
