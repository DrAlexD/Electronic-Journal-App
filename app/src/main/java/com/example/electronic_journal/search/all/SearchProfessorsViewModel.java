package com.example.electronic_journal.search.all;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronic_journal.Repository;
import com.example.electronic_journal.data_classes.Professor;

import java.util.List;

public class SearchProfessorsViewModel extends ViewModel {
    private final MutableLiveData<List<Professor>> professors = new MutableLiveData<>();
    private final MutableLiveData<Boolean> answer = new MutableLiveData<>();

    public LiveData<Boolean> getAnswer() {
        return answer;
    }

    public LiveData<List<Professor>> getProfessors() {
        return professors;
    }

    public void downloadProfessors() {
        Repository.getInstance().getProfessors(professors);
    }

    public void deleteProfessor(long professorId) {
        Repository.getInstance().deleteProfessor(professorId, answer);
    }
}