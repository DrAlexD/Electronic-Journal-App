package com.example.electronicdiary.settings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;
import com.example.electronicdiary.data_classes.Semester;

import java.util.ArrayList;

public class SettingsViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<Semester>> semesters = new MutableLiveData<>();

    public LiveData<ArrayList<Semester>> getSemesters() {
        return semesters;
    }

    public void downloadSemesters() {
        this.semesters.setValue(Repository.getInstance().getSemesters());
    }
}