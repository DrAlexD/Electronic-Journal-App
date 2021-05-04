package com.example.electronicdiary.student;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;
import com.example.electronicdiary.data_classes.Group;
import com.example.electronicdiary.data_classes.Semester;
import com.example.electronicdiary.data_classes.Student;
import com.example.electronicdiary.data_classes.Subject;

import java.util.List;

public class StudentProfileViewModel extends ViewModel {
    private final MutableLiveData<Semester> semester = new MutableLiveData<>();
    private final MutableLiveData<Student> student = new MutableLiveData<>();
    private final MutableLiveData<Group> group = new MutableLiveData<>();
    private final MutableLiveData<List<Subject>> availableStudentSubjects = new MutableLiveData<>();

    public MutableLiveData<Semester> getSemester() {
        return semester;
    }


    public LiveData<Student> getStudent() {
        return student;
    }

    public MutableLiveData<Group> getGroup() {
        return group;
    }

    public LiveData<List<Subject>> getAvailableStudentSubjects() {
        return availableStudentSubjects;
    }

    public void downloadSemesterById(long semesterId) {
        this.semester.setValue(Repository.getInstance().getSemesterById(semesterId));
    }

    public void downloadStudentById(long studentId) {
        Repository.getInstance().getStudentById(studentId, student);
    }

    public void downloadGroupById(long groupId) {
        this.group.setValue(Repository.getInstance().getGroupById(groupId));
    }

    public void downloadAvailableStudentSubjects(long studentId, long semesterId) {
        this.availableStudentSubjects.setValue(Repository.getInstance().getAvailableStudentSubjects(studentId, semesterId));
    }
}