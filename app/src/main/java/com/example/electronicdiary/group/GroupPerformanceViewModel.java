package com.example.electronicdiary.group;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Event;
import com.example.electronicdiary.Lesson;
import com.example.electronicdiary.ModuleInfo;
import com.example.electronicdiary.Repository;
import com.example.electronicdiary.Student;
import com.example.electronicdiary.StudentEvent;
import com.example.electronicdiary.StudentLesson;
import com.example.electronicdiary.StudentPerformanceInModule;
import com.example.electronicdiary.StudentPerformanceInSubject;
import com.example.electronicdiary.SubjectInfo;

import java.util.ArrayList;
import java.util.HashMap;

public class GroupPerformanceViewModel extends ViewModel {
    private final MutableLiveData<SubjectInfo> subjectInfo = new MutableLiveData<>();
    private final MutableLiveData<HashMap<Integer, ModuleInfo>> moduleInfo = new MutableLiveData<>();
    private final MutableLiveData<HashMap<Integer, ArrayList<Event>>> events = new MutableLiveData<>();
    private final MutableLiveData<HashMap<Integer, ArrayList<Lesson>>> lessons = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Student>> studentsInGroup = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<StudentPerformanceInSubject>> studentsPerformancesInSubject = new MutableLiveData<>();
    private final MutableLiveData<HashMap<Integer, ArrayList<StudentPerformanceInModule>>> studentsPerformancesInModules = new MutableLiveData<>();
    private final MutableLiveData<HashMap<Integer, ArrayList<ArrayList<StudentEvent>>>> studentsEvents = new MutableLiveData<>();
    private final MutableLiveData<HashMap<Integer, ArrayList<ArrayList<StudentLesson>>>> studentsLessons = new MutableLiveData<>();

    public LiveData<SubjectInfo> getSubjectInfo() {
        return subjectInfo;
    }

    public LiveData<HashMap<Integer, ModuleInfo>> getModuleInfo() {
        return moduleInfo;
    }

    public LiveData<HashMap<Integer, ArrayList<Event>>> getEvents() {
        return events;
    }

    public LiveData<HashMap<Integer, ArrayList<Lesson>>> getLessons() {
        return lessons;
    }

    public LiveData<ArrayList<Student>> getStudentsInGroup() {
        return studentsInGroup;
    }

    public LiveData<ArrayList<StudentPerformanceInSubject>> getStudentsPerformancesInSubject() {
        return studentsPerformancesInSubject;
    }

    public LiveData<HashMap<Integer, ArrayList<StudentPerformanceInModule>>> getStudentsPerformancesInModules() {
        return studentsPerformancesInModules;
    }

    public LiveData<HashMap<Integer, ArrayList<ArrayList<StudentEvent>>>> getStudentsEvents() {
        return studentsEvents;
    }

    public LiveData<HashMap<Integer, ArrayList<ArrayList<StudentLesson>>>> getStudentsLessons() {
        return studentsLessons;
    }

    public void downloadStudentsEventsAndLessons(int groupId, int subjectId, int lecturerId, int seminarianId, int semesterId) {
        this.subjectInfo.setValue(Repository.getInstance().getSubjectInfo(groupId, subjectId, lecturerId, seminarianId, semesterId));
        this.moduleInfo.setValue(Repository.getInstance().getModuleInfo(groupId, subjectId, lecturerId, seminarianId, semesterId));
        this.events.setValue(Repository.getInstance().getEvents(groupId, subjectId, lecturerId, seminarianId, semesterId));
        this.lessons.setValue(Repository.getInstance().getLessons(groupId, subjectId, lecturerId, seminarianId, semesterId));
        this.studentsInGroup.setValue(Repository.getInstance().getStudentsInGroup(groupId));
        this.studentsPerformancesInSubject.setValue(Repository.getInstance().getStudentsPerformancesInSubject(groupId, subjectId, lecturerId, seminarianId, semesterId));
        this.studentsPerformancesInModules.setValue(Repository.getInstance().getStudentsPerformancesInModules(groupId, subjectId, lecturerId, seminarianId, semesterId));
        this.studentsEvents.setValue(Repository.getInstance().getStudentsEvents(groupId, subjectId, lecturerId, seminarianId, semesterId));
        this.studentsLessons.setValue(Repository.getInstance().getStudentsLessons(groupId, subjectId, lecturerId, seminarianId, semesterId));
    }
}