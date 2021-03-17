package com.example.electronicdiary.admin.adding;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;

public class GroupAddingViewModel extends ViewModel {
    private final MutableLiveData<GroupAddingFormState> groupAddingFormState = new MutableLiveData<>();

    LiveData<GroupAddingFormState> getGroupAddingFormState() {
        return groupAddingFormState;
    }

    public void groupAddingDataChanged(String groupTitle) {
        groupAddingFormState.setValue(new GroupAddingFormState(groupTitle));
    }

    public void addGroup(String groupTitle) {
        Repository.getInstance().addGroup(groupTitle);
    }
}