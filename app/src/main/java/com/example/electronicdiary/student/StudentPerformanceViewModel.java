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
    private final MutableLiveData<HashMap<String, ArrayList<StudentLesson>>> studentLessonsByModules = new MutableLiveData<>();

    public LiveData<ArrayList<StudentEvent>> getStudentEvents() {
        return studentEvents;
    }

    public LiveData<HashMap<String, ArrayList<StudentLesson>>> getStudentLessonsByModules() {
        return studentLessonsByModules;
    }

    public void downloadStudentEvents(String studentName, String subject, String group) {
        this.studentEvents.setValue(Repository.getInstance().getStudentEvents(studentName, subject, group));
    }

    public void downloadStudentLessonsByModules(String studentName, String subject, String group) {
        this.studentLessonsByModules.setValue(Repository.getInstance().getStudentLessonsByModules(studentName, subject, group));
    }
}