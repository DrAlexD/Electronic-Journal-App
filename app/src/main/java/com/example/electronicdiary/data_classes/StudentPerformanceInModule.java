package com.example.electronicdiary.data_classes;

public class StudentPerformanceInModule {
    private final int moduleNumber;
    private final long studentId;
    private final long groupId;
    private final long subjectId;
    private final long lecturerId;
    private final long seminarianId;
    private final long semesterId;

    private int earnedPoints = -1;
    private boolean isHaveCredit = false;

    public StudentPerformanceInModule(int moduleNumber, long studentId, long groupId, long subjectId, long lecturerId, long seminarianId, long semesterId) {
        this.moduleNumber = moduleNumber;
        this.studentId = studentId;
        this.groupId = groupId;
        this.subjectId = subjectId;
        this.lecturerId = lecturerId;
        this.seminarianId = seminarianId;
        this.semesterId = semesterId;
    }

    public StudentPerformanceInModule(int moduleNumber, long studentId, long groupId, long subjectId, long lecturerId, long seminarianId, long semesterId, int earnedPoints, boolean isHaveCredit) {
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
