package com.example.electronicdiary.student;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;

import java.util.ArrayList;

public class StudentProfileViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<String>> availableStudentSubjects = new MutableLiveData<>();

    public LiveData<ArrayList<String>> getAvailableStudentSubjects() {
        return availableStudentSubjects;
    }

    public void downloadAvailableStudentSubjects(String studentName, String group) {
        this.availableStudentSubjects.setValue(Repository.getInstance().getAvailableStudentSubjects(studentName, group));
    }
}