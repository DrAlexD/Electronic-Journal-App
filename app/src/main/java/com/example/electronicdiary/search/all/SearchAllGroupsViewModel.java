package com.example.electronicdiary.search.all;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;
import com.example.electronicdiary.data_classes.Group;

import java.util.List;

public class SearchAllGroupsViewModel extends ViewModel {
    private final MutableLiveData<List<Group>> allGroups = new MutableLiveData<>();

    public LiveData<List<Group>> getAllGroups() {
        return allGroups;
    }

    public void downloadAllGroups() {
        this.allGroups.setValue(Repository.getInstance().getAllGroups());
    }

    public void addStudent(String studentName, String studentSecondName, long studentGroupId, String studentLogin,
                           String studentPassword) {
        Repository.getInstance().addStudent(studentName, studentSecondName, studentGroupId, studentLogin, studentPassword);
    }

    public void deleteGroup(long groupId) {
        Repository.getInstance().deleteGroup(groupId);
    }

    public void changeStudentGroup(long studentId, long newStudentGroupId) {
        Repository.getInstance().changeStudentGroup(studentId, newStudentGroupId);
    }
}