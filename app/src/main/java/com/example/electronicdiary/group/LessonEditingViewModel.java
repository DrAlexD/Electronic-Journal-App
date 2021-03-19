package com.example.electronicdiary.group;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;

import java.util.Date;

public class LessonEditingViewModel extends ViewModel {
    private final MutableLiveData<LessonFormState> lessonFormState = new MutableLiveData<>();
    private final MutableLiveData<Date> lesson = new MutableLiveData<>();

    LiveData<LessonFormState> getLessonFormState() {
        return lessonFormState;
    }

    public MutableLiveData<Date> getLesson() {
        return lesson;
    }

    public void lessonEditingDataChanged(String lessonAttendPoints) {
        lessonFormState.setValue(new LessonFormState(lessonAttendPoints));
    }

    public void downloadLessonById(int lessonId) {
        this.lesson.setValue(Repository.getInstance().getLessonById(lessonId));
    }

    public void editLesson(String lessonAttendPoints) {
        Repository.getInstance().editLesson(lessonAttendPoints);
    }

    public void deleteLesson(int lessonId) {
        Repository.getInstance().deleteLesson(lessonId);
    }
}