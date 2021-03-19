package com.example.electronicdiary.search.all;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;

import java.util.ArrayList;

public class SearchAllSubjectsViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<String>> allSubjects = new MutableLiveData<>();

    public LiveData<ArrayList<String>> getAllSubjects() {
        return allSubjects;
    }

    public void downloadAllSubjects() {
        this.allSubjects.setValue(Repository.getInstance().getAllSubjects());
    }

    public void deleteSubject(int subjectId) {
        Repository.getInstance().deleteSubject(subjectId);
    }
}