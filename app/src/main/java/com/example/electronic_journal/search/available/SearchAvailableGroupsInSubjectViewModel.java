package com.example.electronic_journal.search.available;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronic_journal.Repository;
import com.example.electronic_journal.data_classes.SubjectInfo;

import java.util.List;

public class SearchAvailableGroupsInSubjectViewModel extends ViewModel {
    private final MutableLiveData<List<SubjectInfo>> availableGroupsInSubject = new MutableLiveData<>();
    private final MutableLiveData<Boolean> answer = new MutableLiveData<>();

    public LiveData<Boolean> getAnswer() {
        return answer;
    }

    public LiveData<List<SubjectInfo>> getAvailableGroupsInSubject() {
        return availableGroupsInSubject;
    }

    public void downloadAvailableGroupsInSubject(long professorId, long subjectId, long semesterId) {
        Repository.getInstance().getAvailableGroupsInSubject(professorId, subjectId, semesterId, availableGroupsInSubject);
    }

    public void deleteSubjectInfo(long subjectInfoId, long professorId) {
        Repository.getInstance().deleteSubjectInfo(subjectInfoId, professorId, answer);
    }
}