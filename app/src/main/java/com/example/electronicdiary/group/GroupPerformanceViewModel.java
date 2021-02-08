package com.example.electronicdiary.group;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GroupPerformanceViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public GroupPerformanceViewModel(String group, String subject) {
        mText = new MutableLiveData<>();
        mText.setValue("This is groupPerformance fragment: " + group + " with " + subject);
    }

    public LiveData<String> getText() {
        return mText;
    }
}