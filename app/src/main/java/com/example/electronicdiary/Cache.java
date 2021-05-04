package com.example.electronicdiary;

import com.example.electronicdiary.data_classes.Group;
import com.example.electronicdiary.data_classes.Professor;
import com.example.electronicdiary.data_classes.Semester;
import com.example.electronicdiary.data_classes.Student;
import com.example.electronicdiary.data_classes.Subject;
import com.example.electronicdiary.data_classes.SubjectInfo;
import com.example.electronicdiary.data_classes.User;

import java.util.HashMap;
import java.util.List;

public class Cache {
    private User user = null;
    private List<Student> allStudents = null;
    private List<Professor> professors = null;
    private List<Group> allGroups = null;
    private List<Subject> allSubjects = null;
    private List<Semester> semesters = null;
    private List<Student> availableStudents = null;
    private List<Subject> availableSubjects = null;
    private HashMap<Subject, List<SubjectInfo>> availableSubjectsWithGroups = null;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Student> getAllStudents() {
        return allStudents;
    }

    public void setAllStudents(List<Student> allStudents) {
        this.allStudents = allStudents;
    }

    public List<Professor> getProfessors() {
        return professors;
    }

    public void setProfessors(List<Professor> professors) {
        this.professors = professors;
    }

    public List<Group> getAllGroups() {
        return allGroups;
    }

    public void setAllGroups(List<Group> allGroups) {
        this.allGroups = allGroups;
    }

    public List<Subject> getAllSubjects() {
        return allSubjects;
    }

    public void setAllSubjects(List<Subject> allSubjects) {
        this.allSubjects = allSubjects;
    }

    public List<Semester> getSemesters() {
        return semesters;
    }

    public void setSemesters(List<Semester> semesters) {
        this.semesters = semesters;
    }

    public List<Student> getAvailableStudents() {
        return availableStudents;
    }

    public void setAvailableStudents(List<Student> availableStudents) {
        this.availableStudents = availableStudents;
    }

    public List<Subject> getAvailableSubjects() {
        return availableSubjects;
    }

    public void setAvailableSubjects(List<Subject> availableSubjects) {
        this.availableSubjects = availableSubjects;
    }

    public HashMap<Subject, List<SubjectInfo>> getAvailableSubjectsWithGroups() {
        return availableSubjectsWithGroups;
    }

    public void setAvailableSubjectsWithGroups(HashMap<Subject, List<SubjectInfo>> availableSubjectsWithGroups) {
        this.availableSubjectsWithGroups = availableSubjectsWithGroups;
    }
}
