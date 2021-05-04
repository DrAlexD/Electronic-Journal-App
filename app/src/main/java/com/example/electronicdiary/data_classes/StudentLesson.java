package com.example.electronicdiary.data_classes;

import org.jetbrains.annotations.NotNull;

public class StudentLesson {
    private final long lessonId;
    private final int moduleNumber;
    private final long studentId;
    private final long groupId;
    private final long subjectId;
    private final long lecturerId;
    private final long seminarianId;
    private final long semesterId;

    private final boolean isAttended;

    private int bonusPoints = -1;

    public StudentLesson(long lessonId, int moduleNumber, long studentId, long groupId, long subjectId, long lecturerId,
                         long seminarianId, long semesterId, boolean isAttended, int bonusPoints) {
        this.lessonId = lessonId;
        this.moduleNumber = moduleNumber;
        this.studentId = studentId;
        this.groupId = groupId;
        this.subjectId = subjectId;
        this.lecturerId = lecturerId;
        this.seminarianId = seminarianId;
        this.semesterId = semesterId;
        this.isAttended = isAttended;
        this.bonusPoints = bonusPoints;
    }

    public StudentLesson(long lessonId, int moduleNumber, long studentId, long groupId, long subjectId, long lecturerId,
                         long seminarianId, long semesterId, boolean isAttended) {
        this.lessonId = lessonId;
        this.moduleNumber = moduleNumber;
        this.studentId = studentId;
        this.groupId = groupId;
        this.subjectId = subjectId;
        this.lecturerId = lecturerId;
        this.seminarianId = seminarianId;
        this.semesterId = semesterId;
        this.isAttended = isAttended;
    }

    @NotNull
    @Override
    public String toString() {
        return "StudentLesson{" + "lessonId=" + lessonId + ", isAttended=" + isAttended + ", bonusPoints=" + bonusPoints + '}';
    }

    public long getLessonId() {
        return lessonId;
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

    public boolean isAttended() {
        return isAttended;
    }

    public int getBonusPoints() {
        return bonusPoints;
    }

    public void setBonusPoints(int bonusPoints) {
        this.bonusPoints = bonusPoints;
    }
}
