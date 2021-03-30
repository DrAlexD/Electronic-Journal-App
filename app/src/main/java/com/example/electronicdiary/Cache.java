package com.example.electronicdiary;

import java.util.ArrayList;
import java.util.HashMap;

public class Cache {
    private User user = null;
    private Integer lastStudentId = null;
    private Integer lastProfessorId = null;
    private ArrayList<Student> allStudents = null;
    private ArrayList<Professor> professors = null;
    private ArrayList<Group> allGroups = null;
    private ArrayList<Subject> allSubjects = null;
    private ArrayList<Semester> semesters = null;
    private ArrayList<Student> availableStudents = null;
    private ArrayList<Subject> availableSubjects = null;
    private HashMap<Subject, ArrayList<GroupInfo>> availableSubjectsWithGroups = null;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getLastStudentId() {
        return lastStudentId;
    }

    public void setLastStudentId(Integer lastStudentId) {
        this.lastStudentId = lastStudentId;
    }

    public Integer getLastProfessorId() {
        return lastProfessorId;
    }

    public void setLastProfessorId(Integer lastProfessorId) {
        this.lastProfessorId = lastProfessorId;
    }

    public ArrayList<Student> getAllStudents() {
        return allStudents;
    }

    public void setAllStudents(ArrayList<Student> allStudents) {
        this.allStudents = allStudents;
    }

    public ArrayList<Professor> getProfessors() {
        return professors;
    }

    public void setProfessors(ArrayList<Professor> professors) {
        this.professors = professors;
    }

    public ArrayList<Group> getAllGroups() {
        return allGroups;
    }

    public void setAllGroups(ArrayList<Group> allGroups) {
        this.allGroups = allGroups;
    }

    public ArrayList<Subject> getAllSubjects() {
        return allSubjects;
    }

    public void setAllSubjects(ArrayList<Subject> allSubjects) {
        this.allSubjects = allSubjects;
    }

    public ArrayList<Semester> getSemesters() {
        return semesters;
    }

    public void setSemesters(ArrayList<Semester> semesters) {
        this.semesters = semesters;
    }

    public ArrayList<Student> getAvailableStudents() {
        return availableStudents;
    }

    public void setAvailableStudents(ArrayList<Student> availableStudents) {
        this.availableStudents = availableStudents;
    }

    public ArrayList<Subject> getAvailableSubjects() {
        return availableSubjects;
    }

    public void setAvailableSubjects(ArrayList<Subject> availableSubjects) {
        this.availableSubjects = availableSubjects;
    }

    public HashMap<Subject, ArrayList<GroupInfo>> getAvailableSubjectsWithGroups() {
        return availableSubjectsWithGroups;
    }

    public void setAvailableSubjectsWithGroups(HashMap<Subject, ArrayList<GroupInfo>> availableSubjectsWithGroups) {
        this.availableSubjectsWithGroups = availableSubjectsWithGroups;
    }
}
