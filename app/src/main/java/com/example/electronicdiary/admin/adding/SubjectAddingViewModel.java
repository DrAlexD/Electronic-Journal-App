package com.example.electronicdiary.admin.adding;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;
import com.example.electronicdiary.admin.SubjectFormState;
import com.example.electronicdiary.data_classes.Subject;

public class SubjectAddingViewModel extends ViewModel {
    private final MutableLiveData<SubjectFormState> subjectFormState = new MutableLiveData<>();

    LiveData<SubjectFormState> getSubjectFormState() {
        return subjectFormState;
    }

    public void subjectAddingDataChanged(String subjectTitle) {
        subjectFormState.setValue(new SubjectFormState(subjectTitle));
    }

    public void addSubject(String subjectTitle) {
        Repository.getInstance().addSubject(new Subject(subjectTitle));
    }
}