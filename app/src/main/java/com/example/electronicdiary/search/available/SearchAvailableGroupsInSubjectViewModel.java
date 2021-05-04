package com.example.electronicdiary.search.available;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;
import com.example.electronicdiary.data_classes.SubjectInfo;

import java.util.List;

public class SearchAvailableGroupsInSubjectViewModel extends ViewModel {
    private final MutableLiveData<List<SubjectInfo>> availableGroupsInSubject = new MutableLiveData<>();

    public LiveData<List<SubjectInfo>> getAvailableGroupsInSubject() {
        return availableGroupsInSubject;
    }

    public void downloadAvailableGroupsInSubject(long professorId, long subjectId, long semesterId) {
        this.availableGroupsInSubject.setValue(Repository.getInstance().
                getAvailableGroupsInSubject(professorId, subjectId, semesterId));
    }

    public void deleteGroupInAvailableSubject(long professorId, long groupId, long subjectId, long semesterId) {
        Repository.getInstance().deleteGroupInAvailableSubject(professorId, groupId, subjectId, semesterId);
    }
}