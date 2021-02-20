package com.example.electronicdiary.search;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.student.Student;

import java.util.ArrayList;

public class SearchViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<Student>> students = new MutableLiveData<>();

    public MutableLiveData<ArrayList<Student>> getStudents() {
        return students;
    }

    public void setSubjectsWithGroups(ArrayList<Student> students) {
        this.students.setValue(students);
    }
}
