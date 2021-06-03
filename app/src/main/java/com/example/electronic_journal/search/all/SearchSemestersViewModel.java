package com.example.electronic_journal.search.all;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronic_journal.Repository;
import com.example.electronic_journal.data_classes.Semester;

import java.util.List;

public class SearchSemestersViewModel extends ViewModel {
    private final MutableLiveData<List<Semester>> semesters = new MutableLiveData<>();
    private final MutableLiveData<Boolean> answer = new MutableLiveData<>();

    public LiveData<Boolean> getAnswer() {
        return answer;
    }

    public LiveData<List<Semester>> getSemesters() {
        return semesters;
    }

    public void downloadSemesters() {
        Repository.getInstance().getSemesters(semesters);
    }

    public void deleteSemester(long semesterId) {
        Repository.getInstance().deleteSemester(semesterId, answer);
    }
}