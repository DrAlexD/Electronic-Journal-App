package com.example.electronicdiary.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;
import com.example.electronicdiary.data_classes.Semester;
import com.example.electronicdiary.data_classes.Subject;
import com.example.electronicdiary.data_classes.SubjectInfo;

import java.util.HashMap;
import java.util.List;

public class ProfileViewModel extends ViewModel {
    private final MutableLiveData<Semester> semester = new MutableLiveData<>();
    private final MutableLiveData<List<Subject>> availableSubjects = new MutableLiveData<>();
    private final MutableLiveData<HashMap<Subject, List<SubjectInfo>>> availableSubjectsWithGroups = new MutableLiveData<>();

    public MutableLiveData<Semester> getSemester() {
        return semester;
    }

    public LiveData<List<Subject>> getAvailableSubjects() {
        return availableSubjects;
    }

    public LiveData<HashMap<Subject, List<SubjectInfo>>> getAvailableSubjectsWithGroups() {
        return availableSubjectsWithGroups;
    }

    public void downloadSemesterById(long semesterId) {
        this.semester.setValue(Repository.getInstance().getSemesterById(semesterId));
    }

    public void downloadAvailableSubjectsWithGroups(long professorId, long semesterId) {
        this.availableSubjects.setValue(Repository.getInstance().getAvailableSubjects(semesterId));
        this.availableSubjectsWithGroups.setValue(Repository.getInstance().getAvailableSubjectsWithGroups(professorId, semesterId));
    }
}