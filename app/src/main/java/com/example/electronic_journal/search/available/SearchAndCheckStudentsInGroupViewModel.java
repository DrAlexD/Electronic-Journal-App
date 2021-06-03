package com.example.electronic_journal.search.available;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronic_journal.Repository;
import com.example.electronic_journal.Result;
import com.example.electronic_journal.data_classes.Group;
import com.example.electronic_journal.data_classes.Student;

import java.util.List;

public class SearchAndCheckStudentsInGroupViewModel extends ViewModel {
    private final MutableLiveData<List<Student>> studentsInGroup = new MutableLiveData<>();
    private final MutableLiveData<Group> group = new MutableLiveData<>();
    private final MutableLiveData<Boolean> firstAnswer = new MutableLiveData<>();
    private final MutableLiveData<Result<Boolean>> secondAnswer = new MutableLiveData<>();

    public LiveData<Boolean> getFirstAnswer() {
        return firstAnswer;
    }

    public LiveData<Result<Boolean>> getSecondAnswer() {
        return secondAnswer;
    }

    public LiveData<Group> getGroup() {
        return group;
    }

    public LiveData<List<Student>> getStudentsInGroup() {
        return studentsInGroup;
    }

    public void downloadStudentsInGroup(long groupId) {
        Repository.getInstance().getStudentsInGroup(groupId, studentsInGroup);
    }

    public void changeGroupInSubjectsInfo(long fromGroupId, long toGroupId) {
        Repository.getInstance().changeGroupInSubjectsInfo(fromGroupId, toGroupId, firstAnswer);
    }

    public void downloadGroup(long groupId) {
        Repository.getInstance().getGroupById(groupId, group);
    }

    public void changeStudentGroup(Student student, Group group) {
        Repository.getInstance().editStudent(student.getId(), new Student(student.getFirstName(),
                student.getSecondName(), group, student.getUsername(), student.getPassword(), "ROLE_STUDENT"), secondAnswer);
    }
}
