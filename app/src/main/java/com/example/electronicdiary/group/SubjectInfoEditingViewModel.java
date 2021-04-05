package com.example.electronicdiary.group;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;
import com.example.electronicdiary.SubjectInfo;

public class SubjectInfoEditingViewModel extends ViewModel {
    private final MutableLiveData<SubjectInfo> subjectInfo = new MutableLiveData<>();

    public MutableLiveData<SubjectInfo> getSubjectInfo() {
        return subjectInfo;
    }

    public void downloadSubjectInfo(int groupId, int subjectId, int lecturerId, int seminarianId, int semesterId) {
        this.subjectInfo.setValue(Repository.getInstance().getSubjectInfo(groupId, subjectId, lecturerId, seminarianId, semesterId));
    }

    public void editSubjectInfo(int groupId, int subjectId, int lecturerId, int seminarianId, int semesterId,
                                boolean isSwapLecturerAndSeminarian, boolean isExam, boolean isDifferentiatedCredit, boolean isForAllGroups) {
        Repository.getInstance().editSubjectInfo(groupId, subjectId, lecturerId, seminarianId, semesterId, isSwapLecturerAndSeminarian,
                isExam, isDifferentiatedCredit, isForAllGroups);
    }
}