package com.example.electronicdiary.data_classes;

public class StudentPerformanceInSubject {
    private final SubjectInfo subjectInfo;
    private final Student student;
    private long id;
    private Integer earnedPoints = null;
    private Integer bonusPoints = null;
    private Boolean isHaveCreditOrAdmission = null;
    private Integer earnedExamPoints = null;
    private Integer mark = null;

    public StudentPerformanceInSubject(SubjectInfo subjectInfo, Student student) {
        this.subjectInfo = subjectInfo;
        this.student = student;
    }

    public StudentPerformanceInSubject(long id, SubjectInfo subjectInfo, Student student) {
        this.id = id;
        this.subjectInfo = subjectInfo;
        this.student = student;
    }

    public StudentPerformanceInSubject(SubjectInfo subjectInfo, Student student, Integer earnedPoints,
                                       Integer bonusPoints, Boolean isHaveCreditOrAdmission, Integer earnedExamPoints, Integer mark) {
        this.subjectInfo = subjectInfo;
        this.student = student;
        this.earnedPoints = earnedPoints;
        this.bonusPoints = bonusPoints;
        this.isHaveCreditOrAdmission = isHaveCreditOrAdmission;
        this.earnedExamPoints = earnedExamPoints;
        this.mark = mark;
    }

    public StudentPerformanceInSubject(long id, SubjectInfo subjectInfo, Student student, Integer earnedPoints,
                                       Integer bonusPoints, Boolean isHaveCreditOrAdmission, Integer earnedExamPoints, Integer mark) {
        this.id = id;
        this.subjectInfo = subjectInfo;
        this.student = student;
        this.earnedPoints = earnedPoints;
        this.bonusPoints = bonusPoints;
        this.isHaveCreditOrAdmission = isHaveCreditOrAdmission;
        this.earnedExamPoints = earnedExamPoints;
        this.mark = mark;
    }

    public long getId() {
        return id;
    }

    public SubjectInfo getSubjectInfo() {
        return subjectInfo;
    }

    public Student getStudent() {
        return student;
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

    public Boolean isHaveCreditOrAdmission() {
        return isHaveCreditOrAdmission;
    }

    public void setHaveCreditOrAdmission(Boolean haveCreditOrAdmission) {
        isHaveCreditOrAdmission = haveCreditOrAdmission;
    }

    public Integer getEarnedExamPoints() {
        return earnedExamPoints;
    }

    public void setEarnedExamPoints(Integer earnedExamPoints) {
        this.earnedExamPoints = earnedExamPoints;
    }

    public Integer getMark() {
        return mark;
    }

    public void setMark(Integer mark) {
        this.mark = mark;
    }
}
