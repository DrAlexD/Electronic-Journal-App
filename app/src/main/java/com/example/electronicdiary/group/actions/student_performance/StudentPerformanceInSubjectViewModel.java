package com.example.electronicdiary.group.actions.student_performance;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;
import com.example.electronicdiary.data_classes.StudentPerformanceInSubject;
import com.example.electronicdiary.data_classes.SubjectInfo;

public class StudentPerformanceInSubjectViewModel extends ViewModel {
    private final MutableLiveData<StudentPerformanceInSubject> studentPerformanceInSubject = new MutableLiveData<>();
    private final MutableLiveData<SubjectInfo> subjectInfo = new MutableLiveData<>();

    public LiveData<SubjectInfo> getSubjectInfo() {
        return subjectInfo;
    }

    public LiveData<StudentPerformanceInSubject> getStudentPerformanceInSubject() {
        return studentPerformanceInSubject;
    }

    public void downloadSubjectInfo(long groupId, long subjectId, long lecturerId, long seminarianId, long semesterId) {
        this.subjectInfo.setValue(Repository.getInstance().getSubjectInfo(groupId, subjectId, lecturerId, seminarianId, semesterId));
    }

    public void downloadStudentPerformanceInSubject(long studentId, long groupId, long subjectId, long lecturerId, long seminarianId, long semesterId) {
        this.studentPerformanceInSubject.setValue(Repository.getInstance().getStudentPerformanceInSubject(studentId, groupId, subjectId, lecturerId, seminarianId, semesterId));
    }

    public void editStudentPerformance(long studentId, long groupId, long subjectId, long lecturerId, long seminarianId, long semesterId, int earnedPoints, int bonusPoints,
                                       boolean isHaveCreditOrAdmission) {
        Repository.getInstance().editStudentPerformanceInSubject(studentId, groupId, subjectId, lecturerId, seminarianId, semesterId,
                earnedPoints, bonusPoints, isHaveCreditOrAdmission);
    }

    public void editStudentPerformance(long studentId, long groupId, long subjectId, long lecturerId, long seminarianId, long semesterId, int earnedPoints, int bonusPoints,
                                       boolean isHaveCreditOrAdmission, int mark) {
        Repository.getInstance().editStudentPerformanceInSubject(studentId, groupId, subjectId, lecturerId, seminarianId, semesterId,
                earnedPoints, bonusPoints, isHaveCreditOrAdmission);
    }

    public void editStudentPerformance(long studentId, long groupId, long subjectId, long lecturerId, long seminarianId, long semesterId, int earnedPoints, int bonusPoints,
                                       boolean isHaveCreditOrAdmission, int earnedExamPoints, int mark) {
        Repository.getInstance().editStudentPerformanceInSubject(studentId, groupId, subjectId, lecturerId, seminarianId, semesterId,
                earnedPoints, bonusPoints, isHaveCreditOrAdmission, earnedExamPoints, mark);
    }
}