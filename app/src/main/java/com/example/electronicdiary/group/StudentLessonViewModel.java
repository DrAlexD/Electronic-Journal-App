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

    public void downloadStudentLessonById(int lessonId, int studentId) {
        this.lesson.setValue(Repository.getInstance().getStudentLessonById(lessonId, studentId));
    }

    public void addStudentLesson(int lessonId, int moduleNumber, int studentId, int groupId, int subjectId, int lecturerId,
                                 int seminarianId, int semesterId, boolean isAttended) {
        Repository.getInstance().addStudentLesson(lessonId, moduleNumber, studentId, groupId, subjectId, lecturerId,
                seminarianId, semesterId, isAttended);
    }

    public void editStudentLesson(int lessonId, int moduleNumber, int studentId, int groupId, int subjectId, int lecturerId,
                                  int seminarianId, int semesterId, boolean isAttended, int bonusPoints) {
        Repository.getInstance().editStudentLesson(lessonId, moduleNumber, studentId, groupId, subjectId, lecturerId,
                seminarianId, semesterId, isAttended, bonusPoints);
    }

    public void deleteStudentLesson(int lessonId, int studentId) {
        Repository.getInstance().deleteStudentLesson(lessonId, studentId);
    }
}