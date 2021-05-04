package com.example.electronicdiary.group.actions.event;

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

    public void eventAddingDataChanged(String startDate, String deadlineDate, String minPoints, String maxPoints) {
        eventFormState.setValue(new EventFormState(startDate, deadlineDate, minPoints, maxPoints));
    }

    public void addEvent(int moduleNumber, long groupId, long subjectId, long lecturerId, long seminarianId, long semesterId,
                         String type, Date startDate, Date deadlineDate, int minPoints, int maxPoints) {
        Repository.getInstance().addEvent(moduleNumber, groupId, subjectId, lecturerId, seminarianId, semesterId,
                type, startDate, deadlineDate, minPoints, maxPoints);
    }
}