package com.example.electronicdiary.student;

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

public class StudentPerformanceViewModel extends ViewModel {
    private final MutableLiveData<Student> student = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Event>> events = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<StudentEvent>> studentEvents = new MutableLiveData<>();
    private final MutableLiveData<HashMap<Integer, ArrayList<Lesson>>> lecturesByModules = new MutableLiveData<>();
    private final MutableLiveData<HashMap<Integer, ArrayList<Lesson>>> seminarsByModules = new MutableLiveData<>();
    private final MutableLiveData<HashMap<Integer, ArrayList<StudentLesson>>> studentLessonsByModules = new MutableLiveData<>();

    public MutableLiveData<Student> getStudent() {
        return student;
    }


    public MutableLiveData<ArrayList<Event>> getEvents() {
        return events;
    }

    public LiveData<ArrayList<StudentEvent>> getStudentEvents() {
        return studentEvents;
    }

    public MutableLiveData<HashMap<Integer, ArrayList<Lesson>>> getLecturesByModules() {
        return lecturesByModules;
    }

    public MutableLiveData<HashMap<Integer, ArrayList<Lesson>>> getSeminarsByModules() {
        return seminarsByModules;
    }

    public LiveData<HashMap<Integer, ArrayList<StudentLesson>>> getStudentLessonsByModules() {
        return studentLessonsByModules;
    }

    public void downloadStudentById(int studentId) {
        this.student.setValue(Repository.getInstance().getStudentById(studentId));
    }

    public void downloadEvents(int groupId, int subjectId, int semesterId) {
        this.events.setValue(Repository.getInstance().getEvents(groupId, subjectId, semesterId));
    }

    public void downloadStudentEvents(int studentId, int subjectId, int semesterId) {
        this.studentEvents.setValue(Repository.getInstance().getStudentEvents(studentId, subjectId, semesterId));
    }

    public void downloadLessonsByModules(int groupId, int subjectId, int semesterId) {
        this.lecturesByModules.setValue(Repository.getInstance().getLecturesByModules(groupId, subjectId, semesterId));
        this.seminarsByModules.setValue(Repository.getInstance().getSeminarsByModules(groupId, subjectId, semesterId));
    }

    public void downloadStudentLessonsByModules(int studentId, int subjectId, int semesterId) {
        this.studentLessonsByModules.setValue(Repository.getInstance().getStudentLessonsByModules(studentId, subjectId, semesterId));
    }
}