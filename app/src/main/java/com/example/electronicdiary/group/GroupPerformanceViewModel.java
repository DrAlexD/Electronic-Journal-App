package com.example.electronicdiary.group;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.student.Student;

import java.util.ArrayList;
import java.util.Date;

public class GroupPerformanceViewModel extends ViewModel {
    private final MutableLiveData<String> group = new MutableLiveData<>();
    private final MutableLiveData<String> subject = new MutableLiveData<>();

    private final MutableLiveData<ArrayList<StudentInModule>> studentsInModule = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Student>> students = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<String>> events = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Date>> visits = new MutableLiveData<>();

    public LiveData<String> getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject.setValue(subject);
    }

    public LiveData<String> getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group.setValue(group);
    }

    public LiveData<ArrayList<StudentInModule>> getStudentsInModule() {
        return studentsInModule;
    }

    public void setStudentsInModule(ArrayList<StudentInModule> studentsInModule) {
        this.studentsInModule.setValue(studentsInModule);
    }

    public LiveData<ArrayList<Student>> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<Student> students) {
        this.students.setValue(students);
    }

    public LiveData<ArrayList<String>> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<String> events) {
        this.events.setValue(events);
    }

    public LiveData<ArrayList<Date>> getVisits() {
        return visits;
    }

    public void setVisits(ArrayList<Date> visits) {
        this.visits.setValue(visits);
    }
}