package com.example.electronicdiary.group;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;
import com.example.electronicdiary.StudentEvent;

public class StudentEventViewModel extends ViewModel {
    private final MutableLiveData<StudentEventFormState> studentEventFormState = new MutableLiveData<>();
    private final MutableLiveData<StudentEvent> event = new MutableLiveData<>();

    LiveData<StudentEventFormState> getStudentEventFormState() {
        return studentEventFormState;
    }

    public MutableLiveData<StudentEvent> getEvent() {
        return event;
    }

    public void eventPerformanceDataChanged(String eventEarnedPoints) {
        studentEventFormState.setValue(new StudentEventFormState(eventEarnedPoints));
    }

    public void downloadStudentEventById(int eventId) {
        this.event.setValue(Repository.getInstance().getStudentEventById(eventId));
    }

    public void addStudentEvent(String eventEarnedPoints) {
        Repository.getInstance().addStudentEvent(eventEarnedPoints);
    }

    public void editStudentEvent(String eventEarnedPoints) {
        Repository.getInstance().editStudentEvent(eventEarnedPoints);
    }

    public void deleteStudentEvent(int eventId) {
        Repository.getInstance().deleteStudentEvent(eventId);
    }
}