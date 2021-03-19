package com.example.electronicdiary.admin.editing;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;
import com.example.electronicdiary.admin.SubjectFormState;

public class SubjectEditingViewModel extends ViewModel {
    private final MutableLiveData<SubjectFormState> subjectFormState = new MutableLiveData<>();
    private final MutableLiveData<String> subject = new MutableLiveData<>();

    LiveData<SubjectFormState> getSubjectFormState() {
        return subjectFormState;
    }

    public MutableLiveData<String> getSubject() {
        return subject;
    }

    public void subjectEditingDataChanged(String subjectTitle) {
        subjectFormState.setValue(new SubjectFormState(subjectTitle));
    }

    public void downloadSubjectById(int subjectId) {
        this.subject.setValue(Repository.getInstance().getSubjectById(subjectId));
    }

    public void editSubject(String subjectTitle) {
        Repository.getInstance().editSubject(subjectTitle);
    }
}