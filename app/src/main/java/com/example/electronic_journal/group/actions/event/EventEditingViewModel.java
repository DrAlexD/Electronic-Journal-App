package com.example.electronic_journal.group.actions.event;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronic_journal.Repository;
import com.example.electronic_journal.data_classes.Event;
import com.example.electronic_journal.data_classes.Module;
import com.example.electronic_journal.data_classes.Semester;

import java.util.Date;

public class EventEditingViewModel extends ViewModel {
    private final MutableLiveData<EventFormState> eventFormState = new MutableLiveData<>();
    private final MutableLiveData<Event> event = new MutableLiveData<>();
    private final MutableLiveData<Boolean> secondAnswer = new MutableLiveData<>();
    private MutableLiveData<Integer> answer = new MutableLiveData<>();

    public LiveData<Integer> getAnswer() {
        return answer;
    }

    public void setAnswer(MutableLiveData<Integer> answer) {
        this.answer = answer;
    }

    public LiveData<Boolean> getSecondAnswer() {
        return secondAnswer;
    }

    LiveData<EventFormState> getEventFormState() {
        return eventFormState;
    }

    public LiveData<Event> getEvent() {
        return event;
    }

    public void eventEditingDataChanged(String startDate, String deadlineDate, String minPoints, String maxPoints, String numberOfVariants, Semester semester) {
        eventFormState.setValue(new EventFormState(startDate, deadlineDate, minPoints, maxPoints, semester));
    }

    public void downloadEventById(long eventId) {
        Repository.getInstance().getEventById(eventId, event);
    }

    public void editEvent(long eventId, Module module, int type, int number, Date startDate, Date deadlineDate, int minPoints, int maxPoints, Integer numberOfVariants) {
        Repository.getInstance().editEvent(eventId, new Event(module, type, number, startDate, deadlineDate, minPoints, maxPoints, numberOfVariants), answer);
    }

    public void deleteEvent(long eventId) {
        Repository.getInstance().deleteEvent(eventId, secondAnswer);
    }
}