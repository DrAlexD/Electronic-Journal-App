package com.example.electronicdiary.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.GroupInfo;
import com.example.electronicdiary.Repository;
import com.example.electronicdiary.Semester;
import com.example.electronicdiary.Subject;

import java.util.ArrayList;
import java.util.HashMap;

public class ProfileViewModel extends ViewModel {
    private final MutableLiveData<Semester> semester = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Subject>> availableSubjects = new MutableLiveData<>();
    private final MutableLiveData<HashMap<Subject, ArrayList<GroupInfo>>> availableSubjectsWithGroups = new MutableLiveData<>();

    public MutableLiveData<Semester> getSemester() {
        return semester;
    }

    public LiveData<ArrayList<Subject>> getAvailableSubjects() {
        return availableSubjects;
    }

    public LiveData<HashMap<Subject, ArrayList<GroupInfo>>> getAvailableSubjectsWithGroups() {
        return availableSubjectsWithGroups;
    }

    public void downloadSemesterById(int semesterId) {
        this.semester.setValue(Repository.getInstance().getSemesterById(semesterId));
    }

    public void downloadAvailableSubjectsWithGroups(int professorId, int semesterId) {
        this.availableSubjects.setValue(Repository.getInstance().getAvailableSubjects(semesterId));
        this.availableSubjectsWithGroups.setValue(Repository.getInstance().getAvailableSubjectsWithGroups(professorId, semesterId));
    }
}