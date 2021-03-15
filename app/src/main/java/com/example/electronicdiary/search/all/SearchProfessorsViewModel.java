package com.example.electronicdiary.search.all;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Professor;
import com.example.electronicdiary.Repository;

import java.util.ArrayList;

public class SearchProfessorsViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<Professor>> professors = new MutableLiveData<>();

    public LiveData<ArrayList<Professor>> getProfessors() {
        return professors;
    }

    public void downloadProfessors() {
        this.professors.setValue(Repository.getInstance().getProfessors());
    }
}