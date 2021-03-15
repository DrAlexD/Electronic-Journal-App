package com.example.electronicdiary;

import java.util.ArrayList;
import java.util.HashMap;

public class Cache {
    private User user = null;
    private ArrayList<String> availableSubjects = null;
    private HashMap<String, ArrayList<String>> availableSubjectsWithGroups = null;
    private ArrayList<Semester> semesters = null;
    private ArrayList<String> allGroups = null;
    private ArrayList<Student> allStudents = null;
    private ArrayList<Student> availableStudents = null;
    private ArrayList<String> allSubjects = null;
    private ArrayList<Professor> professors = null;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<String> getAvailableSubjects() {
        return availableSubjects;
    }

    public void setAvailableSubjects(ArrayList<String> availableSubjects) {
        this.availableSubjects = availableSubjects;
    }

    public HashMap<String, ArrayList<String>> getAvailableSubjectsWithGroups() {
        return availableSubjectsWithGroups;
    }

    public void setAvailableSubjectsWithGroups(HashMap<String, ArrayList<String>> availableSubjectsWithGroups) {
        this.availableSubjectsWithGroups = availableSubjectsWithGroups;
    }

    public ArrayList<Semester> getSemesters() {
        return semesters;
    }

    public void setSemesters(ArrayList<Semester> semesters) {
        this.semesters = semesters;
    }

    public ArrayList<String> getAllGroups() {
        return allGroups;
    }

    public void setAllGroups(ArrayList<String> allGroups) {
        this.allGroups = allGroups;
    }

    public ArrayList<Student> getAllStudents() {
        return allStudents;
    }

    public void setAllStudents(ArrayList<Student> allStudents) {
        this.allStudents = allStudents;
    }

    public ArrayList<String> getAllSubjects() {
        return allSubjects;
    }

    public void setAllSubjects(ArrayList<String> allSubjects) {
        this.allSubjects = allSubjects;
    }

    public ArrayList<Professor> getProfessors() {
        return professors;
    }

    public void setProfessors(ArrayList<Professor> professors) {
        this.professors = professors;
    }

    public ArrayList<Student> getAvailableStudents() {
        return availableStudents;
    }

    public void setAvailableStudents(ArrayList<Student> availableStudents) {
        this.availableStudents = availableStudents;
    }
}
