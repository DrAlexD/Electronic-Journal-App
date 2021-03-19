package com.example.electronicdiary.group;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;
import com.example.electronicdiary.StudentLesson;

public class StudentLessonViewModel extends ViewModel {
    private final MutableLiveData<StudentLessonFormState> studentLessonFormState = new MutableLiveData<>();
    private final MutableLiveData<StudentLesson> lesson = new MutableLiveData<>();

    LiveData<StudentLessonFormState> getStudentLessonFormState() {
        return studentLessonFormState;
    }

    public MutableLiveData<StudentLesson> getLesson() {
        return lesson;
    }

    public void lessonPerformanceDataChanged(String lessonEarnedPoints) {
        studentLessonFormState.setValue(new StudentLessonFormState(lessonEarnedPoints));
    }

    public void downloadStudentLessonById(int lessonId) {
        this.lesson.setValue(Repository.getInstance().getStudentLessonById(lessonId));
    }

    public void addStudentLesson(String lessonEarnedPoints) {
        Repository.getInstance().addStudentLesson(lessonEarnedPoints);
    }

    public void editStudentLesson(String lessonEarnedPoints) {
        Repository.getInstance().editStudentLesson(lessonEarnedPoints);
    }

    public void deleteStudentLesson(int lessonId) {
        Repository.getInstance().deleteStudentLesson(lessonId);
    }
}