package com.example.electronic_journal.search.available;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronic_journal.Repository;
import com.example.electronic_journal.data_classes.Student;

import java.util.List;

public class SearchAvailableStudentsViewModel extends ViewModel {
    private final MutableLiveData<List<Student>> availableStudents = new MutableLiveData<>();

    public LiveData<List<Student>> getAvailableStudents() {
        return availableStudents;
    }

    public void downloadAvailableStudents(long professorId, long semesterId) {
        Repository.getInstance().getAvailableStudents(professorId, semesterId, availableStudents);
    }
}