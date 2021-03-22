package com.example.electronicdiary.group;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;

import java.util.Date;

public class LessonAddingViewModel extends ViewModel {
    private final MutableLiveData<LessonFormState> lessonFormState = new MutableLiveData<>();

    LiveData<LessonFormState> getLessonFormState() {
        return lessonFormState;
    }

    public void lessonAddingDataChanged(String lessonDate, String lessonAttendPoints) {
        lessonFormState.setValue(new LessonFormState(lessonDate, lessonAttendPoints));
    }

    public void addLesson(int moduleNumber, int groupId, int subjectId, int lecturerId, int seminarianId, int semesterId,
                          Date dateAndTime, boolean isLecture, int pointsPerVisit) {
        Repository.getInstance().addLesson(moduleNumber, groupId, subjectId, lecturerId, seminarianId, semesterId,
                dateAndTime, isLecture, pointsPerVisit);
    }
}