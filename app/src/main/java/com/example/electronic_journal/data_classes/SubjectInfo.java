package com.example.electronic_journal.data_classes;

public class SubjectInfo {
    private final Subject subject;
    private final Group group;
    private final Professor seminarsProfessor;
    private final Semester semester;
    private long id;
    private final Long lecturerId;

    private final boolean isExam;
    private final boolean isDifferentiatedCredit;

    public SubjectInfo(Group group, Subject subject, Long lecturerId, Professor seminarsProfessor, Semester semester, boolean isExam, boolean isDifferentiatedCredit) {
        this.group = group;
        this.subject = subject;
        this.lecturerId = lecturerId;
        this.seminarsProfessor = seminarsProfessor;
        this.semester = semester;
        this.isExam = isExam;
        this.isDifferentiatedCredit = isDifferentiatedCredit;
    }

    public SubjectInfo(long id, Group group, Subject subject, Long lecturerId, Professor seminarsProfessor, Semester semester, boolean isExam, boolean isDifferentiatedCredit) {
        this.id = id;
        this.group = group;
        this.subject = subject;
        this.lecturerId = lecturerId;
        this.seminarsProfessor = seminarsProfessor;
        this.semester = semester;
        this.isExam = isExam;
        this.isDifferentiatedCredit = isDifferentiatedCredit;
    }

    public long getId() {
        return id;
    }

    public Group getGroup() {
        return group;
    }

    public Subject getSubject() {
        return subject;
    }

    public Long getLecturerId() {
        return lecturerId;
    }

    public Professor getSeminarsProfessor() {
        return seminarsProfessor;
    }

    public Semester getSemester() {
        return semester;
    }

    public boolean isExam() {
        return isExam;
    }

    public boolean isDifferentiatedCredit() {
        return isDifferentiatedCredit;
    }
}
