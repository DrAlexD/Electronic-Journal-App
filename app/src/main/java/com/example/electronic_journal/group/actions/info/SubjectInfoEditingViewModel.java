package com.example.electronic_journal.group.actions.info;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronic_journal.Repository;
import com.example.electronic_journal.data_classes.Group;
import com.example.electronic_journal.data_classes.Professor;
import com.example.electronic_journal.data_classes.Semester;
import com.example.electronic_journal.data_classes.Subject;
import com.example.electronic_journal.data_classes.SubjectInfo;

public class SubjectInfoEditingViewModel extends ViewModel {
    private final MutableLiveData<SubjectInfo> subjectInfo = new MutableLiveData<>();
    private final MutableLiveData<Boolean> answer = new MutableLiveData<>();

    public LiveData<Boolean> getAnswer() {
        return answer;
    }

    public LiveData<SubjectInfo> getSubjectInfo() {
        return subjectInfo;
    }

    public void downloadSubjectInfo(long subjectInfoId) {
        Repository.getInstance().getSubjectInfoById(subjectInfoId, subjectInfo);
    }

    public void editSubjectInfo(long subjectInfoId, Group group, Subject subject, Long lecturerId, Professor professor, Semester semester, boolean isExam, boolean isDifferentiatedCredit) {
        Repository.getInstance().editSubjectInfo(subjectInfoId, new SubjectInfo(group, subject, lecturerId, professor, semester, isExam, isDifferentiatedCredit), answer);
    }
}