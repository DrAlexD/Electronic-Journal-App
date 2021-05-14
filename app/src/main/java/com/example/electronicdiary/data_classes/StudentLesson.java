package com.example.electronicdiary.data_classes;

import org.jetbrains.annotations.NotNull;

public class StudentLesson {
    private final StudentPerformanceInModule studentPerformanceInModule;
    private final Lesson lesson;
    private long id;

    private final boolean isAttended;
    private Integer bonusPoints = null;

    public StudentLesson(StudentPerformanceInModule studentPerformanceInModule, Lesson lesson,
                         boolean isAttended) {
        this.studentPerformanceInModule = studentPerformanceInModule;
        this.lesson = lesson;
        this.isAttended = isAttended;
    }

    public StudentLesson(long id, StudentPerformanceInModule studentPerformanceInModule, Lesson lesson,
                         boolean isAttended) {
        this.id = id;
        this.studentPerformanceInModule = studentPerformanceInModule;
        this.lesson = lesson;
        this.isAttended = isAttended;
    }

    public StudentLesson(StudentPerformanceInModule studentPerformanceInModule, Lesson lesson,
                         boolean isAttended, Integer bonusPoints) {
        this.studentPerformanceInModule = studentPerformanceInModule;
        this.lesson = lesson;
        this.isAttended = isAttended;
        this.bonusPoints = bonusPoints;
    }

    public StudentLesson(long id, StudentPerformanceInModule studentPerformanceInModule, Lesson lesson,
                         boolean isAttended, Integer bonusPoints) {
        this.id = id;
        this.studentPerformanceInModule = studentPerformanceInModule;
        this.lesson = lesson;
        this.isAttended = isAttended;
        this.bonusPoints = bonusPoints;
    }

    @NotNull
    @Override
    public String toString() {
        return "StudentLesson{" + "id=" + id + ", isAttended=" + isAttended + ", bonusPoints=" + bonusPoints + '}';
    }

    public long getId() {
        return id;
    }

    public StudentPerformanceInModule getStudentPerformanceInModule() {
        return studentPerformanceInModule;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public boolean isAttended() {
        return isAttended;
    }

    public Integer getBonusPoints() {
        return bonusPoints;
    }

    public void setBonusPoints(Integer bonusPoints) {
        this.bonusPoints = bonusPoints;
    }
}
