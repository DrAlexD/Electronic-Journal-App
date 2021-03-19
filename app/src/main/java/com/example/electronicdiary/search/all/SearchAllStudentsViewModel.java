package com.example.electronicdiary.search.all;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;
import com.example.electronicdiary.Student;

import java.util.ArrayList;

public class SearchAllStudentsViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<Student>> allStudents = new MutableLiveData<>();

    public LiveData<ArrayList<Student>> getAllStudents() {
        return allStudents;
    }

    public void downloadAllStudents() {
        this.allStudents.setValue(Repository.getInstance().getAllStudents());
    }

    public void deleteStudent(int studentId) {
        Repository.getInstance().deleteStudent(studentId);
    }
}