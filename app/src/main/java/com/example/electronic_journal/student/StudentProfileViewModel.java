package com.example.electronic_journal.student;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronic_journal.Repository;
import com.example.electronic_journal.Result;
import com.example.electronic_journal.data_classes.Group;
import com.example.electronic_journal.data_classes.Semester;
import com.example.electronic_journal.data_classes.Student;
import com.example.electronic_journal.data_classes.StudentPerformanceInSubject;

import java.util.List;

public class StudentProfileViewModel extends ViewModel {
    private final MutableLiveData<Semester> semester = new MutableLiveData<>();
    private final MutableLiveData<Result<Student>> student = new MutableLiveData<>();
    private final MutableLiveData<Group> group = new MutableLiveData<>();
    private final MutableLiveData<List<StudentPerformanceInSubject>> availableStudentSubjects = new MutableLiveData<>();

    public LiveData<Semester> getSemester() {
        return semester;
    }

    public LiveData<Result<Student>> getStudent() {
        return student;
    }

    public LiveData<Group> getGroup() {
        return group;
    }

    public LiveData<List<StudentPerformanceInSubject>> getAvailableStudentSubjects() {
        return availableStudentSubjects;
    }

    public void downloadSemesterById(long semesterId) {
        Repository.getInstance().getSemesterById(semesterId, semester);
    }

    public void downloadStudentById(long studentId) {
        Repository.getInstance().getStudentById(studentId, student);
    }

    public void downloadGroupById(long groupId) {
        Repository.getInstance().getGroupById(groupId, group);
    }

    public void downloadAvailableStudentSubjects(long studentId, long semesterId) {
        Repository.getInstance().getAvailableStudentSubjects(studentId, semesterId, availableStudentSubjects);
    }
}