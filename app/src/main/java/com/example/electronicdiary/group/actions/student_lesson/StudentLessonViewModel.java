package com.example.electronicdiary.group.actions.student_lesson;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;
import com.example.electronicdiary.data_classes.StudentLesson;

public class StudentLessonViewModel extends ViewModel {
    private final MutableLiveData<StudentLesson> studentLesson = new MutableLiveData<>();

    public MutableLiveData<StudentLesson> getStudentLesson() {
        return studentLesson;
    }

    public void downloadStudentLessonById(long lessonId, long studentId) {
        this.studentLesson.setValue(Repository.getInstance().getStudentLessonById(lessonId, studentId));
    }

    public void addStudentLesson(long lessonId, int moduleNumber, long studentId, long groupId, long subjectId, long lecturerId,
                                 long seminarianId, long semesterId, boolean isAttended) {
        Repository.getInstance().addStudentLesson(lessonId, moduleNumber, studentId, groupId, subjectId, lecturerId,
                seminarianId, semesterId, isAttended);
    }

    public void editStudentLesson(long lessonId, long studentId, boolean isAttended, int bonusPoints) {
        Repository.getInstance().editStudentLesson(lessonId, studentId, isAttended, bonusPoints);
    }

    public void deleteStudentLesson(long lessonId, long studentId) {
        Repository.getInstance().deleteStudentLesson(lessonId, studentId);
    }
}