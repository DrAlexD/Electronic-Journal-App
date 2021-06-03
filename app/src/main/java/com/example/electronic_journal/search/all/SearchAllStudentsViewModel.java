package com.example.electronic_journal.search.all;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronic_journal.Repository;
import com.example.electronic_journal.data_classes.Student;

import java.util.List;

public class SearchAllStudentsViewModel extends ViewModel {
    private final MutableLiveData<List<Student>> allStudents = new MutableLiveData<>();
    private final MutableLiveData<Boolean> answer = new MutableLiveData<>();

    public LiveData<Boolean> getAnswer() {
        return answer;
    }

    public LiveData<List<Student>> getAllStudents() {
        return allStudents;
    }

    public void downloadAllStudents() {
        Repository.getInstance().getAllStudents(allStudents);
    }

    public void deleteStudent(long studentId) {
        Repository.getInstance().deleteStudent(studentId, answer);
    }
}