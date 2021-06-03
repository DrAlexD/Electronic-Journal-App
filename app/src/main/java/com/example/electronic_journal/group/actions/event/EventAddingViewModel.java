package com.example.electronic_journal.group.actions.event;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronic_journal.Repository;
import com.example.electronic_journal.data_classes.Event;
import com.example.electronic_journal.data_classes.Module;
import com.example.electronic_journal.data_classes.Semester;

import java.util.Date;
import java.util.Map;

public class EventAddingViewModel extends ViewModel {
    private final MutableLiveData<EventFormState> eventFormState = new MutableLiveData<>();
    private final MutableLiveData<Map<String, Module>> modules = new MutableLiveData<>();
    private final MutableLiveData<Integer> lastNumberOfEventType = new MutableLiveData<>();
    private MutableLiveData<Integer> answer = new MutableLiveData<>();

    public LiveData<Integer> getAnswer() {
        return answer;
    }

    public void setAnswer(MutableLiveData<Integer> answer) {
        this.answer = answer;
    }

    LiveData<EventFormState> getEventFormState() {
        return eventFormState;
    }

    public LiveData<Map<String, Module>> getModules() {
        return modules;
    }

    public LiveData<Integer> getLastNumberOfEventType() {
        return lastNumberOfEventType;
    }

    public void eventAddingDataChanged(String startDate, String deadlineDate, String minPoints, String maxPoints, String numberOfVariants, Semester semester) {
        eventFormState.setValue(new EventFormState(startDate, deadlineDate, minPoints, maxPoints, numberOfVariants, semester));
    }

    public void downloadModules(long subjectInfoId) {
        Repository.getInstance().getModules(subjectInfoId, modules);
    }

    public void downloadLastNumberOfEventType(long subjectInfoId, int type) {
        Repository.getInstance().getLastNumberOfEventType(subjectInfoId, type, lastNumberOfEventType);
    }

    public void addEvent(Module module, int type, int number, Date startDate, Date deadlineDate, int minPoints, int maxPoints, int numberOfVariants) {
        Repository.getInstance().addEvent(new Event(module, type, number, startDate, deadlineDate, minPoints, maxPoints, numberOfVariants), answer);
    }
}