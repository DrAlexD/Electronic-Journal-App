package com.example.electronicdiary.student;

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

import java.util.ArrayList;
import java.util.HashMap;

public class StudentPerformanceViewModel extends ViewModel {
    private final MutableLiveData<Integer> moduleExpand = new MutableLiveData<>();
    private final MutableLiveData<Integer> openPage = new MutableLiveData<>();

    private final MutableLiveData<SubjectInfo> subjectInfo = new MutableLiveData<>();
    private final MutableLiveData<HashMap<Integer, ModuleInfo>> moduleInfo = new MutableLiveData<>();
    private final MutableLiveData<Student> student = new MutableLiveData<>();
    private final MutableLiveData<HashMap<Integer, ArrayList<Event>>> events = new MutableLiveData<>();

    private final MutableLiveData<StudentPerformanceInSubject> studentPerformanceInSubject = new MutableLiveData<>();
    private final MutableLiveData<HashMap<Integer, StudentPerformanceInModule>> studentPerformanceInModules = new MutableLiveData<>();
    private final MutableLiveData<HashMap<Integer, ArrayList<StudentEvent>>> studentEvents = new MutableLiveData<>();
    private final MutableLiveData<HashMap<Integer, ArrayList<Lesson>>> lecturesByModules = new MutableLiveData<>();
    private final MutableLiveData<HashMap<Integer, ArrayList<Lesson>>> seminarsByModules = new MutableLiveData<>();
    private final MutableLiveData<HashMap<Integer, ArrayList<StudentLesson>>> studentLessonsByModules = new MutableLiveData<>();


    public MutableLiveData<Integer> getModuleExpand() {
        return moduleExpand;
    }

    public void setModuleExpand(int moduleExpand) {
        this.moduleExpand.setValue(moduleExpand);
    }

    public MutableLiveData<Integer> getOpenPage() {
        return openPage;
    }

    public void setOpenPage(int openPage) {
        this.openPage.setValue(openPage);
    }

    public LiveData<Student> getStudent() {
        return student;
    }

    public MutableLiveData<SubjectInfo> getSubjectInfo() {
        return subjectInfo;
    }

    public MutableLiveData<HashMap<Integer, ModuleInfo>> getModuleInfo() {
        return moduleInfo;
    }

    public LiveData<StudentPerformanceInSubject> getStudentPerformanceInSubject() {
        return studentPerformanceInSubject;
    }

    public LiveData<HashMap<Integer, StudentPerformanceInModule>> getStudentPerformanceInModules() {
        return studentPerformanceInModules;
    }

    public LiveData<HashMap<Integer, ArrayList<Event>>> getEvents() {
        return events;
    }

    public LiveData<HashMap<Integer, ArrayList<StudentEvent>>> getStudentEvents() {
        return studentEvents;
    }

    public LiveData<HashMap<Integer, ArrayList<Lesson>>> getLecturesByModules() {
        return lecturesByModules;
    }

    public LiveData<HashMap<Integer, ArrayList<Lesson>>> getSeminarsByModules() {
        return seminarsByModules;
    }

    public LiveData<HashMap<Integer, ArrayList<StudentLesson>>> getStudentLessonsByModules() {
        return studentLessonsByModules;
    }

    public void downloadStudentById(int studentId) {
        this.student.setValue(Repository.getInstance().getStudentById(studentId));
    }

    public void downloadSubjectInfo(int groupId, int subjectId, int semesterId) {
        this.subjectInfo.setValue(Repository.getInstance().getSubjectInfo(groupId, subjectId, semesterId));
    }

    public void downloadEventsAndLessons(int studentId, int groupId, int subjectId, int lecturerId, int seminarianId, int semesterId) {
        this.moduleInfo.setValue(Repository.getInstance().getModuleInfo(groupId, subjectId, lecturerId, seminarianId, semesterId));
        this.studentPerformanceInSubject.setValue(Repository.getInstance().getStudentPerformanceInSubject(studentId, groupId, subjectId, lecturerId, seminarianId, semesterId));
        this.studentPerformanceInModules.setValue(Repository.getInstance().getStudentPerformanceInModules(studentId, groupId, subjectId, lecturerId, seminarianId, semesterId));

        this.events.setValue(Repository.getInstance().getEvents(groupId, subjectId, lecturerId, seminarianId, semesterId));
        this.lecturesByModules.setValue(Repository.getInstance().getLectures(groupId, subjectId, lecturerId, seminarianId, semesterId));
        this.seminarsByModules.setValue(Repository.getInstance().getSeminars(groupId, subjectId, lecturerId, seminarianId, semesterId));
        this.studentEvents.setValue(Repository.getInstance().getStudentEvents(studentId, subjectId, lecturerId, seminarianId, semesterId));
        this.studentLessonsByModules.setValue(Repository.getInstance().getStudentLessons(studentId, subjectId, lecturerId, seminarianId, semesterId));
    }
}