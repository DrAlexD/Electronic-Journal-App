package com.example.electronicdiary.search.available;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;
import com.example.electronicdiary.data_classes.Subject;

import java.util.ArrayList;

public class SearchAvailableSubjectsViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<Subject>> availableSubjects = new MutableLiveData<>();

    public LiveData<ArrayList<Subject>> getAvailableSubjects() {
        return availableSubjects;
    }

    public void downloadAvailableSubjects(int semesterId) {
        this.availableSubjects.setValue(Repository.getInstance().getAvailableSubjects(semesterId));
    }

    public void deleteAvailableSubject(int professorId, int subjectId, int semesterId) {
        Repository.getInstance().deleteAvailableSubject(professorId, subjectId, semesterId);
    }
}