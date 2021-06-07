package com.example.electronic_journal.group.actions.student_event;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronic_journal.Repository;
import com.example.electronic_journal.data_classes.Event;
import com.example.electronic_journal.data_classes.Semester;
import com.example.electronic_journal.data_classes.StudentEvent;
import com.example.electronic_journal.data_classes.StudentPerformanceInModule;

import java.util.Date;
import java.util.Map;

public class StudentEventViewModel extends ViewModel {
    private final MutableLiveData<StudentEventFormState> studentEventFormState = new MutableLiveData<>();
    private final MutableLiveData<StudentEvent> studentEvent = new MutableLiveData<>();
    private final MutableLiveData<Event> event = new MutableLiveData<>();
    private final MutableLiveData<Map<String, StudentPerformanceInModule>> studentPerformanceInModules = new MutableLiveData<>();
    private final MutableLiveData<Boolean> answer = new MutableLiveData<>();

    public LiveData<Boolean> getAnswer() {
        return answer;
    }

    LiveData<StudentEventFormState> getStudentEventFormState() {
        return studentEventFormState;
    }

    public LiveData<StudentEvent> getStudentEvent() {
        return studentEvent;
    }

    public LiveData<Event> getEvent() {
        return event;
    }

    public LiveData<Map<String, StudentPerformanceInModule>> getStudentPerformanceInModules() {
        return studentPerformanceInModules;
    }

    public void eventPerformanceDataChanged(String earnedPoints, Integer eventMaxPoints, String finishDate, Semester semester, Date startDate) {
        studentEventFormState.setValue(new StudentEventFormState(earnedPoints, eventMaxPoints, finishDate, semester, startDate));
    }

    public void downloadEventById(long eventId) {
        Repository.getInstance().getEventById(eventId, event);
    }

    public void downloadStudentPerformanceInModule(long studentPerformanceInSubjectId) {
        Repository.getInstance().getStudentPerformanceInModules(studentPerformanceInSubjectId, studentPerformanceInModules);
    }

    public void downloadStudentEventById(long studentEventId) {
        Repository.getInstance().getStudentEventById(studentEventId, studentEvent);
    }

    public void addStudentEvent(int attemptNumber, StudentPerformanceInModule studentPerformanceInModule, Event event, boolean isAttended) {
        Repository.getInstance().addStudentEvent(new StudentEvent(attemptNumber, studentPerformanceInModule, event, isAttended), answer);
    }

    public void editStudentEvent(long studentEventId, int attemptNumber, StudentPerformanceInModule studentPerformanceInModule, Event event, boolean isAttended, Integer variantNumber,
                                 Date finishDate, Integer earnedPoints, Integer bonusPoints, Boolean isHaveCredit) {
        Repository.getInstance().editStudentEvent(studentEventId, new StudentEvent(attemptNumber, studentPerformanceInModule, event, isAttended, variantNumber,
                finishDate, earnedPoints, bonusPoints, isHaveCredit), answer);
    }

    public void deleteStudentEvent(long studentEventId) {
        Repository.getInstance().deleteStudentEvent(studentEventId, answer);
    }
}