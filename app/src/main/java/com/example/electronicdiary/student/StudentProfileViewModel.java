package com.example.electronicdiary.student;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Group;
import com.example.electronicdiary.Repository;
import com.example.electronicdiary.Student;
import com.example.electronicdiary.Subject;

import java.util.ArrayList;

public class StudentProfileViewModel extends ViewModel {
    private final MutableLiveData<Student> student = new MutableLiveData<>();
    private final MutableLiveData<Group> group = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Subject>> availableStudentSubjects = new MutableLiveData<>();

    public MutableLiveData<Student> getStudent() {
        return student;
    }

    public MutableLiveData<Group> getGroup() {
        return group;
    }

    public LiveData<ArrayList<Subject>> getAvailableStudentSubjects() {
        return availableStudentSubjects;
    }

    public void downloadStudentById(int studentId) {
        this.student.setValue(Repository.getInstance().getStudentById(studentId));
    }

    public void downloadGroupById(int groupId) {
        this.group.setValue(Repository.getInstance().getGroupById(groupId));
    }

    public void downloadAvailableStudentSubjects(int studentId, int semesterId) {
        this.availableStudentSubjects.setValue(Repository.getInstance().getAvailableStudentSubjects(studentId, semesterId));
    }
}