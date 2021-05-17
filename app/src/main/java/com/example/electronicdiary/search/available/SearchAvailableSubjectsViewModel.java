package com.example.electronicdiary.search.available;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronicdiary.Repository;
import com.example.electronicdiary.data_classes.Subject;
import com.example.electronicdiary.data_classes.SubjectInfo;

import java.util.List;
import java.util.Map;

public class SearchAvailableSubjectsViewModel extends ViewModel {
    private final MutableLiveData<List<Subject>> availableSubjects = new MutableLiveData<>();
    private final MutableLiveData<Map<String, List<SubjectInfo>>> availableSubjectsWithGroups = new MutableLiveData<>();
    private final MutableLiveData<Boolean> answer = new MutableLiveData<>();

    public LiveData<Boolean> getAnswer() {
        return answer;
    }

    public LiveData<List<Subject>> getAvailableSubjects() {
        return availableSubjects;
    }

    public LiveData<Map<String, List<SubjectInfo>>> getAvailableSubjectsWithGroups() {
        return availableSubjectsWithGroups;
    }

    public void downloadAvailableSubjects(long professorId, long semesterId) {
        Repository.getInstance().getAvailableSubjects(professorId, semesterId, availableSubjects);
    }

    public void downloadAvailableSubjectsWithGroups(long professorId, long semesterId) {
        Repository.getInstance().getAvailableSubjectsWithGroups(professorId, semesterId, availableSubjectsWithGroups);
    }

    public void deleteAvailableSubject(long subjectInfoId, long professorId) {
        Repository.getInstance().deleteSubjectInfo(subjectInfoId, professorId, answer);
    }
}