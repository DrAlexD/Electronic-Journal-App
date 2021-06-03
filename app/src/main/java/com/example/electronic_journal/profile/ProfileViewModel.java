package com.example.electronic_journal.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronic_journal.Repository;
import com.example.electronic_journal.data_classes.Semester;
import com.example.electronic_journal.data_classes.Subject;
import com.example.electronic_journal.data_classes.SubjectInfo;

import java.util.List;
import java.util.Map;

public class ProfileViewModel extends ViewModel {
    private final MutableLiveData<Semester> semester = new MutableLiveData<>();
    private final MutableLiveData<List<Subject>> availableSubjects = new MutableLiveData<>();
    private final MutableLiveData<Map<String, List<SubjectInfo>>> availableSubjectsWithGroups = new MutableLiveData<>();

    public LiveData<Semester> getSemester() {
        return semester;
    }

    public LiveData<List<Subject>> getAvailableSubjects() {
        return availableSubjects;
    }

    public LiveData<Map<String, List<SubjectInfo>>> getAvailableSubjectsWithGroups() {
        return availableSubjectsWithGroups;
    }

    public void downloadSemesterById(long semesterId) {
        Repository.getInstance().getSemesterById(semesterId, semester);
    }

    public void downloadAvailableSubjects(long professorId, long semesterId) {
        Repository.getInstance().getAvailableSubjects(professorId, semesterId, availableSubjects);
    }

    public void downloadAvailableSubjectsWithGroups(long professorId, long semesterId) {
        Repository.getInstance().getAvailableSubjectsWithGroups(professorId, semesterId, availableSubjectsWithGroups);
    }
}