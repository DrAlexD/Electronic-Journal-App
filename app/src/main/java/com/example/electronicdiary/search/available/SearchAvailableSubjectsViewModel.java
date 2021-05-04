package com.example.electronicdiary.search.available;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;
import com.example.electronicdiary.data_classes.Subject;

import java.util.List;

public class SearchAvailableSubjectsViewModel extends ViewModel {
    private final MutableLiveData<List<Subject>> availableSubjects = new MutableLiveData<>();

    public LiveData<List<Subject>> getAvailableSubjects() {
        return availableSubjects;
    }

    public void downloadAvailableSubjects(long semesterId) {
        this.availableSubjects.setValue(Repository.getInstance().getAvailableSubjects(semesterId));
    }

    public void deleteAvailableSubject(long professorId, long subjectId, long semesterId) {
        Repository.getInstance().deleteAvailableSubject(professorId, subjectId, semesterId);
    }
}