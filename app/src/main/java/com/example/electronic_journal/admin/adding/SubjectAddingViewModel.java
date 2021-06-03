package com.example.electronic_journal.admin.adding;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronic_journal.Repository;
import com.example.electronic_journal.Result;
import com.example.electronic_journal.admin.SubjectFormState;
import com.example.electronic_journal.data_classes.Subject;

public class SubjectAddingViewModel extends ViewModel {
    private final MutableLiveData<SubjectFormState> subjectFormState = new MutableLiveData<>();
    private MutableLiveData<Result<Boolean>> answer = new MutableLiveData<>();

    public LiveData<Result<Boolean>> getAnswer() {
        return answer;
    }

    public void setAnswer(MutableLiveData<Result<Boolean>> answer) {
        this.answer = answer;
    }

    LiveData<SubjectFormState> getSubjectFormState() {
        return subjectFormState;
    }

    public void subjectAddingDataChanged(String subjectTitle) {
        subjectFormState.setValue(new SubjectFormState(subjectTitle));
    }

    public void addSubject(String subjectTitle) {
        Repository.getInstance().addSubject(new Subject(subjectTitle), answer);
    }
}