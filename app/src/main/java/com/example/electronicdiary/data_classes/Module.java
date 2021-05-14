package com.example.electronicdiary.data_classes;

public class Module {
    private final int moduleNumber;
    private final SubjectInfo subjectInfo;
    private final int minPoints;
    private final int maxPoints;
    private long id;

    public Module(int moduleNumber, SubjectInfo subjectInfo, int minPoints, int maxPoints) {
        this.moduleNumber = moduleNumber;
        this.subjectInfo = subjectInfo;
        this.minPoints = minPoints;
        this.maxPoints = maxPoints;
    }

    public Module(int moduleNumber, long id, SubjectInfo subjectInfo, int minPoints, int maxPoints) {
        this.moduleNumber = moduleNumber;
        this.id = id;
        this.subjectInfo = subjectInfo;
        this.minPoints = minPoints;
        this.maxPoints = maxPoints;
    }

    public int getModuleNumber() {
        return moduleNumber;
    }

    public long getId() {
        return id;
    }

    public SubjectInfo getSubjectInfo() {
        return subjectInfo;
    }

    public int getMaxPoints() {
        return maxPoints;
    }

    public int getMinPoints() {
        return minPoints;
    }
}

