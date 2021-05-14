package com.example.electronicdiary.group.actions.event;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;
import com.example.electronicdiary.data_classes.Event;
import com.example.electronicdiary.data_classes.Module;

import java.util.Date;
import java.util.Map;

public class EventAddingViewModel extends ViewModel {
    private final MutableLiveData<EventFormState> eventFormState = new MutableLiveData<>();
    private final MutableLiveData<Map<String, Module>> modules = new MutableLiveData<>();
    private final MutableLiveData<Integer> lastNumberOfEventType = new MutableLiveData<>();

    LiveData<EventFormState> getEventFormState() {
        return eventFormState;
    }

    public LiveData<Map<String, Module>> getModules() {
        return modules;
    }

    public LiveData<Integer> getLastNumberOfEventType() {
        return lastNumberOfEventType;
    }

    public void eventAddingDataChanged(String startDate, String deadlineDate, String minPoints, String maxPoints) {
        eventFormState.setValue(new EventFormState(startDate, deadlineDate, minPoints, maxPoints));
    }

    public void downloadModules(long subjectInfoId) {
        Repository.getInstance().getModules(subjectInfoId, modules);
    }

    public void downloadLastNumberOfEventType(long subjectInfoId, int type) {
        Repository.getInstance().getLastNumberOfEventType(subjectInfoId, type, lastNumberOfEventType);
    }

    public void addEvent(Module module, int type, int number, Date startDate, Date deadlineDate, int minPoints, int maxPoints) {
        Repository.getInstance().addEvent(new Event(module, type, number, startDate, deadlineDate, minPoints, maxPoints));
    }
}