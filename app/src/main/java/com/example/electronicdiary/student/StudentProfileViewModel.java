package com.example.electronicdiary.student;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class StudentProfileViewModel extends ViewModel {
    private final MutableLiveData<String> studentName = new MutableLiveData<>();
    private final MutableLiveData<String> group = new MutableLiveData<>();

    private final MutableLiveData<ArrayList<String>> subjects = new MutableLiveData<>();

    public MutableLiveData<String> getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName.setValue(studentName);
    }

    public MutableLiveData<String> getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group.setValue(group);
    }

    public MutableLiveData<ArrayList<String>> getSubjects() {
        return subjects;
    }

    public void setSubjects(ArrayList<String> subjects) {
        this.subjects.setValue(subjects);
    }
}
