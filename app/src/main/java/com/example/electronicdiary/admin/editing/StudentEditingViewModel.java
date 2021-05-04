package com.example.electronicdiary.admin.editing;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;
import com.example.electronicdiary.admin.StudentFormState;
import com.example.electronicdiary.data_classes.Student;

public class StudentEditingViewModel extends ViewModel {
    private final MutableLiveData<StudentFormState> studentFormState = new MutableLiveData<>();
    private final MutableLiveData<Student> student = new MutableLiveData<>();

    LiveData<StudentFormState> getStudentFormState() {
        return studentFormState;
    }

    public MutableLiveData<Student> getStudent() {
        return student;
    }

    public void studentEditingDataChanged(String studentName, String studentSecondName, String studentLogin,
                                          String studentPassword, boolean isNameOrSecondNameChanged) {
        studentFormState.setValue(new StudentFormState(studentName, studentSecondName, studentLogin,
                studentPassword, isNameOrSecondNameChanged));
    }

    public void downloadStudentByIdWithLogin(long studentId) {
        this.student.setValue(Repository.getInstance().getStudentByIdWithLogin(studentId));
    }

    public void editStudent(long studentId, String studentName, String studentSecondName, String studentLogin, String studentPassword) {
        Repository.getInstance().editStudent(studentId, studentName, studentSecondName, studentLogin, studentPassword);
    }
}