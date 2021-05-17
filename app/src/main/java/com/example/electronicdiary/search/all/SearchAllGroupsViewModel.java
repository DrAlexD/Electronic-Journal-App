package com.example.electronicdiary.search.all;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;
import com.example.electronicdiary.data_classes.Group;
import com.example.electronicdiary.data_classes.Student;
import com.example.electronicdiary.data_classes.SubjectInfo;

import java.util.List;

public class SearchAllGroupsViewModel extends ViewModel {
    private final MutableLiveData<List<Group>> allGroups = new MutableLiveData<>();
    private final MutableLiveData<List<SubjectInfo>> availableGroupsInSubject = new MutableLiveData<>();
    private final MutableLiveData<Group> group = new MutableLiveData<>();
    private final MutableLiveData<Boolean> answer = new MutableLiveData<>();

    public LiveData<Boolean> getAnswer() {
        return answer;
    }

    public LiveData<List<Group>> getAllGroups() {
        return allGroups;
    }

    public LiveData<Group> getGroup() {
        return group;
    }

    public LiveData<List<SubjectInfo>> getAvailableGroupsInSubject() {
        return availableGroupsInSubject;
    }

    public void downloadAllGroups() {
        Repository.getInstance().getAllGroups(allGroups);
    }

    public void downloadGroupById(long groupId) {
        Repository.getInstance().getGroupById(groupId, group);
    }

    public void downloadAvailableGroupsInSubject(long professorId, long subjectId, long semesterId) {
        Repository.getInstance().getAvailableGroupsInSubject(professorId, subjectId, semesterId, availableGroupsInSubject);
    }

    public void addStudent(String studentName, String studentSecondName, Group group, String studentLogin,
                           String studentPassword) {
        Repository.getInstance().addStudent(new Student(studentName, studentSecondName, group, studentLogin, studentPassword, "ROLE_STUDENT"), answer);
    }

    public void deleteGroup(long groupId) {
        Repository.getInstance().deleteGroup(groupId, answer);
    }

    public void changeStudentGroup(long studentId, String studentName, String studentSecondName, Group group, String studentLogin,
                                   String studentPassword) {
        Repository.getInstance().editStudent(studentId, new Student(studentName, studentSecondName, group, studentLogin, studentPassword, "ROLE_STUDENT"), answer);
    }
}