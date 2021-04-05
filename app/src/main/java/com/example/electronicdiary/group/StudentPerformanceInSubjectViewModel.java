package com.example.electronicdiary.group;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;
import com.example.electronicdiary.StudentPerformanceInSubject;
import com.example.electronicdiary.SubjectInfo;

public class StudentPerformanceInSubjectViewModel extends ViewModel {
    private final MutableLiveData<StudentPerformanceInSubjectFormState> studentPerformanceInSubjectFormState = new MutableLiveData<>();
    private final MutableLiveData<StudentPerformanceInSubject> studentPerformanceInSubject = new MutableLiveData<>();
    private final MutableLiveData<SubjectInfo> subjectInfo = new MutableLiveData<>();

    public LiveData<SubjectInfo> getSubjectInfo() {
        return subjectInfo;
    }

    LiveData<StudentPerformanceInSubjectFormState> getStudentPerformanceInSubjectFormState() {
        return studentPerformanceInSubjectFormState;
    }

    public LiveData<StudentPerformanceInSubject> getStudentPerformanceInSubject() {
        return studentPerformanceInSubject;
    }

    public void studentPerformanceDataChanged(String earnedPoints, String bonusPoints) {
        studentPerformanceInSubjectFormState.setValue(new StudentPerformanceInSubjectFormState(earnedPoints, bonusPoints));
    }

    public void studentPerformanceDataChanged(String earnedPoints, String bonusPoints, String mark) {
        studentPerformanceInSubjectFormState.setValue(new StudentPerformanceInSubjectFormState(earnedPoints, bonusPoints, mark));
    }

    public void studentPerformanceDataChanged(String earnedPoints, String bonusPoints, String earnedExamPoints, String mark) {
        studentPerformanceInSubjectFormState.setValue(new StudentPerformanceInSubjectFormState(earnedPoints, bonusPoints,
                earnedExamPoints, mark));
    }

    public void downloadSubjectInfo(int groupId, int subjectId, int lecturerId, int seminarianId, int semesterId) {
        this.subjectInfo.setValue(Repository.getInstance().getSubjectInfo(groupId, subjectId, lecturerId, seminarianId, semesterId));
    }

    public void downloadStudentPerformanceInSubject(int studentId, int groupId, int subjectId, int lecturerId, int seminarianId, int semesterId) {
        this.studentPerformanceInSubject.setValue(Repository.getInstance().getStudentPerformanceInSubject(studentId, groupId, subjectId, lecturerId, seminarianId, semesterId));
    }

    public void editStudentPerformance(int studentId, int groupId, int subjectId, int lecturerId, int seminarianId, int semesterId, int earnedPoints, int bonusPoints,
                                       boolean isHaveCreditOrAdmission) {
        Repository.getInstance().editStudentPerformanceInSubject(studentId, groupId, subjectId, lecturerId, seminarianId, semesterId,
                earnedPoints, bonusPoints, isHaveCreditOrAdmission);
    }

    public void editStudentPerformance(int studentId, int groupId, int subjectId, int lecturerId, int seminarianId, int semesterId, int earnedPoints, int bonusPoints,
                                       boolean isHaveCreditOrAdmission, int mark) {
        Repository.getInstance().editStudentPerformanceInSubject(studentId, groupId, subjectId, lecturerId, seminarianId, semesterId,
                earnedPoints, bonusPoints, isHaveCreditOrAdmission);
    }

    public void editStudentPerformance(int studentId, int groupId, int subjectId, int lecturerId, int seminarianId, int semesterId, int earnedPoints, int bonusPoints,
                                       boolean isHaveCreditOrAdmission, int earnedExamPoints, int mark) {
        Repository.getInstance().editStudentPerformanceInSubject(studentId, groupId, subjectId, lecturerId, seminarianId, semesterId,
                earnedPoints, bonusPoints, isHaveCreditOrAdmission, earnedExamPoints, mark);
    }
}