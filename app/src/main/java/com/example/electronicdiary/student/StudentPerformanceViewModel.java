package com.example.electronicdiary.student;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;
import com.example.electronicdiary.StudentEvent;
import com.example.electronicdiary.StudentLesson;

import java.util.ArrayList;
import java.util.HashMap;

public class StudentPerformanceViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<StudentEvent>> studentEvents = new MutableLiveData<>();
    private final MutableLiveData<HashMap<Integer, ArrayList<StudentLesson>>> studentLessonsByModules = new MutableLiveData<>();

    public LiveData<ArrayList<StudentEvent>> getStudentEvents() {
        return studentEvents;
    }

    public LiveData<HashMap<Integer, ArrayList<StudentLesson>>> getStudentLessonsByModules() {
        return studentLessonsByModules;
    }

    public void downloadStudentEvents(int studentId, int subjectId, int semesterId) {
        this.studentEvents.setValue(Repository.getInstance().getStudentEvents(studentId, subjectId, semesterId));
    }

    public void downloadStudentLessonsByModules(int studentId, int subjectId, int semesterId) {
        this.studentLessonsByModules.setValue(Repository.getInstance().getStudentLessonsByModules(studentId, subjectId, semesterId));
    }
}