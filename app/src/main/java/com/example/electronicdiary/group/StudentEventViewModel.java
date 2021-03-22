package com.example.electronicdiary.group;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;
import com.example.electronicdiary.StudentEvent;

import java.util.Date;

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

    public void downloadStudentEventById(int attemptNumber, int eventId, int studentId) {
        this.event.setValue(Repository.getInstance().getStudentEventById(attemptNumber, eventId, studentId));
    }

    public void addStudentEvent(int attemptNumber, int eventId, int moduleNumber, int studentId, int groupId, int subjectId,
                                int lecturerId, int seminarianId, int semesterId, boolean isAttended, int variantNumber) {
        Repository.getInstance().addStudentEvent(attemptNumber, eventId, moduleNumber, studentId, groupId, subjectId,
                lecturerId, seminarianId, semesterId, isAttended, variantNumber);
    }

    public void editStudentEvent(int attemptNumber, int eventId, int moduleNumber, int studentId, int groupId, int subjectId,
                                 int lecturerId, int seminarianId, int semesterId, boolean isAttended, int variantNumber,
                                 Date finishDate, int earnedPoints, int bonusPoints, boolean isHaveCredit) {
        Repository.getInstance().editStudentEvent(attemptNumber, eventId, moduleNumber, studentId, groupId, subjectId,
                lecturerId, seminarianId, semesterId, isAttended, variantNumber,
                finishDate, earnedPoints, bonusPoints, isHaveCredit);
    }

    public void deleteStudentEvent(int attemptNumber, int eventId, int studentId) {
        Repository.getInstance().deleteStudentEvent(attemptNumber, eventId, studentId);
    }
}