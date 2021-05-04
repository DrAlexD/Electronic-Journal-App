package com.example.electronicdiary.search.available;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;
import com.example.electronicdiary.data_classes.Student;

import java.util.List;

public class SearchAvailableStudentsViewModel extends ViewModel {
    private final MutableLiveData<List<Student>> availableStudents = new MutableLiveData<>();

    public LiveData<List<Student>> getAvailableStudents() {
        return availableStudents;
    }

    public void downloadAvailableStudents(long semesterId) {
        this.availableStudents.setValue(Repository.getInstance().getAvailableStudents(semesterId));
    }
}