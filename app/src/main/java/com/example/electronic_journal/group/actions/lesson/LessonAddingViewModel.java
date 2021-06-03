package com.example.electronic_journal.group.actions.lesson;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronic_journal.Repository;
import com.example.electronic_journal.data_classes.Lesson;
import com.example.electronic_journal.data_classes.Module;
import com.example.electronic_journal.data_classes.Semester;

import java.util.Date;

public class LessonAddingViewModel extends ViewModel {
    private final MutableLiveData<LessonFormState> lessonFormState = new MutableLiveData<>();
    private final MutableLiveData<Module> module = new MutableLiveData<>();
    private final MutableLiveData<Boolean> answer = new MutableLiveData<>();

    public LiveData<Boolean> getAnswer() {
        return answer;
    }

    LiveData<LessonFormState> getLessonFormState() {
        return lessonFormState;
    }

    public void lessonAddingDataChanged(String dateAndTime, String pointsPerVisit, Semester semester) {
        lessonFormState.setValue(new LessonFormState(dateAndTime, pointsPerVisit, semester));
    }

    public LiveData<Module> getModule() {
        return module;
    }

    public void downloadModuleById(long moduleId) {
        Repository.getInstance().getModuleById(moduleId, module);
    }

    public void addLesson(Module module, Date dateAndTime, boolean isLecture, int pointsPerVisit) {
        Repository.getInstance().addLesson(new Lesson(module, dateAndTime, isLecture, pointsPerVisit), answer);
    }
}