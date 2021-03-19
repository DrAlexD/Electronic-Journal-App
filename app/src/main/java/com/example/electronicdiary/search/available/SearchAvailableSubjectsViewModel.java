package com.example.electronicdiary.search.available;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;

import java.util.ArrayList;

public class SearchAvailableSubjectsViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<String>> availableSubjects = new MutableLiveData<>();

    public LiveData<ArrayList<String>> getAvailableSubjects() {
        return availableSubjects;
    }

    public void downloadAvailableSubjects() {
        this.availableSubjects.setValue(Repository.getInstance().getAvailableSubjects());
    }

    public void deleteAvailableSubject(int professorId, int subjectId) {
        Repository.getInstance().deleteAvailableSubject(professorId, subjectId);
    }
}