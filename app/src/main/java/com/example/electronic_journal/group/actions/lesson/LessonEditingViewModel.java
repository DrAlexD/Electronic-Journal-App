package com.example.electronic_journal.group.actions.lesson;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronic_journal.Repository;
import com.example.electronic_journal.data_classes.Lesson;
import com.example.electronic_journal.data_classes.Module;
import com.example.electronic_journal.data_classes.Semester;

import java.util.Date;

public class LessonEditingViewModel extends ViewModel {
    private final MutableLiveData<LessonFormState> lessonFormState = new MutableLiveData<>();
    private final MutableLiveData<Lesson> lesson = new MutableLiveData<>();
    private final MutableLiveData<Boolean> answer = new MutableLiveData<>();

    public LiveData<Boolean> getAnswer() {
        return answer;
    }

    LiveData<LessonFormState> getLessonFormState() {
        return lessonFormState;
    }

    public LiveData<Lesson> getLesson() {
        return lesson;
    }

    public void lessonEditingDataChanged(String dateAndTime, String pointsPerVisit, Semester semester) {
        lessonFormState.setValue(new LessonFormState(dateAndTime, pointsPerVisit, semester));
    }

    public void downloadLessonById(long lessonId) {
        Repository.getInstance().getLessonById(lessonId, lesson);
    }

    public void editLesson(long lessonId, Module module, Date dateAndTime, boolean isLecture, int pointsPerVisit) {
        Repository.getInstance().editLesson(lessonId, new Lesson(module, dateAndTime, isLecture, pointsPerVisit), answer);
    }

    public void deleteLesson(long lessonId) {
        Repository.getInstance().deleteLesson(lessonId, answer);
    }
}