package com.example.electronicdiary.group;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;

public class EventAddingViewModel extends ViewModel {
    private final MutableLiveData<EventFormState> eventFormState = new MutableLiveData<>();

    LiveData<EventFormState> getEventFormState() {
        return eventFormState;
    }

    public void eventAddingDataChanged(String eventMinPoints) {
        eventFormState.setValue(new EventFormState(eventMinPoints));
    }

    public void addEvent(String eventMinPoints, int eventType) {
        Repository.getInstance().addEvent(eventMinPoints, eventType);
    }
}