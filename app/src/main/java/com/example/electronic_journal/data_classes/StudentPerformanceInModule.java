package com.example.electronic_journal.data_classes;

public class StudentPerformanceInModule {
    private final Module module;
    private final StudentPerformanceInSubject studentPerformanceInSubject;
    private long id;
    private Integer earnedPoints = null;
    private Boolean isHasCredit = null;

    public StudentPerformanceInModule(Module module, StudentPerformanceInSubject studentPerformanceInSubject) {
        this.module = module;
        this.studentPerformanceInSubject = studentPerformanceInSubject;
    }

    public StudentPerformanceInModule(long id, Module module, StudentPerformanceInSubject studentPerformanceInSubject) {
        this.id = id;
        this.module = module;
        this.studentPerformanceInSubject = studentPerformanceInSubject;
    }

    public StudentPerformanceInModule(Module module, StudentPerformanceInSubject studentPerformanceInSubject,
                                      Integer earnedPoints, Boolean isHasCredit) {
        this.module = module;
        this.studentPerformanceInSubject = studentPerformanceInSubject;
        this.earnedPoints = earnedPoints;
        this.isHasCredit = isHasCredit;
    }

    public StudentPerformanceInModule(long id, Module module, StudentPerformanceInSubject studentPerformanceInSubject,
                                      Integer earnedPoints, Boolean isHasCredit) {
        this.id = id;
        this.module = module;
        this.studentPerformanceInSubject = studentPerformanceInSubject;
        this.earnedPoints = earnedPoints;
        this.isHasCredit = isHasCredit;
    }

    public long getId() {
        return id;
    }

    public Module getModule() {
        return module;
    }

    public StudentPerformanceInSubject getStudentPerformanceInSubject() {
        return studentPerformanceInSubject;
    }

    public Integer getEarnedPoints() {
        return earnedPoints;
    }

    public void setEarnedPoints(Integer earnedPoints) {
        this.earnedPoints = earnedPoints;
    }

    public Boolean isHaveCredit() {
        return isHasCredit;
    }

    public void setHaveCredit(Boolean haveCredit) {
        isHasCredit = haveCredit;
    }
}
