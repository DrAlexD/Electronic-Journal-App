package com.example.electronicdiary.data_classes;

public class ModuleInfo {
    private final int moduleNumber;
    private final long groupId;
    private final long subjectId;
    private final long lecturerId;
    private final long seminarianId;
    private final long semesterId;

    private final int minPoints;
    private final int maxPoints;

    public ModuleInfo(int moduleNumber, long groupId, long subjectId, long lecturerId, long seminarianId, long semesterId, int minPoints, int maxPoints) {
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

    public int getMaxPoints() {
        return maxPoints;
    }

    public int getMinPoints() {
        return minPoints;
    }
}

