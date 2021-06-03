package com.example.electronic_journal.group.actions.student_lesson;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronic_journal.Repository;
import com.example.electronic_journal.data_classes.Lesson;
import com.example.electronic_journal.data_classes.StudentLesson;
import com.example.electronic_journal.data_classes.StudentPerformanceInModule;

import java.util.Map;

public class StudentLessonViewModel extends ViewModel {
    private final MutableLiveData<StudentLesson> studentLesson = new MutableLiveData<>();
    private final MutableLiveData<Lesson> lesson = new MutableLiveData<>();
    private final MutableLiveData<Map<String, StudentPerformanceInModule>> studentPerformanceInModules = new MutableLiveData<>();
    private final MutableLiveData<Boolean> answer = new MutableLiveData<>();

    public LiveData<Boolean> getAnswer() {
        return answer;
    }

    public LiveData<StudentLesson> getStudentLesson() {
        return studentLesson;
    }

    public LiveData<Lesson> getLesson() {
        return lesson;
    }

    public LiveData<Map<String, StudentPerformanceInModule>> getStudentPerformanceInModules() {
        return studentPerformanceInModules;
    }

    public void downloadStudentLessonById(long studentLessonId) {
        Repository.getInstance().getStudentLessonById(studentLessonId, studentLesson);
    }

    public void downloadLessonById(long lessonId) {
        Repository.getInstance().getLessonById(lessonId, lesson);
    }

    public void downloadStudentPerformanceInModules(long studentPerformanceInSubjectId) {
        Repository.getInstance().getStudentPerformanceInModules(studentPerformanceInSubjectId, studentPerformanceInModules);
    }

    public void addStudentLesson(StudentPerformanceInModule studentPerformanceInModule, Lesson lesson, boolean isAttended) {
        Repository.getInstance().addStudentLesson(new StudentLesson(studentPerformanceInModule, lesson, isAttended), answer);
    }

    public void editStudentLesson(long studentLessonId, StudentPerformanceInModule studentPerformanceInModule, Lesson lesson, boolean isAttended, Integer bonusPoints) {
        Repository.getInstance().editStudentLesson(studentLessonId, new StudentLesson(studentPerformanceInModule, lesson, isAttended, bonusPoints), answer);
    }

    public void deleteStudentLesson(long studentLessonId) {
        Repository.getInstance().deleteStudentLesson(studentLessonId, answer);
    }
}