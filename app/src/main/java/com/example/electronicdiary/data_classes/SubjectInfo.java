package com.example.electronicdiary.data_classes;

public class SubjectInfo {
    private final Group group;
    private final long subjectId;
    private final long lecturerId;
    private final long seminarianId;
    private final long semesterId;

    private final boolean isExam;
    private final boolean isDifferentiatedCredit;

    private boolean isQueueAllowed = false;

    public SubjectInfo(Group group, long subjectId, long lecturerId, long seminarianId, long semesterId, boolean isExam, boolean isDifferentiatedCredit) {
        this.group = group;
        this.subjectId = subjectId;
        this.lecturerId = lecturerId;
        this.seminarianId = seminarianId;
        this.semesterId = semesterId;
        this.isExam = isExam;
        this.isDifferentiatedCredit = isDifferentiatedCredit;
    }

    public SubjectInfo(Group group, long subjectId, long lecturerId, long seminarianId, long semesterId, boolean isExam, boolean isDifferentiatedCredit, boolean isQueueAllowed) {
        this.group = group;
        this.subjectId = subjectId;
        this.lecturerId = lecturerId;
        this.seminarianId = seminarianId;
        this.semesterId = semesterId;
        this.isExam = isExam;
        this.isDifferentiatedCredit = isDifferentiatedCredit;
        this.isQueueAllowed = isQueueAllowed;
    }

    public Group getGroup() {
        return group;
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

    public boolean isExam() {
        return isExam;
    }

    public boolean isDifferentiatedCredit() {
        return isDifferentiatedCredit;
    }

    public boolean isQueueAllowed() {
        return isQueueAllowed;
    }

    public void setQueueAllowed(boolean queueAllowed) {
        isQueueAllowed = queueAllowed;
    }
}
