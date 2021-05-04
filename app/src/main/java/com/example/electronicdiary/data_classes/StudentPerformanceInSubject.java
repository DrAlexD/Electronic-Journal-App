package com.example.electronicdiary.data_classes;

public class StudentPerformanceInSubject {
    private final long studentId;
    private final long groupId;
    private final long subjectId;
    private final long lecturerId;
    private final long seminarianId;
    private final long semesterId;

    private int positionInWaitingList = -1;
    private int earnedPoints = -1;
    private int bonusPoints = -1;
    private boolean isHaveCreditOrAdmission = false;
    private int earnedExamPoints = -1;
    private int mark = -1;

    public StudentPerformanceInSubject(long studentId, long groupId, long subjectId, long lecturerId, long seminarianId, long semesterId) {
        this.studentId = studentId;
        this.groupId = groupId;
        this.subjectId = subjectId;
        this.lecturerId = lecturerId;
        this.seminarianId = seminarianId;
        this.semesterId = semesterId;
    }

    public StudentPerformanceInSubject(long studentId, long groupId, long subjectId, long lecturerId,
                                       long seminarianId, long semesterId, int positionInWaitingList, int earnedPoints,
                                       int bonusPoints, boolean isHaveCreditOrAdmission, int earnedExamPoints, int mark) {
        this.studentId = studentId;
        this.groupId = groupId;
        this.subjectId = subjectId;
        this.lecturerId = lecturerId;
        this.seminarianId = seminarianId;
        this.semesterId = semesterId;
        this.positionInWaitingList = positionInWaitingList;
        this.earnedPoints = earnedPoints;
        this.bonusPoints = bonusPoints;
        this.isHaveCreditOrAdmission = isHaveCreditOrAdmission;
        this.earnedExamPoints = earnedExamPoints;
        this.mark = mark;
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

    public int getPositionInWaitingList() {
        return positionInWaitingList;
    }

    public void setPositionInWaitingList(int positionInWaitingList) {
        this.positionInWaitingList = positionInWaitingList;
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

    public boolean isHaveCreditOrAdmission() {
        return isHaveCreditOrAdmission;
    }

    public void setHaveCreditOrAdmission(boolean haveCreditOrAdmission) {
        isHaveCreditOrAdmission = haveCreditOrAdmission;
    }

    public int getEarnedExamPoints() {
        return earnedExamPoints;
    }

    public void setEarnedExamPoints(int earnedExamPoints) {
        this.earnedExamPoints = earnedExamPoints;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }
}
