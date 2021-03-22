package com.example.electronicdiary.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Group;
import com.example.electronicdiary.Repository;
import com.example.electronicdiary.Subject;

import java.util.ArrayList;
import java.util.HashMap;

public class ProfileViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<Subject>> availableSubjects = new MutableLiveData<>();
    private final MutableLiveData<HashMap<Subject, ArrayList<Group>>> availableSubjectsWithGroups = new MutableLiveData<>();

    public LiveData<ArrayList<Subject>> getAvailableSubjects() {
        return availableSubjects;
    }

    public LiveData<HashMap<Subject, ArrayList<Group>>> getAvailableSubjectsWithGroups() {
        return availableSubjectsWithGroups;
    }

    public void downloadAvailableSubjectsWithGroups(int semesterId) {
        this.availableSubjects.setValue(Repository.getInstance().getAvailableSubjects(semesterId));
        this.availableSubjectsWithGroups.setValue(Repository.getInstance().getAvailableSubjectsWithGroups(semesterId));
    }
}