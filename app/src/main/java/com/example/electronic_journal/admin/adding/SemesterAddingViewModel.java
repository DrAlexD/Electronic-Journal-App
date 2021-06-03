package com.example.electronic_journal.admin.adding;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronic_journal.Repository;
import com.example.electronic_journal.Result;
import com.example.electronic_journal.admin.SemesterFormState;
import com.example.electronic_journal.data_classes.Semester;

public class SemesterAddingViewModel extends ViewModel {
    private final MutableLiveData<SemesterFormState> semesterFormState = new MutableLiveData<>();
    private MutableLiveData<Result<Boolean>> answer = new MutableLiveData<>();

    public LiveData<Result<Boolean>> getAnswer() {
        return answer;
    }

    public void setAnswer(MutableLiveData<Result<Boolean>> answer) {
        this.answer = answer;
    }

    LiveData<SemesterFormState> getSemesterFormState() {
        return semesterFormState;
    }

    public void semesterAddingDataChanged(String semesterYear, boolean isFirstHalf, boolean isSecondHalf) {
        semesterFormState.setValue(new SemesterFormState(semesterYear, isFirstHalf, isSecondHalf));
    }

    public void addSemester(int semesterYear, boolean isFirstHalf) {
        Repository.getInstance().addSemester(new Semester(semesterYear, isFirstHalf), answer);
    }
}