package com.example.electronicdiary.group;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;
import com.example.electronicdiary.Student;
import com.example.electronicdiary.StudentEvent;
import com.example.electronicdiary.StudentLesson;

import java.util.ArrayList;
import java.util.HashMap;

public class GroupPerformanceViewModel extends ViewModel {
    private final MutableLiveData<String> group = new MutableLiveData<>();
    private final MutableLiveData<String> subject = new MutableLiveData<>();

    private final MutableLiveData<ArrayList<Student>> studentsInGroup = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<ArrayList<StudentEvent>>> studentsEvents = new MutableLiveData<>();
    private final MutableLiveData<HashMap<String, ArrayList<ArrayList<StudentLesson>>>> studentsLessonsByModules = new MutableLiveData<>();

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

    public LiveData<ArrayList<Student>> getStudentsInGroup() {
        return studentsInGroup;
    }

    public LiveData<ArrayList<ArrayList<StudentEvent>>> getStudentsEvents() {
        return studentsEvents;
    }

    public LiveData<HashMap<String, ArrayList<ArrayList<StudentLesson>>>> getStudentsLessonsByModules() {
        return studentsLessonsByModules;
    }

    public void downloadStudentsInGroup(String group) {
        this.studentsInGroup.setValue(Repository.getInstance().getStudentsInGroup(group));
    }

    public void downloadStudentsEvents(String subject, String group) {
        this.studentsEvents.setValue(Repository.getInstance().getStudentsEvents(subject, group));
    }

    public void downloadStudentsLessonsByModules(String subject, String group) {
        this.studentsLessonsByModules.setValue(Repository.getInstance().getStudentsLessonsByModules(subject, group));
    }
}