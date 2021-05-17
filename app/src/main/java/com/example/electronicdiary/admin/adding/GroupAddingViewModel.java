package com.example.electronicdiary.admin.adding;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;
import com.example.electronicdiary.admin.GroupFormState;
import com.example.electronicdiary.data_classes.Group;

public class GroupAddingViewModel extends ViewModel {
    private final MutableLiveData<GroupFormState> groupFormState = new MutableLiveData<>();
    private final MutableLiveData<Boolean> answer = new MutableLiveData<>();

    public LiveData<Boolean> getAnswer() {
        return answer;
    }

    LiveData<GroupFormState> getGroupFormState() {
        return groupFormState;
    }

    public void groupAddingDataChanged(String groupTitle) {
        groupFormState.setValue(new GroupFormState(groupTitle));
    }

    public void addGroup(String groupTitle) {
        Repository.getInstance().addGroup(new Group(groupTitle), answer);
    }
}