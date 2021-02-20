package com.example.electronicdiary.student;

import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class EventInfoViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<String>> values = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<View>> changedOptions = new MutableLiveData<>();

    public MutableLiveData<ArrayList<String>> getValues() {
        return values;
    }

    public void setValues(ArrayList<String> values) {
        this.values.setValue(values);
    }

    public MutableLiveData<ArrayList<View>> getChangedOptions() {
        return changedOptions;
    }

    public void setChangedOptions(ArrayList<View> changedOptions) {
        this.changedOptions.setValue(changedOptions);
    }
}
