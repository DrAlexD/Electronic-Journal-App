package com.example.electronicdiary.student;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;
import com.example.electronicdiary.Subject;

import java.util.ArrayList;

public class StudentProfileViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<Subject>> availableStudentSubjects = new MutableLiveData<>();

    public LiveData<ArrayList<Subject>> getAvailableStudentSubjects() {
        return availableStudentSubjects;
    }

    public void downloadAvailableStudentSubjects(int studentId, int semesterId) {
        this.availableStudentSubjects.setValue(Repository.getInstance().getAvailableStudentSubjects(studentId, semesterId));
    }
}