package com.example.electronicdiary.group.actions.lesson;

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

    public void lessonAddingDataChanged(String dateAndTime, String pointsPerVisit) {
        lessonFormState.setValue(new LessonFormState(dateAndTime, pointsPerVisit));
    }

    public void addLesson(int moduleNumber, long groupId, long subjectId, long lecturerId, long seminarianId, long semesterId,
                          Date dateAndTime, boolean isLecture, int pointsPerVisit) {
        Repository.getInstance().addLesson(moduleNumber, groupId, subjectId, lecturerId, seminarianId, semesterId,
                dateAndTime, isLecture, pointsPerVisit);
    }
}