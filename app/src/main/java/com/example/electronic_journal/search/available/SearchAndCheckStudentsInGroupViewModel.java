package com.example.electronic_journal.search.available;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.electronic_journal.Repository;
import com.example.electronic_journal.Result;
import com.example.electronic_journal.data_classes.Event;
import com.example.electronic_journal.data_classes.Group;
import com.example.electronic_journal.data_classes.Lesson;
import com.example.electronic_journal.data_classes.Student;
import com.example.electronic_journal.data_classes.StudentEvent;
import com.example.electronic_journal.data_classes.StudentLesson;
import com.example.electronic_journal.data_classes.StudentPerformanceInModule;

import java.util.List;
import java.util.Map;

public class SearchAndCheckStudentsInGroupViewModel extends ViewModel {
    private final MutableLiveData<List<Student>> studentsInGroup = new MutableLiveData<>();
    private final MutableLiveData<Group> group = new MutableLiveData<>();
    private final MutableLiveData<Event> event = new MutableLiveData<>();
    private final MutableLiveData<Lesson> lesson = new MutableLiveData<>();
    private final MutableLiveData<Boolean> firstAnswer = new MutableLiveData<>();
    private final MutableLiveData<Result<Boolean>> secondAnswer = new MutableLiveData<>();
    private final MutableLiveData<Map<String, Map<String, List<StudentEvent>>>> studentsEvents = new MutableLiveData<>();
    private final MutableLiveData<Map<String, List<StudentPerformanceInModule>>> studentsPerformancesInModules = new MutableLiveData<>();

    public LiveData<Boolean> getFirstAnswer() {
        return firstAnswer;
    }

    public LiveData<Result<Boolean>> getSecondAnswer() {
        return secondAnswer;
    }

    public LiveData<Group> getGroup() {
        return group;
    }

    public LiveData<Lesson> getLesson() {
        return lesson;
    }

    public LiveData<Event> getEvent() {
        return event;
    }

    public LiveData<List<Student>> getStudentsInGroup() {
        return studentsInGroup;
    }

    public LiveData<Map<String, Map<String, List<StudentEvent>>>> getStudentsEvents() {
        return studentsEvents;
    }

    public LiveData<Map<String, List<StudentPerformanceInModule>>> getStudentsPerformancesInModules() {
        return studentsPerformancesInModules;
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

    public void downloadEvent(long eventId) {
        Repository.getInstance().getEventById(eventId, event);
    }

    public void downloadLesson(long lessonId) {
        Repository.getInstance().getLessonById(lessonId, lesson);
    }

    public void downloadStudentsEvents(long subjectInfoId) {
        Repository.getInstance().getStudentsEvents(subjectInfoId, studentsEvents);
    }

    public void downloadStudentsPerformancesInModules(long subjectInfoId) {
        Repository.getInstance().getStudentsPerformancesInModules(subjectInfoId, studentsPerformancesInModules);
    }

    public void changeStudentGroup(Student student, Group group) {
        Repository.getInstance().editStudent(student.getId(), new Student(student.getFirstName(),
                student.getSecondName(), group, student.getUsername(), student.getPassword(), "ROLE_STUDENT"), secondAnswer);
    }

    public void addStudentEvent(int attemptNumber, StudentPerformanceInModule studentPerformanceInModule, Event event, boolean isAttended) {
        Repository.getInstance().addStudentEvent(new StudentEvent(attemptNumber, studentPerformanceInModule, event, isAttended), firstAnswer);
    }

    public void addStudentLesson(StudentPerformanceInModule studentPerformanceInModule, Lesson lesson, boolean isAttended) {
        Repository.getInstance().addStudentLesson(new StudentLesson(studentPerformanceInModule, lesson, isAttended), firstAnswer);
    }
}
