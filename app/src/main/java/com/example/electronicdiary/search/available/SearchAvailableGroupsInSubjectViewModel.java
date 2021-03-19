package com.example.electronicdiary.search.available;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;

import java.util.ArrayList;

public class SearchAvailableGroupsInSubjectViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<String>> availableGroupsInSubject = new MutableLiveData<>();

    public LiveData<ArrayList<String>> getAvailableGroupsInSubject() {
        return availableGroupsInSubject;
    }

    public void downloadAvailableGroupsInSubject(String subjectTitle) {
        this.availableGroupsInSubject.setValue(Repository.getInstance().getAvailableGroupsInSubject(subjectTitle));
    }

    public void deleteGroupInAvailableSubject(int professorId, int groupId, int subjectId) {
        Repository.getInstance().deleteGroupInAvailableSubject(professorId, groupId, subjectId);
    }
}