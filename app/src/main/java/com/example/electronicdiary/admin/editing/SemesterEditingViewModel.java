package com.example.electronicdiary.admin.editing;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;
import com.example.electronicdiary.admin.SemesterFormState;
import com.example.electronicdiary.data_classes.Semester;

public class SemesterEditingViewModel extends ViewModel {
    private final MutableLiveData<SemesterFormState> semesterFormState = new MutableLiveData<>();
    private final MutableLiveData<Semester> semester = new MutableLiveData<>();

    LiveData<SemesterFormState> getSemesterFormState() {
        return semesterFormState;
    }

    public MutableLiveData<Semester> getSemester() {
        return semester;
    }

    public void semesterEditingDataChanged(String semesterYear) {
        semesterFormState.setValue(new SemesterFormState(semesterYear));
    }

    public void downloadSemesterById(int semesterId) {
        this.semester.setValue(Repository.getInstance().getSemesterById(semesterId));
    }

    public void editSemester(int semesterId, int semesterYear, boolean isFirstHalf) {
        Repository.getInstance().editSemester(semesterId, semesterYear, isFirstHalf);
    }
}