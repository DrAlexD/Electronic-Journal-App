package com.example.electronic_journal.group.actions.student_performance;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronic_journal.Repository;
import com.example.electronic_journal.data_classes.Student;
import com.example.electronic_journal.data_classes.StudentPerformanceInSubject;
import com.example.electronic_journal.data_classes.SubjectInfo;

public class StudentPerformanceInSubjectViewModel extends ViewModel {
    private final MutableLiveData<StudentPerformanceInSubject> studentPerformanceInSubject = new MutableLiveData<>();
    private final MutableLiveData<Boolean> answer = new MutableLiveData<>();

    public LiveData<Boolean> getAnswer() {
        return answer;
    }

    public LiveData<StudentPerformanceInSubject> getStudentPerformanceInSubject() {
        return studentPerformanceInSubject;
    }

    public void downloadStudentPerformanceInSubject(long studentPerformanceInSubjectId) {
        Repository.getInstance().getStudentPerformanceInSubjectById(studentPerformanceInSubjectId, studentPerformanceInSubject);
    }

    public void editStudentPerformance(long studentPerformanceInSubjectId, SubjectInfo subjectInfo, Student student, Integer earnedPoints, Integer bonusPoints,
                                       Boolean isHaveCreditOrAdmission) {
        Repository.getInstance().editStudentPerformanceInSubject(studentPerformanceInSubjectId, new StudentPerformanceInSubject(subjectInfo, student,
                earnedPoints, bonusPoints, isHaveCreditOrAdmission, null, null), answer);
    }

    public void editStudentPerformance(long studentPerformanceInSubjectId, SubjectInfo subjectInfo, Student student, Integer earnedPoints, Integer bonusPoints,
                                       Boolean isHaveCreditOrAdmission, Integer mark) {
        Repository.getInstance().editStudentPerformanceInSubject(studentPerformanceInSubjectId, new StudentPerformanceInSubject(subjectInfo, student,
                earnedPoints, bonusPoints, isHaveCreditOrAdmission, null, mark), answer);
    }

    public void editStudentPerformance(long studentPerformanceInSubjectId, SubjectInfo subjectInfo, Student student, Integer earnedPoints, Integer bonusPoints,
                                       Boolean isHaveCreditOrAdmission, Integer earnedExamPoints, Integer mark) {
        Repository.getInstance().editStudentPerformanceInSubject(studentPerformanceInSubjectId, new StudentPerformanceInSubject(subjectInfo, student,
                earnedPoints, bonusPoints, isHaveCreditOrAdmission, earnedExamPoints, mark), answer);
    }
}