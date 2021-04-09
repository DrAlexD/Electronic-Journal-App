package com.example.electronicdiary.group.actions.lesson;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;
import com.example.electronicdiary.data_classes.Lesson;

import java.util.Date;

public class LessonEditingViewModel extends ViewModel {
    private final MutableLiveData<LessonFormState> lessonFormState = new MutableLiveData<>();
    private final MutableLiveData<Lesson> lesson = new MutableLiveData<>();

    LiveData<LessonFormState> getLessonFormState() {
        return lessonFormState;
    }

    public MutableLiveData<Lesson> getLesson() {
        return lesson;
    }

    public void lessonEditingDataChanged(String dateAndTime, String pointsPerVisit) {
        lessonFormState.setValue(new LessonFormState(dateAndTime, pointsPerVisit));
    }

    public void downloadLessonById(int lessonId) {
        this.lesson.setValue(Repository.getInstance().getLessonById(lessonId));
    }

    public void editLesson(int lessonId, Date dateAndTime, boolean isLecture, int pointsPerVisit) {
        Repository.getInstance().editLesson(lessonId, dateAndTime, isLecture, pointsPerVisit);
    }

    public void deleteLesson(int lessonId) {
        Repository.getInstance().deleteLesson(lessonId);
    }
}