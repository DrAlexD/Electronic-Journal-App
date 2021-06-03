package com.example.electronic_journal.admin.adding;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronic_journal.Repository;
import com.example.electronic_journal.Result;
import com.example.electronic_journal.admin.GroupFormState;
import com.example.electronic_journal.data_classes.Group;

public class GroupAddingViewModel extends ViewModel {
    private final MutableLiveData<GroupFormState> groupFormState = new MutableLiveData<>();
    private MutableLiveData<Result<Boolean>> answer = new MutableLiveData<>();

    public LiveData<Result<Boolean>> getAnswer() {
        return answer;
    }

    public void setAnswer(MutableLiveData<Result<Boolean>> answer) {
        this.answer = answer;
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