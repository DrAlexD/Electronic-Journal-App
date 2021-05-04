package com.example.electronicdiary.search.all;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;
import com.example.electronicdiary.data_classes.Professor;

import java.util.List;

public class SearchProfessorsViewModel extends ViewModel {
    private final MutableLiveData<List<Professor>> professors = new MutableLiveData<>();

    public LiveData<List<Professor>> getProfessors() {
        return professors;
    }

    public void downloadProfessors() {
        Repository.getInstance().getProfessors(professors);
    }

    public void deleteProfessor(long professorId) {
        Repository.getInstance().deleteProfessor(professorId);
    }
}