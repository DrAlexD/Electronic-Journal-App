package com.example.electronicdiary.group.actions.student_performance;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;
import com.example.electronicdiary.data_classes.Student;
import com.example.electronicdiary.data_classes.StudentPerformanceInSubject;
import com.example.electronicdiary.data_classes.SubjectInfo;

public class StudentPerformanceInSubjectViewModel extends ViewModel {
    private final MutableLiveData<StudentPerformanceInSubject> studentPerformanceInSubject = new MutableLiveData<>();

    public LiveData<StudentPerformanceInSubject> getStudentPerformanceInSubject() {
        return studentPerformanceInSubject;
    }

    public void downloadStudentPerformanceInSubject(long studentPerformanceInSubjectId) {
        Repository.getInstance().getStudentPerformanceInSubjectById(studentPerformanceInSubjectId, studentPerformanceInSubject);
    }

    public void editStudentPerformance(long studentPerformanceInSubjectId, SubjectInfo subjectInfo, Student student, int earnedPoints, int bonusPoints,
                                       boolean isHaveCreditOrAdmission) {
        Repository.getInstance().editStudentPerformanceInSubject(studentPerformanceInSubjectId, new StudentPerformanceInSubject(subjectInfo, student,
                earnedPoints, bonusPoints, isHaveCreditOrAdmission, null, null));
    }

    public void editStudentPerformance(long studentPerformanceInSubjectId, SubjectInfo subjectInfo, Student student, int earnedPoints, int bonusPoints,
                                       boolean isHaveCreditOrAdmission, int mark) {
        Repository.getInstance().editStudentPerformanceInSubject(studentPerformanceInSubjectId, new StudentPerformanceInSubject(subjectInfo, student,
                earnedPoints, bonusPoints, isHaveCreditOrAdmission, null, mark));
    }

    public void editStudentPerformance(long studentPerformanceInSubjectId, SubjectInfo subjectInfo, Student student, int earnedPoints, int bonusPoints,
                                       boolean isHaveCreditOrAdmission, int earnedExamPoints, int mark) {
        Repository.getInstance().editStudentPerformanceInSubject(studentPerformanceInSubjectId, new StudentPerformanceInSubject(subjectInfo, student,
                earnedPoints, bonusPoints, isHaveCreditOrAdmission, earnedExamPoints, mark));
    }
}