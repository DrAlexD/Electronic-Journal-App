package com.example.electronicdiary;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class StudentEvent {
    private final int attemptNumber;
    private final int eventId;
    private final int moduleNumber;
    private final int studentId;
    private final int groupId;
    private final int subjectId;
    private final int lecturerId;
    private final int seminarianId;
    private final int semesterId;

    private final boolean isAttended;
    private final int variantNumber;

    private Date finishDate;
    private int earnedPoints;
    private int bonusPoints;
    private boolean isHaveCredit;

    public StudentEvent(int attemptNumber, int eventId, int moduleNumber, int studentId, int groupId, int subjectId,
                        int lecturerId, int seminarianId, int semesterId, boolean isAttended, int variantNumber,
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

    public StudentEvent(int attemptNumber, int eventId, int moduleNumber, int studentId, int groupId, int subjectId,
                        int lecturerId, int seminarianId, int semesterId, boolean isAttended, int variantNumber) {
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

    public int getEventId() {
        return eventId;
    }

    public int getModuleNumber() {
        return moduleNumber;
    }

    public int getStudentId() {
        return studentId;
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
