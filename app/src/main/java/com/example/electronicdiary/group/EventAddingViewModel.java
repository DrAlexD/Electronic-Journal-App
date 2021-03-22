package com.example.electronicdiary.group;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;

import java.util.Date;

public class EventAddingViewModel extends ViewModel {
    private final MutableLiveData<EventFormState> eventFormState = new MutableLiveData<>();

    LiveData<EventFormState> getEventFormState() {
        return eventFormState;
    }

    public void eventAddingDataChanged(String eventMinPoints) {
        eventFormState.setValue(new EventFormState(eventMinPoints));
    }

    public void addEvent(int moduleNumber, int groupId, int subjectId, int lecturerId, int seminarianId, int semesterId,
                         String type, int number, Date startDate, Date deadlineDate, int minPoints, int maxPoints) {
        Repository.getInstance().addEvent(moduleNumber, groupId, subjectId, lecturerId, seminarianId, semesterId,
                type, number, startDate, deadlineDate, minPoints, maxPoints);
    }
}