package com.example.electronicdiary.admin.adding;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;
import com.example.electronicdiary.admin.SemesterFormState;
import com.example.electronicdiary.data_classes.Semester;

public class SemesterAddingViewModel extends ViewModel {
    private final MutableLiveData<SemesterFormState> semesterFormState = new MutableLiveData<>();

    LiveData<SemesterFormState> getSemesterFormState() {
        return semesterFormState;
    }

    public void semesterAddingDataChanged(String semesterYear) {
        semesterFormState.setValue(new SemesterFormState(semesterYear));
    }

    public void addSemester(int semesterYear, boolean isFirstHalf) {
        Repository.getInstance().addSemester(new Semester(semesterYear, isFirstHalf));
    }
}