package com.example.electronicdiary.group;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;
import com.example.electronicdiary.data_classes.Event;
import com.example.electronicdiary.data_classes.Lesson;
import com.example.electronicdiary.data_classes.Module;
import com.example.electronicdiary.data_classes.Student;
import com.example.electronicdiary.data_classes.StudentEvent;
import com.example.electronicdiary.data_classes.StudentLesson;
import com.example.electronicdiary.data_classes.StudentPerformanceInModule;
import com.example.electronicdiary.data_classes.StudentPerformanceInSubject;
import com.example.electronicdiary.data_classes.SubjectInfo;

import java.util.List;
import java.util.Map;

public class GroupPerformanceViewModel extends ViewModel {
    private final MutableLiveData<SubjectInfo> subjectInfo = new MutableLiveData<>();
    private final MutableLiveData<Map<String, Module>> modules = new MutableLiveData<>();
    private final MutableLiveData<Map<String, List<Event>>> events = new MutableLiveData<>();
    private final MutableLiveData<Map<String, List<Lesson>>> lessons = new MutableLiveData<>();
    private final MutableLiveData<List<Student>> studentsInGroup = new MutableLiveData<>();
    private final MutableLiveData<List<StudentPerformanceInSubject>> studentsPerformancesInSubject = new MutableLiveData<>();
    private final MutableLiveData<Map<String, List<StudentPerformanceInModule>>> studentsPerformancesInModules = new MutableLiveData<>();
    private final MutableLiveData<Map<String, Map<String, List<StudentEvent>>>> studentsEvents = new MutableLiveData<>();
    private final MutableLiveData<Map<String, Map<String, List<StudentLesson>>>> studentsLessons = new MutableLiveData<>();

    public LiveData<SubjectInfo> getSubjectInfo() {
        return subjectInfo;
    }

    public LiveData<Map<String, Module>> getModules() {
        return modules;
    }

    public LiveData<Map<String, List<Event>>> getEvents() {
        return events;
    }

    public LiveData<Map<String, List<Lesson>>> getLessons() {
        return lessons;
    }

    public LiveData<List<Student>> getStudentsInGroup() {
        return studentsInGroup;
    }

    public LiveData<List<StudentPerformanceInSubject>> getStudentsPerformancesInSubject() {
        return studentsPerformancesInSubject;
    }

    public LiveData<Map<String, List<StudentPerformanceInModule>>> getStudentsPerformancesInModules() {
        return studentsPerformancesInModules;
    }

    public LiveData<Map<String, Map<String, List<StudentEvent>>>> getStudentsEvents() {
        return studentsEvents;
    }

    public LiveData<Map<String, Map<String, List<StudentLesson>>>> getStudentsLessons() {
        return studentsLessons;
    }

    public void downloadEntities(long subjectInfoId) {
        Repository.getInstance().getSubjectInfoById(subjectInfoId, subjectInfo);
        Repository.getInstance().getModules(subjectInfoId, modules);
        Repository.getInstance().getStudentsPerformancesInModules(subjectInfoId, studentsPerformancesInModules);
    }

    public void downloadLessons(long subjectInfoId) {
        Repository.getInstance().getLessons(subjectInfoId, lessons);
        Repository.getInstance().getStudentsLessons(subjectInfoId, studentsLessons);
    }

    public void downloadEvents(long subjectInfoId) {
        Repository.getInstance().getStudentsPerformancesInSubject(subjectInfoId, studentsPerformancesInSubject);
        Repository.getInstance().getEvents(subjectInfoId, events);
        Repository.getInstance().getStudentsEvents(subjectInfoId, studentsEvents);
    }

    public void downloadStudentsInGroup(long groupId) {
        Repository.getInstance().getStudentsInGroup(groupId, studentsInGroup);
    }
}