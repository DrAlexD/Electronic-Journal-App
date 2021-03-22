package com.example.electronicdiary;

import org.jetbrains.annotations.NotNull;

public class StudentLesson {
    private final int lessonId;
    private final int moduleNumber;
    private final int studentId;
    private final int groupId;
    private final int subjectId;
    private final int lecturerId;
    private final int seminarianId;
    private final int semesterId;

    private final boolean isAttended;

    private int bonusPoints;

    public StudentLesson(int lessonId, int moduleNumber, int studentId, int groupId, int subjectId, int lecturerId,
                         int seminarianId, int semesterId, boolean isAttended, int bonusPoints) {
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

    public StudentLesson(int lessonId, int moduleNumber, int studentId, int groupId, int subjectId, int lecturerId,
                         int seminarianId, int semesterId, boolean isAttended) {
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

    public int getLessonId() {
        return lessonId;
    }

    public int getModuleNumber() {
        return moduleNumber;
    }

    public int getStudentId() {
        return studentId;
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
