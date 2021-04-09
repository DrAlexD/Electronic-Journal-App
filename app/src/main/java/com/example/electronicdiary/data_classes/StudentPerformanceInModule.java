package com.example.electronicdiary.data_classes;

public class StudentPerformanceInModule {
    private final int moduleNumber;
    private final int studentId;
    private final int groupId;
    private final int subjectId;
    private final int lecturerId;
    private final int seminarianId;
    private final int semesterId;

    private int earnedPoints = -1;
    private boolean isHaveCredit = false;

    public StudentPerformanceInModule(int moduleNumber, int studentId, int groupId, int subjectId, int lecturerId, int seminarianId, int semesterId) {
        this.moduleNumber = moduleNumber;
        this.studentId = studentId;
        this.groupId = groupId;
        this.subjectId = subjectId;
        this.lecturerId = lecturerId;
        this.seminarianId = seminarianId;
        this.semesterId = semesterId;
    }

    public StudentPerformanceInModule(int moduleNumber, int studentId, int groupId, int subjectId, int lecturerId, int seminarianId, int semesterId, int earnedPoints, boolean isHaveCredit) {
        this.moduleNumber = moduleNumber;
        this.studentId = studentId;
        this.groupId = groupId;
        this.subjectId = subjectId;
        this.lecturerId = lecturerId;
        this.seminarianId = seminarianId;
        this.semesterId = semesterId;
        this.earnedPoints = earnedPoints;
        this.isHaveCredit = isHaveCredit;
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

    public int getEarnedPoints() {
        return earnedPoints;
    }

    public void setEarnedPoints(int earnedPoints) {
        this.earnedPoints = earnedPoints;
    }

    public boolean isHaveCredit() {
        return isHaveCredit;
    }

    public void setHaveCredit(boolean haveCredit) {
        isHaveCredit = haveCredit;
    }
}
