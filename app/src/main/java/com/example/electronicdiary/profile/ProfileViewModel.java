package com.example.electronicdiary.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;

import java.util.ArrayList;
import java.util.HashMap;

public class ProfileViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<String>> availableSubjects = new MutableLiveData<>();
    private final MutableLiveData<HashMap<String, ArrayList<String>>> availableSubjectsWithGroups = new MutableLiveData<>();

    public LiveData<ArrayList<String>> getAvailableSubjects() {
        return availableSubjects;
    }

    public LiveData<HashMap<String, ArrayList<String>>> getAvailableSubjectsWithGroups() {
        return availableSubjectsWithGroups;
    }

    public void downloadAvailableSubjectsWithGroups() {
        this.availableSubjects.setValue(Repository.getInstance().getAvailableSubjects());
        this.availableSubjectsWithGroups.setValue(Repository.getInstance().getAvailableSubjectsWithGroups());
    }
}