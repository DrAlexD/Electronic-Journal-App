package com.example.electronicdiary.group.actions.event;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;
import com.example.electronicdiary.data_classes.Event;
import com.example.electronicdiary.data_classes.Module;

import java.util.Date;

public class EventEditingViewModel extends ViewModel {
    private final MutableLiveData<EventFormState> eventFormState = new MutableLiveData<>();
    private final MutableLiveData<Event> event = new MutableLiveData<>();

    LiveData<EventFormState> getEventFormState() {
        return eventFormState;
    }

    public LiveData<Event> getEvent() {
        return event;
    }

    public void eventEditingDataChanged(String startDate, String deadlineDate, String minPoints, String maxPoints) {
        eventFormState.setValue(new EventFormState(startDate, deadlineDate, minPoints, maxPoints));
    }

    public void downloadEventById(long eventId) {
        Repository.getInstance().getEventById(eventId, event);
    }

    public void editEvent(long eventId, Module module, int type, int number, Date startDate, Date deadlineDate, int minPoints, int maxPoints) {
        Repository.getInstance().editEvent(eventId, new Event(module, type, number, startDate, deadlineDate, minPoints, maxPoints));
    }

    public void deleteEvent(long eventId) {
        Repository.getInstance().deleteEvent(eventId);
    }
}