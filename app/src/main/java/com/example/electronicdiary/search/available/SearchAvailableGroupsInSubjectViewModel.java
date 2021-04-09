package com.example.electronicdiary.search.available;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;
import com.example.electronicdiary.data_classes.SubjectInfo;

import java.util.ArrayList;

public class SearchAvailableGroupsInSubjectViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<SubjectInfo>> availableGroupsInSubject = new MutableLiveData<>();

    public LiveData<ArrayList<SubjectInfo>> getAvailableGroupsInSubject() {
        return availableGroupsInSubject;
    }

    public void downloadAvailableGroupsInSubject(int professorId, int subjectId, int semesterId) {
        this.availableGroupsInSubject.setValue(Repository.getInstance().
                getAvailableGroupsInSubject(professorId, subjectId, semesterId));
    }

    public void deleteGroupInAvailableSubject(int professorId, int groupId, int subjectId, int semesterId) {
        Repository.getInstance().deleteGroupInAvailableSubject(professorId, groupId, subjectId, semesterId);
    }
}