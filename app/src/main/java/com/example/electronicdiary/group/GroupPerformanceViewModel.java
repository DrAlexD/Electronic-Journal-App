package com.example.electronicdiary.group;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GroupPerformanceViewModel extends ViewModel {
    private final MutableLiveData<String> mGroup = new MutableLiveData<>();
    private final MutableLiveData<String> mSubject = new MutableLiveData<>();

    public LiveData<String> getSubject() {
        return mSubject;
    }

    public LiveData<String> getGroup() {
        return mGroup;
    }

    public void setGroupAndSubject(String group, String subject) {
        mGroup.setValue(group);
        mSubject.setValue(subject);
    }
}