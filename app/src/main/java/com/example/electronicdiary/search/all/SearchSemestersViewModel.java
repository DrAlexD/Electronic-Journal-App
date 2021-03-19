package com.example.electronicdiary.search.all;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;
import com.example.electronicdiary.Semester;

import java.util.ArrayList;

public class SearchSemestersViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<Semester>> semesters = new MutableLiveData<>();

    public LiveData<ArrayList<Semester>> getSemesters() {
        return semesters;
    }

    public void downloadSemesters() {
        this.semesters.setValue(Repository.getInstance().getSemesters());
    }

    public void deleteSemester(int semesterId) {
        Repository.getInstance().deleteSemester(semesterId);
    }
}