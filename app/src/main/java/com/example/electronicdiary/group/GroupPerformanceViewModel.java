package com.example.electronicdiary.group;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;
import com.example.electronicdiary.data_classes.Event;
import com.example.electronicdiary.data_classes.Lesson;
import com.example.electronicdiary.data_classes.ModuleInfo;
import com.example.electronicdiary.data_classes.Student;
import com.example.electronicdiary.data_classes.StudentEvent;
import com.example.electronicdiary.data_classes.StudentLesson;
import com.example.electronicdiary.data_classes.StudentPerformanceInModule;
import com.example.electronicdiary.data_classes.StudentPerformanceInSubject;
import com.example.electronicdiary.data_classes.SubjectInfo;

import java.util.HashMap;
import java.util.List;

public class GroupPerformanceViewModel extends ViewModel {
    private final MutableLiveData<SubjectInfo> subjectInfo = new MutableLiveData<>();
    private final MutableLiveData<HashMap<Integer, ModuleInfo>> moduleInfo = new MutableLiveData<>();
    private final MutableLiveData<HashMap<Integer, List<Event>>> events = new MutableLiveData<>();
    private final MutableLiveData<HashMap<Integer, List<Lesson>>> lessons = new MutableLiveData<>();
    private final MutableLiveData<List<Student>> studentsInGroup = new MutableLiveData<>();
    private final MutableLiveData<List<StudentPerformanceInSubject>> studentsPerformancesInSubject = new MutableLiveData<>();
    private final MutableLiveData<HashMap<Integer, List<StudentPerformanceInModule>>> studentsPerformancesInModules = new MutableLiveData<>();
    private final MutableLiveData<HashMap<Integer, List<List<StudentEvent>>>> studentsEvents = new MutableLiveData<>();
    private final MutableLiveData<HashMap<Integer, List<List<StudentLesson>>>> studentsLessons = new MutableLiveData<>();

    public LiveData<SubjectInfo> getSubjectInfo() {
        return subjectInfo;
    }

    public LiveData<HashMap<Integer, ModuleInfo>> getModuleInfo() {
        return moduleInfo;
    }

    public LiveData<HashMap<Integer, List<Event>>> getEvents() {
        return events;
    }

    public LiveData<HashMap<Integer, List<Lesson>>> getLessons() {
        return lessons;
    }

    public LiveData<List<Student>> getStudentsInGroup() {
        return studentsInGroup;
    }

    public LiveData<List<StudentPerformanceInSubject>> getStudentsPerformancesInSubject() {
        return studentsPerformancesInSubject;
    }

    public LiveData<HashMap<Integer, List<StudentPerformanceInModule>>> getStudentsPerformancesInModules() {
        return studentsPerformancesInModules;
    }

    public LiveData<HashMap<Integer, List<List<StudentEvent>>>> getStudentsEvents() {
        return studentsEvents;
    }

    public LiveData<HashMap<Integer, List<List<StudentLesson>>>> getStudentsLessons() {
        return studentsLessons;
    }

    public void downloadStudentsEventsAndLessons(long groupId, long subjectId, long lecturerId, long seminarianId, long semesterId) {
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