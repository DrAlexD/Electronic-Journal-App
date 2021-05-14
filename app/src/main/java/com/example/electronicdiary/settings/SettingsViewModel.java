package com.example.electronicdiary.settings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;
import com.example.electronicdiary.data_classes.Semester;

import java.util.List;

public class SettingsViewModel extends ViewModel {
    private final MutableLiveData<List<Semester>> semesters = new MutableLiveData<>();

    public LiveData<List<Semester>> getSemesters() {
        return semesters;
    }

    public void downloadSemesters() {
        Repository.getInstance().getSemesters(semesters);
    }
}