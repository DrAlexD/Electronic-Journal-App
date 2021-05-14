package com.example.electronicdiary.group.actions.lesson;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;
import com.example.electronicdiary.data_classes.Lesson;
import com.example.electronicdiary.data_classes.Module;

import java.util.Date;

public class LessonEditingViewModel extends ViewModel {
    private final MutableLiveData<LessonFormState> lessonFormState = new MutableLiveData<>();
    private final MutableLiveData<Lesson> lesson = new MutableLiveData<>();

    LiveData<LessonFormState> getLessonFormState() {
        return lessonFormState;
    }

    public LiveData<Lesson> getLesson() {
        return lesson;
    }

    public void lessonEditingDataChanged(String dateAndTime, String pointsPerVisit) {
        lessonFormState.setValue(new LessonFormState(dateAndTime, pointsPerVisit));
    }

    public void downloadLessonById(long lessonId) {
        Repository.getInstance().getLessonById(lessonId, lesson);
    }

    public void editLesson(long lessonId, Module module, Date dateAndTime, boolean isLecture, int pointsPerVisit) {
        Repository.getInstance().editLesson(lessonId, new Lesson(module, dateAndTime, isLecture, pointsPerVisit));
    }

    public void deleteLesson(long lessonId) {
        Repository.getInstance().deleteLesson(lessonId);
    }
}