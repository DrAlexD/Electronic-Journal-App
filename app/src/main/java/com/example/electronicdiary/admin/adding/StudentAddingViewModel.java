package com.example.electronicdiary.admin.adding;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;
import com.example.electronicdiary.admin.StudentFormState;

public class StudentAddingViewModel extends ViewModel {
    private final MutableLiveData<StudentFormState> studentFormState = new MutableLiveData<>();
    private final MutableLiveData<Long> lastStudentId = new MutableLiveData<>();

    LiveData<StudentFormState> getStudentFormState() {
        return studentFormState;
    }

    public LiveData<Long> getLastStudentId() {
        return lastStudentId;
    }

    public void studentAddingDataChanged(String studentName, String studentSecondName, String studentLogin,
                                         String studentPassword, boolean isNameOrSecondNameChanged) {
        studentFormState.setValue(new StudentFormState(studentName, studentSecondName, studentLogin,
                studentPassword, isNameOrSecondNameChanged));
    }

    public void downloadLastStudentId() {
        this.lastStudentId.setValue(Repository.getInstance().getLastStudentId());
    }
}