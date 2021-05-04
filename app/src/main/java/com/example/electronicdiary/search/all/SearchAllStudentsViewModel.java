package com.example.electronicdiary.search.all;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;
import com.example.electronicdiary.data_classes.Student;

import java.util.List;

public class SearchAllStudentsViewModel extends ViewModel {
    private final MutableLiveData<List<Student>> allStudents = new MutableLiveData<>();

    public LiveData<List<Student>> getAllStudents() {
        return allStudents;
    }

    public void downloadAllStudents() {
        Repository.getInstance().getAllStudents(allStudents);
    }

    public void deleteStudent(long studentId) {
        Repository.getInstance().deleteStudent(studentId);
    }
}