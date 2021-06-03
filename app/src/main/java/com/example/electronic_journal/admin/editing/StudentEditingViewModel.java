package com.example.electronic_journal.admin.editing;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronic_journal.Repository;
import com.example.electronic_journal.Result;
import com.example.electronic_journal.admin.StudentFormState;
import com.example.electronic_journal.data_classes.Group;
import com.example.electronic_journal.data_classes.Student;

public class StudentEditingViewModel extends ViewModel {
    private final MutableLiveData<StudentFormState> studentFormState = new MutableLiveData<>();
    private final MutableLiveData<Result<Student>> student = new MutableLiveData<>();
    private MutableLiveData<Result<Boolean>> answer = new MutableLiveData<>();

    public LiveData<Result<Boolean>> getAnswer() {
        return answer;
    }

    public void setAnswer(MutableLiveData<Result<Boolean>> answer) {
        this.answer = answer;
    }

    LiveData<StudentFormState> getStudentFormState() {
        return studentFormState;
    }

    public LiveData<Result<Student>> getStudent() {
        return student;
    }

    public void studentEditingDataChanged(String studentName, String studentSecondName, String studentLogin,
                                          String studentPassword, boolean isNameOrSecondNameChanged) {
        studentFormState.setValue(new StudentFormState(studentName, studentSecondName, studentLogin,
                studentPassword, isNameOrSecondNameChanged));
    }

    public void downloadStudentByIdWithLogin(long studentId) {
        Repository.getInstance().getStudentById(studentId, student);
    }

    public void editStudent(long studentId, String studentName, String studentSecondName, Group group, String studentLogin, String studentPassword, String role) {
        Repository.getInstance().editStudent(studentId, new Student(studentName, studentSecondName, group, studentLogin, studentPassword, role), answer);
    }
}