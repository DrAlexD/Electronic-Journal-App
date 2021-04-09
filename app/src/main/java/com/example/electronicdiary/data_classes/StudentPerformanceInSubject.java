package com.example.electronicdiary.data_classes;

public class StudentPerformanceInSubject {
    private final int studentId;
    private final int groupId;
    private final int subjectId;
    private final int lecturerId;
    private final int seminarianId;
    private final int semesterId;

    private int positionInWaitingList = -1;
    private int earnedPoints = -1;
    private int bonusPoints = -1;
    private boolean isHaveCreditOrAdmission = false;
    private int earnedExamPoints = -1;
    private int mark = -1;

    public StudentPerformanceInSubject(int studentId, int groupId, int subjectId, int lecturerId, int seminarianId, int semesterId) {
        this.studentId = studentId;
        this.groupId = groupId;
        this.subjectId = subjectId;
        this.lecturerId = lecturerId;
        this.seminarianId = seminarianId;
        this.semesterId = semesterId;
    }

    public StudentPerformanceInSubject(int studentId, int groupId, int subjectId, int lecturerId,
                                       int seminarianId, int semesterId, int positionInWaitingList, int earnedPoints,
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
