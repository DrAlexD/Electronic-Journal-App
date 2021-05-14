package com.example.electronicdiary.student;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;
import com.example.electronicdiary.data_classes.Event;
import com.example.electronicdiary.data_classes.Lesson;
import com.example.electronicdiary.data_classes.Module;
import com.example.electronicdiary.data_classes.StudentEvent;
import com.example.electronicdiary.data_classes.StudentLesson;
import com.example.electronicdiary.data_classes.StudentPerformanceInModule;
import com.example.electronicdiary.data_classes.StudentPerformanceInSubject;

import java.util.List;
import java.util.Map;

public class StudentPerformanceViewModel extends ViewModel {
    private final MutableLiveData<Integer> moduleExpand = new MutableLiveData<>();
    private final MutableLiveData<Integer> openPage = new MutableLiveData<>();

    private final MutableLiveData<Map<String, Module>> modules = new MutableLiveData<>();
    private final MutableLiveData<Map<String, List<Event>>> events = new MutableLiveData<>();

    private final MutableLiveData<StudentPerformanceInSubject> studentPerformanceInSubject = new MutableLiveData<>();
    private final MutableLiveData<Map<String, StudentPerformanceInModule>> studentPerformanceInModules = new MutableLiveData<>();
    private final MutableLiveData<Map<String, List<StudentEvent>>> studentEvents = new MutableLiveData<>();
    private final MutableLiveData<Map<String, List<Lesson>>> lectures = new MutableLiveData<>();
    private final MutableLiveData<Map<String, List<Lesson>>> seminars = new MutableLiveData<>();
    private final MutableLiveData<Map<String, List<StudentLesson>>> studentLessons = new MutableLiveData<>();


    public LiveData<Integer> getModuleExpand() {
        return moduleExpand;
    }

    public void setModuleExpand(int moduleExpand) {
        this.moduleExpand.setValue(moduleExpand);
    }

    public LiveData<Integer> getOpenPage() {
        return openPage;
    }

    public void setOpenPage(int openPage) {
        this.openPage.setValue(openPage);
    }

    public LiveData<Map<String, Module>> getModules() {
        return modules;
    }

    public LiveData<StudentPerformanceInSubject> getStudentPerformanceInSubject() {
        return studentPerformanceInSubject;
    }

    public LiveData<Map<String, StudentPerformanceInModule>> getStudentPerformanceInModules() {
        return studentPerformanceInModules;
    }

    public LiveData<Map<String, List<Event>>> getEvents() {
        return events;
    }

    public LiveData<Map<String, List<StudentEvent>>> getStudentEvents() {
        return studentEvents;
    }

    public LiveData<Map<String, List<Lesson>>> getLectures() {
        return lectures;
    }

    public LiveData<Map<String, List<Lesson>>> getSeminars() {
        return seminars;
    }

    public LiveData<Map<String, List<StudentLesson>>> getStudentLessons() {
        return studentLessons;
    }

    public void downloadStudentPerformanceInSubject(long studentPerformanceInSubjectId) {
        Repository.getInstance().getStudentPerformanceInSubjectById(studentPerformanceInSubjectId, studentPerformanceInSubject);
        Repository.getInstance().getStudentPerformanceInModules(studentPerformanceInSubjectId, studentPerformanceInModules);
        Repository.getInstance().getStudentEvents(studentPerformanceInSubjectId, studentEvents);
        Repository.getInstance().getStudentLessons(studentPerformanceInSubjectId, studentLessons);
    }

    public void downloadEventsAndLessons(long subjectInfoId) {
        Repository.getInstance().getModules(subjectInfoId, modules);
        Repository.getInstance().getEvents(subjectInfoId, events);
        Repository.getInstance().getLectures(subjectInfoId, lectures);
        Repository.getInstance().getSeminars(subjectInfoId, seminars);
    }
}