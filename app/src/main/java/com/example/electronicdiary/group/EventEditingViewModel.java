package com.example.electronicdiary.group;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;

public class EventEditingViewModel extends ViewModel {
    private final MutableLiveData<EventFormState> eventFormState = new MutableLiveData<>();
    private final MutableLiveData<String> event = new MutableLiveData<>();

    LiveData<EventFormState> getEventFormState() {
        return eventFormState;
    }

    public MutableLiveData<String> getEvent() {
        return event;
    }

    public void eventEditingDataChanged(String eventMinPoints) {
        eventFormState.setValue(new EventFormState(eventMinPoints));
    }

    public void downloadEventById(int eventId) {
        this.event.setValue(Repository.getInstance().getEventById(eventId));
    }

    public void editEvent(String eventMinPoints) {
        Repository.getInstance().editEvent(eventMinPoints);
    }

    public void deleteEvent(int eventId) {
        Repository.getInstance().deleteEvent(eventId);
    }
}