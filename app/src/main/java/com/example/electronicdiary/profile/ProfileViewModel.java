package com.example.electronicdiary.profile;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;

public class ProfileViewModel extends ViewModel {
    private final MutableLiveData<HashMap<String, ArrayList<String>>> subjectsWithGroups = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<String>> subjects = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<ArrayList<String>>> groups = new MutableLiveData<>();

    public MutableLiveData<HashMap<String, ArrayList<String>>> getSubjectsWithGroups() {
        return subjectsWithGroups;
    }

    public void setSubjectsWithGroups(HashMap<String, ArrayList<String>> subjectsWithGroups) {
        this.subjectsWithGroups.setValue(subjectsWithGroups);
    }

    public MutableLiveData<ArrayList<String>> getSubjects() {
        return subjects;
    }

    public void setSubjects(ArrayList<String> subjects) {
        this.subjects.setValue(subjects);
    }

    public MutableLiveData<ArrayList<ArrayList<String>>> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<ArrayList<String>> groups) {
        this.groups.setValue(groups);
    }
}