package com.example.electronicdiary.admin.adding;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;

public class SubjectAddingViewModel extends ViewModel {
    private final MutableLiveData<SubjectAddingFormState> subjectAddingFormState = new MutableLiveData<>();

    LiveData<SubjectAddingFormState> getSubjectAddingFormState() {
        return subjectAddingFormState;
    }

    public void subjectAddingDataChanged(String subjectTitle) {
        subjectAddingFormState.setValue(new SubjectAddingFormState(subjectTitle));
    }

    public void addSubject(String subjectTitle) {
        Repository.getInstance().addSubject(subjectTitle);
    }
}