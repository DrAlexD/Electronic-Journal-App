package com.example.electronic_journal.data_classes;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class StudentEvent {
    private final StudentPerformanceInModule studentPerformanceInModule;
    private final int attemptNumber;
    private final Event event;
    private long id;

    private final boolean isAttended;
    private final int variantNumber;

    private Date finishDate = null;
    private Integer earnedPoints = null;
    private Integer bonusPoints = null;
    private Boolean isHasCredit = null;

    public StudentEvent(int attemptNumber, StudentPerformanceInModule studentPerformanceInModule,
                        Event event, boolean isAttended, int variantNumber) {
        this.attemptNumber = attemptNumber;
        this.studentPerformanceInModule = studentPerformanceInModule;
        this.event = event;
        this.isAttended = isAttended;
        this.variantNumber = variantNumber;
    }

    public StudentEvent(long id, int attemptNumber, StudentPerformanceInModule studentPerformanceInModule,
                        Event event, boolean isAttended, int variantNumber) {
        this.id = id;
        this.attemptNumber = attemptNumber;
        this.studentPerformanceInModule = studentPerformanceInModule;
        this.event = event;
        this.isAttended = isAttended;
        this.variantNumber = variantNumber;
    }

    public StudentEvent(int attemptNumber, StudentPerformanceInModule studentPerformanceInModule,
                        Event event, boolean isAttended, int variantNumber, Date finishDate, Integer earnedPoints,
                        Integer bonusPoints, Boolean isHasCredit) {
        this.attemptNumber = attemptNumber;
        this.studentPerformanceInModule = studentPerformanceInModule;
        this.event = event;
        this.isAttended = isAttended;
        this.variantNumber = variantNumber;
        this.finishDate = finishDate;
        this.earnedPoints = earnedPoints;
        this.bonusPoints = bonusPoints;
        this.isHasCredit = isHasCredit;
    }

    public StudentEvent(long id, int attemptNumber, StudentPerformanceInModule studentPerformanceInModule,
                        Event event, boolean isAttended, int variantNumber, Date finishDate, Integer earnedPoints,
                        Integer bonusPoints, Boolean isHasCredit) {
        this.id = id;
        this.attemptNumber = attemptNumber;
        this.studentPerformanceInModule = studentPerformanceInModule;
        this.event = event;
        this.isAttended = isAttended;
        this.variantNumber = variantNumber;
        this.finishDate = finishDate;
        this.earnedPoints = earnedPoints;
        this.bonusPoints = bonusPoints;
        this.isHasCredit = isHasCredit;
    }

    @NotNull
    @Override
    public String toString() {
        return "StudentEvent{" + "attemptNumber=" + attemptNumber + ", isAttended=" + isAttended +
                ", variantNumber=" + variantNumber + ", finishDate=" + finishDate +
                ", earnedPoints=" + earnedPoints + ", bonusPoints=" + bonusPoints +
                ", isHaveCredit=" + isHasCredit + '}';
    }

    public int getAttemptNumber() {
        return attemptNumber;
    }

    public long getId() {
        return id;
    }

    public Event getEvent() {
        return event;
    }

    public StudentPerformanceInModule getStudentPerformanceInModule() {
        return studentPerformanceInModule;
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

    public Integer getEarnedPoints() {
        return earnedPoints;
    }

    public void setEarnedPoints(Integer earnedPoints) {
        this.earnedPoints = earnedPoints;
    }

    public Integer getBonusPoints() {
        return bonusPoints;
    }

    public void setBonusPoints(Integer bonusPoints) {
        this.bonusPoints = bonusPoints;
    }

    public Boolean isHaveCredit() {
        return isHasCredit;
    }

    public void setHaveCredit(Boolean haveCredit) {
        isHasCredit = haveCredit;
    }
}
