package com.example.electronicdiary.student;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;

public class StudentPerformanceViewModel extends ViewModel {
    private final MutableLiveData<String> group = new MutableLiveData<>();
    private final MutableLiveData<String> subject = new MutableLiveData<>();
    private final MutableLiveData<String> studentName = new MutableLiveData<>();

    private final MutableLiveData<ArrayList<Event>> events = new MutableLiveData<>();
    private final MutableLiveData<HashMap<String, ArrayList<Visit>>> visitsByModules = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<String>> modules = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<ArrayList<Visit>>> visits = new MutableLiveData<>();

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

    public LiveData<String> getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName.setValue(studentName);
    }

    public MutableLiveData<ArrayList<Event>> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events.setValue(events);
    }

    public MutableLiveData<HashMap<String, ArrayList<Visit>>> getVisitsByModules() {
        return visitsByModules;
    }

    public void setVisitsByModules(HashMap<String, ArrayList<Visit>> visitsByModules) {
        this.visitsByModules.setValue(visitsByModules);
    }

    public MutableLiveData<ArrayList<String>> getModules() {
        return modules;
    }

    public void setModules(ArrayList<String> modules) {
        this.modules.setValue(modules);
    }

    public MutableLiveData<ArrayList<ArrayList<Visit>>> getVisits() {
        return visits;
    }

    public void setVisits(ArrayList<ArrayList<Visit>> visits) {
        this.visits.setValue(visits);
    }
}