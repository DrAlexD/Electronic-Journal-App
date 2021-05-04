package com.example.electronicdiary.search.all;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;
import com.example.electronicdiary.data_classes.Subject;

import java.util.List;

public class SearchAllSubjectsViewModel extends ViewModel {
    private final MutableLiveData<List<Subject>> allSubjects = new MutableLiveData<>();

    public LiveData<List<Subject>> getAllSubjects() {
        return allSubjects;
    }

    public void downloadAllSubjects() {
        this.allSubjects.setValue(Repository.getInstance().getAllSubjects());
    }

    public void deleteSubject(long subjectId) {
        Repository.getInstance().deleteSubject(subjectId);
    }
}