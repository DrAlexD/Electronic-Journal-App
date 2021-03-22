package com.example.electronicdiary.search.available;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;
import com.example.electronicdiary.Student;

import java.util.ArrayList;

public class SearchAvailableStudentsViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<Student>> availableStudents = new MutableLiveData<>();

    public LiveData<ArrayList<Student>> getAvailableStudents() {
        return availableStudents;
    }

    public void downloadAvailableStudents(int semesterId) {
        this.availableStudents.setValue(Repository.getInstance().getAvailableStudents(semesterId));
    }
}