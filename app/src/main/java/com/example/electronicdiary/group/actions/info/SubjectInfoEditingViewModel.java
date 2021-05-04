package com.example.electronicdiary.group.actions.info;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;
import com.example.electronicdiary.data_classes.SubjectInfo;

public class SubjectInfoEditingViewModel extends ViewModel {
    private final MutableLiveData<SubjectInfo> subjectInfo = new MutableLiveData<>();

    public MutableLiveData<SubjectInfo> getSubjectInfo() {
        return subjectInfo;
    }

    public void downloadSubjectInfo(long groupId, long subjectId, long lecturerId, long seminarianId, long semesterId) {
        this.subjectInfo.setValue(Repository.getInstance().getSubjectInfo(groupId, subjectId, lecturerId, seminarianId, semesterId));
    }

    public void editSubjectInfo(long groupId, long subjectId, long lecturerId, long seminarianId, long semesterId,
                                boolean isSwapLecturerAndSeminarian, boolean isExam, boolean isDifferentiatedCredit, boolean isForAllGroups) {
        Repository.getInstance().editSubjectInfo(groupId, subjectId, lecturerId, seminarianId, semesterId, isSwapLecturerAndSeminarian,
                isExam, isDifferentiatedCredit, isForAllGroups);
    }
}