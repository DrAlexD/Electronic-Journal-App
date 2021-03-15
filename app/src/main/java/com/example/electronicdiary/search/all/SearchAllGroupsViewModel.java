package com.example.electronicdiary.search.all;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;

import java.util.ArrayList;

public class SearchAllGroupsViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<String>> allGroups = new MutableLiveData<>();

    public LiveData<ArrayList<String>> getAllGroups() {
        return allGroups;
    }

    public void downloadAllGroups() {
        this.allGroups.setValue(Repository.getInstance().getAllGroups());
    }
}