package com.example.electronicdiary.student;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EventsOrVisitsViewModel extends ViewModel {
    private final MutableLiveData<Integer> position = new MutableLiveData<>();

    public MutableLiveData<Integer> getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position.setValue(position);
    }
}
