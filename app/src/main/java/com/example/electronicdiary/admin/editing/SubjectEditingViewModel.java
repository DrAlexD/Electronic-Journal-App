package com.example.electronicdiary.admin.editing;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;
import com.example.electronicdiary.admin.SubjectFormState;
import com.example.electronicdiary.data_classes.Subject;

public class SubjectEditingViewModel extends ViewModel {
    private final MutableLiveData<SubjectFormState> subjectFormState = new MutableLiveData<>();
    private final MutableLiveData<Subject> subject = new MutableLiveData<>();

    LiveData<SubjectFormState> getSubjectFormState() {
        return subjectFormState;
    }

    public LiveData<Subject> getSubject() {
        return subject;
    }

    public void subjectEditingDataChanged(String subjectTitle) {
        subjectFormState.setValue(new SubjectFormState(subjectTitle));
    }

    public void downloadSubjectById(long subjectId) {
        Repository.getInstance().getSubjectById(subjectId, subject);
    }

    public void editSubject(long subjectId, String subjectTitle) {
        Repository.getInstance().editSubject(subjectId, new Subject(subjectTitle));
    }
}