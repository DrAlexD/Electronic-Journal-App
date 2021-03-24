package com.example.electronicdiary.group;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Event;
import com.example.electronicdiary.Lesson;
import com.example.electronicdiary.Repository;
import com.example.electronicdiary.Student;
import com.example.electronicdiary.StudentEvent;
import com.example.electronicdiary.StudentLesson;

import java.util.ArrayList;
import java.util.HashMap;

public class GroupPerformanceViewModel extends ViewModel {
    private final MutableLiveData<Integer> semesterId = new MutableLiveData<>();
    private final MutableLiveData<Integer> groupId = new MutableLiveData<>();
    private final MutableLiveData<Integer> subjectId = new MutableLiveData<>();

    private final MutableLiveData<ArrayList<Student>> studentsInGroup = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Event>> events = new MutableLiveData<>();
    private final MutableLiveData<HashMap<Integer, ArrayList<Lesson>>> lessons = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<ArrayList<StudentEvent>>> studentsEvents = new MutableLiveData<>();
    private final MutableLiveData<HashMap<Integer, ArrayList<ArrayList<StudentLesson>>>> studentsLessonsByModules = new MutableLiveData<>();

    public MutableLiveData<Integer> getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(Integer semesterId) {
        this.semesterId.setValue(semesterId);
    }

    public LiveData<Integer> getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId) {
        this.subjectId.setValue(subjectId);
    }

    public LiveData<Integer> getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId.setValue(groupId);
    }

    public LiveData<ArrayList<Student>> getStudentsInGroup() {
        return studentsInGroup;
    }

    public MutableLiveData<ArrayList<Event>> getEvents() {
        return events;
    }

    public MutableLiveData<HashMap<Integer, ArrayList<Lesson>>> getLessons() {
        return lessons;
    }

    public LiveData<ArrayList<ArrayList<StudentEvent>>> getStudentsEvents() {
        return studentsEvents;
    }

    public LiveData<HashMap<Integer, ArrayList<ArrayList<StudentLesson>>>> getStudentsLessonsByModules() {
        return studentsLessonsByModules;
    }

    public void downloadStudentsInGroup(int groupId) {
        this.studentsInGroup.setValue(Repository.getInstance().getStudentsInGroup(groupId));
    }

    public void downloadEvents(int groupId, int subjectId, int semesterId) {
        this.events.setValue(Repository.getInstance().getEvents(groupId, subjectId, semesterId));
    }

    public void downloadLessonsByModules(int groupId, int subjectId, int semesterId) {
        this.lessons.setValue(Repository.getInstance().getLessonsByModules(groupId, subjectId, semesterId));
    }

    public void downloadStudentsEvents(int groupId, int subjectId, int semesterId) {
        this.studentsEvents.setValue(Repository.getInstance().getStudentsEvents(groupId, subjectId, semesterId));
    }

    public void downloadStudentsLessonsByModules(int groupId, int subjectId, int semesterId) {
        this.studentsLessonsByModules.setValue(Repository.getInstance().getStudentsLessonsByModules(groupId, subjectId, semesterId));
    }
}