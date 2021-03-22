package com.example.electronicdiary.search.all;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Group;
import com.example.electronicdiary.Repository;

import java.util.ArrayList;

public class SearchAllGroupsViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<Group>> allGroups = new MutableLiveData<>();

    public LiveData<ArrayList<Group>> getAllGroups() {
        return allGroups;
    }

    public void downloadAllGroups() {
        this.allGroups.setValue(Repository.getInstance().getAllGroups());
    }

    public void addStudent(String studentName, String studentSecondName, int studentGroupId, String studentLogin,
                           String studentPassword) {
        Repository.getInstance().addStudent(studentName, studentSecondName, studentGroupId, studentLogin, studentPassword);
    }

    public void deleteGroup(int groupId) {
        Repository.getInstance().deleteGroup(groupId);
    }

    public void changeStudentGroup(int studentId, int newStudentGroupId) {
        Repository.getInstance().changeStudentGroup(studentId, newStudentGroupId);
    }

    public void addAvailableSubject(int professorId, boolean isLecturer, int groupId, int subjectId, int semesterId, boolean isExam) {
        Repository.getInstance().addAvailableSubject(professorId, isLecturer, groupId, subjectId, semesterId, isExam);
    }

    public void addGroupInAvailableSubject(int lecturerId, int seminarianId, int groupId, int subjectId, int semesterId, boolean isExam) {
        Repository.getInstance().addGroupInAvailableSubject(lecturerId, seminarianId, groupId, subjectId, semesterId, isExam);
    }
}