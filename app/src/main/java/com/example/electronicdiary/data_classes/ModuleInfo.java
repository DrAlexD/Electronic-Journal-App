package com.example.electronicdiary.data_classes;

public class ModuleInfo {
    private final int moduleNumber;
    private final int groupId;
    private final int subjectId;
    private final int lecturerId;
    private final int seminarianId;
    private final int semesterId;

    private final int minPoints;
    private final int maxPoints;

    public ModuleInfo(int moduleNumber, int groupId, int subjectId, int lecturerId, int seminarianId, int semesterId, int minPoints, int maxPoints) {
        this.moduleNumber = moduleNumber;
        this.groupId = groupId;
        this.subjectId = subjectId;
        this.lecturerId = lecturerId;
        this.seminarianId = seminarianId;
        this.semesterId = semesterId;
        this.minPoints = minPoints;
        this.maxPoints = maxPoints;
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

    public int getMaxPoints() {
        return maxPoints;
    }

    public int getMinPoints() {
        return minPoints;
    }
}

