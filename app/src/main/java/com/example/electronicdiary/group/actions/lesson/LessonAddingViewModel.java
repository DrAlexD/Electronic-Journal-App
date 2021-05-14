package com.example.electronicdiary.group.actions.lesson;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;
import com.example.electronicdiary.data_classes.Lesson;
import com.example.electronicdiary.data_classes.Module;

import java.util.Date;

public class LessonAddingViewModel extends ViewModel {
    private final MutableLiveData<LessonFormState> lessonFormState = new MutableLiveData<>();
    private final MutableLiveData<Module> module = new MutableLiveData<>();

    LiveData<LessonFormState> getLessonFormState() {
        return lessonFormState;
    }

    public void lessonAddingDataChanged(String dateAndTime, String pointsPerVisit) {
        lessonFormState.setValue(new LessonFormState(dateAndTime, pointsPerVisit));
    }

    public LiveData<Module> getModule() {
        return module;
    }

    public void downloadModuleById(long moduleId) {
        Repository.getInstance().getModuleById(moduleId, module);
    }

    public void addLesson(Module module, Date dateAndTime, boolean isLecture, int pointsPerVisit) {
        Repository.getInstance().addLesson(new Lesson(module, dateAndTime, isLecture, pointsPerVisit));
    }
}