package com.example.electronicdiary.admin.adding;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;

public class SemesterAddingViewModel extends ViewModel {
    private final MutableLiveData<SemesterAddingFormState> semesterAddingFormState = new MutableLiveData<>();

    LiveData<SemesterAddingFormState> getSemesterAddingFormState() {
        return semesterAddingFormState;
    }

    public void semesterAddingDataChanged(String semesterYear) {
        semesterAddingFormState.setValue(new SemesterAddingFormState(semesterYear));
    }

    public void addSemester(String semesterYear) {
        Repository.getInstance().addSemester(semesterYear);
    }
}