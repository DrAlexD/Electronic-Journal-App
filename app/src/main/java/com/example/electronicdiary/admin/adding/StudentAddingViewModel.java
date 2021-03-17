package com.example.electronicdiary.admin.adding;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;

public class StudentAddingViewModel extends ViewModel {
    private final MutableLiveData<StudentAddingFormState> studentAddingFormState = new MutableLiveData<>();
    private final MutableLiveData<Integer> lastStudentId = new MutableLiveData<>();

    LiveData<StudentAddingFormState> getStudentAddingFormState() {
        return studentAddingFormState;
    }

    public LiveData<Integer> getLastStudentId() {
        return lastStudentId;
    }

    public void studentAddingDataChanged(String studentName, String studentSecondName, String studentLogin,
                                         String studentPassword, boolean isNameOrSecondNameChanged) {
        studentAddingFormState.setValue(new StudentAddingFormState(studentName, studentSecondName, studentLogin,
                studentPassword, isNameOrSecondNameChanged));
    }

    public void downloadLastStudentId() {
        this.lastStudentId.setValue(Repository.getInstance().getLastStudentId());
    }
}