package com.example.electronic_journal.data_classes;

public class Module {
    private final int moduleNumber;
    private final SubjectInfo subjectInfo;
    private final int minPoints;
    private final int maxAvailablePoints;
    private long id;

    public Module(int moduleNumber, SubjectInfo subjectInfo, int minPoints, int maxAvailablePoints) {
        this.moduleNumber = moduleNumber;
        this.subjectInfo = subjectInfo;
        this.minPoints = minPoints;
        this.maxAvailablePoints = maxAvailablePoints;
    }

    public Module(int moduleNumber, long id, SubjectInfo subjectInfo, int minPoints, int maxAvailablePoints) {
        this.moduleNumber = moduleNumber;
        this.id = id;
        this.subjectInfo = subjectInfo;
        this.minPoints = minPoints;
        this.maxAvailablePoints = maxAvailablePoints;
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

    public int getMaxAvailablePoints() {
        return maxAvailablePoints;
    }

    public int getMinPoints() {
        return minPoints;
    }
}

