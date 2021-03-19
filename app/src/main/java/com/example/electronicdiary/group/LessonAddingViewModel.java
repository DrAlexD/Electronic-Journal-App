package com.example.electronicdiary.group;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;

public class LessonAddingViewModel extends ViewModel {
    private final MutableLiveData<LessonFormState> lessonFormState = new MutableLiveData<>();

    LiveData<LessonFormState> getLessonFormState() {
        return lessonFormState;
    }

    public void lessonAddingDataChanged(String lessonDate, String lessonAttendPoints) {
        lessonFormState.setValue(new LessonFormState(lessonDate, lessonAttendPoints));
    }

    public void addLesson(String lessonDate, String lessonAttendPoints) {
        Repository.getInstance().addLesson(lessonDate, lessonAttendPoints);
    }
}