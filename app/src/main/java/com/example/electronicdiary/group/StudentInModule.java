package com.example.electronicdiary.group;

import com.example.electronicdiary.Student;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.Map;

class StudentInModule {
    private final Student student;
    private final Map<String, Integer> eventsWithPoints;
    private final Map<Date, Integer> lessonsWithPoints;

    StudentInModule(Student student, Map<String, Integer> eventsWithPoints, Map<Date, Integer> lessonsWithPoints) {
        this.student = student;
        this.eventsWithPoints = eventsWithPoints;
        this.lessonsWithPoints = lessonsWithPoints;
    }

    @NotNull
    @Override
    public String toString() {
        return "StudentInModule{" +
                "student=" + student +
                ", eventsWithPoints=" + eventsWithPoints +
                ", lessonsWithPoints=" + lessonsWithPoints +
                '}';
    }

    public Student getStudent() {
        return student;
    }

    public Map<String, Integer> getEventsWithPoints() {
        return eventsWithPoints;
    }

    public Map<Date, Integer> getLessonsWithPoints() {
        return lessonsWithPoints;
    }
}
