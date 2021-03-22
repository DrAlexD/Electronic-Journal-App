package com.example.electronicdiary.group;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Event;
import com.example.electronicdiary.Repository;

import java.util.Date;

public class EventEditingViewModel extends ViewModel {
    private final MutableLiveData<EventFormState> eventFormState = new MutableLiveData<>();
    private final MutableLiveData<Event> event = new MutableLiveData<>();

    LiveData<EventFormState> getEventFormState() {
        return eventFormState;
    }

    public MutableLiveData<Event> getEvent() {
        return event;
    }

    public void eventEditingDataChanged(String eventMinPoints) {
        eventFormState.setValue(new EventFormState(eventMinPoints));
    }

    public void downloadEventById(int eventId) {
        this.event.setValue(Repository.getInstance().getEventById(eventId));
    }

    public void editEvent(int id, int moduleNumber, int groupId, int subjectId, int lecturerId, int seminarianId,
                          int semesterId, String type, int number, Date startDate, Date deadlineDate, int minPoints, int maxPoints) {
        Repository.getInstance().editEvent(id, moduleNumber, groupId, subjectId, lecturerId, seminarianId,
                semesterId, type, number, startDate, deadlineDate, minPoints, maxPoints);
    }

    public void deleteEvent(int eventId) {
        Repository.getInstance().deleteEvent(eventId);
    }
}