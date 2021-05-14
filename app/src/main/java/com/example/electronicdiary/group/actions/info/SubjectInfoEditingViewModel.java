package com.example.electronicdiary.group.actions.info;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;
import com.example.electronicdiary.data_classes.Group;
import com.example.electronicdiary.data_classes.Professor;
import com.example.electronicdiary.data_classes.Semester;
import com.example.electronicdiary.data_classes.Subject;
import com.example.electronicdiary.data_classes.SubjectInfo;

public class SubjectInfoEditingViewModel extends ViewModel {
    private final MutableLiveData<SubjectInfo> subjectInfo = new MutableLiveData<>();

    public LiveData<SubjectInfo> getSubjectInfo() {
        return subjectInfo;
    }

    public void downloadSubjectInfo(long subjectInfoId) {
        Repository.getInstance().getSubjectInfoById(subjectInfoId, subjectInfo);
    }

    public void editSubjectInfo(long subjectInfoId, Group group, Subject subject, long lecturerId, Professor professor, Semester semester, boolean isExam, boolean isDifferentiatedCredit) {
        Repository.getInstance().editSubjectInfo(subjectInfoId, new SubjectInfo(group, subject, lecturerId, professor, semester, isExam, isDifferentiatedCredit));
    }
}