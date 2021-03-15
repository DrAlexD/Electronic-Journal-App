package com.example.electronicdiary;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Repository {
    private static volatile Repository repository;
    private final Webservice webservice = new Retrofit.Builder().baseUrl("https://api.github.com/").
            addConverterFactory(GsonConverterFactory.create()).build().create(Webservice.class);
    public boolean availableSubjectsUpdate = false;
    public boolean availableSubjectsWithGroupsUpdate = false;
    public boolean semestersUpdate = false;
    public boolean allGroupsUpdate = false;
    public boolean allStudentsUpdate = false;
    public boolean allSubjectsUpdate = false;
    public boolean professorsUpdate = false;

    private Cache cache = new Cache();

    private Repository() {
    }

    public static Repository getInstance() {
        if (repository == null) {
            repository = new Repository();
        }
        return repository;
    }

    public User getUser() {
        return cache.getUser();
    }

    public void setLastLoggedInUser(int userId) {
        //TODO поиск пользователя по id в таблицах студентов и преподавателей
        cache.setUser(new Professor(userId, "xelagurd", "xelagurd"));
    }

    public void logout() {
        cache = new Cache();
    }

    public Result<User> login(String username, String password) {
        Result<User> result;
        try {
            //TODO шифрование пароля и сравнивание его с полученным из базы даннных
            //TODO 2 поиска - по студентам и по преподавателям
            if ("xelagurd".equals(username) && "123456".equals(password)) {
                cache.setUser(new Professor(1, username, username));
                result = new Result.Success<>(cache.getUser());
            } else
                result = new Result.Error("Login failed");
        } catch (Exception e) {
            result = new Result.Error(e.getMessage());
        }

        return result;
    }

    public ArrayList<String> getAvailableSubjects() {
        ArrayList<String> cached = cache.getAvailableSubjects();
        if (cached != null && !availableSubjectsUpdate) {
            return cached;
        }

        ArrayList<String> availableSubjects = new ArrayList<>();
        availableSubjects.add("Матан");
        availableSubjects.add("Мобилки");
        availableSubjects.add("Алгебра");

        cache.setAvailableSubjects(availableSubjects);
        return availableSubjects;
    }

    public HashMap<String, ArrayList<String>> getAvailableSubjectsWithGroups() {
        HashMap<String, ArrayList<String>> cached = cache.getAvailableSubjectsWithGroups();
        if (cached != null && !availableSubjectsWithGroupsUpdate) {
            return cached;
        }

        HashMap<String, ArrayList<String>> availableSubjectsWithGroups = new HashMap<>();
        ArrayList<String> availableSubjects = getAvailableSubjects();
        ArrayList<ArrayList<String>> groups = new ArrayList<>();
        ArrayList<String> groups1 = new ArrayList<>();
        groups1.add("ИУ9-11");
        groups1.add("ИУ9-21");
        groups1.add("ИУ9-31");
        ArrayList<String> groups2 = new ArrayList<>();
        groups2.add("ИУ9-41");
        groups2.add("ИУ9-51");
        groups2.add("ИУ9-61");
        ArrayList<String> groups3 = new ArrayList<>();
        groups3.add("ИУ9-11");
        groups3.add("ИУ9-31");
        groups3.add("ИУ9-51");
        groups.add(groups1);
        groups.add(groups2);
        groups.add(groups3);
        for (int i = 0; i < availableSubjects.size(); i++) {
            availableSubjectsWithGroups.put(availableSubjects.get(i), groups.get(i));
        }

        cache.setAvailableSubjectsWithGroups(availableSubjectsWithGroups);
        return availableSubjectsWithGroups;
    }

    public ArrayList<Semester> getSemesters() {
        ArrayList<Semester> cached = cache.getSemesters();
        if (cached != null && !semestersUpdate) {
            return cached;
        }

        ArrayList<Semester> semesters = new ArrayList<>();
        semesters.add(new Semester(2018, true));
        semesters.add(new Semester(2018, false));
        semesters.add(new Semester(2019, true));
        semesters.add(new Semester(2019, false));
        semesters.add(new Semester(2020, true));
        semesters.add(new Semester(2020, false));
        semesters.add(new Semester(2021, true));

        cache.setSemesters(semesters);
        return semesters;
    }

    public ArrayList<String> getAllGroups() {
        ArrayList<String> cached = cache.getAllGroups();
        if (cached != null && !allGroupsUpdate) {
            return cached;
        }

        ArrayList<String> allGroups = new ArrayList<>();
        allGroups.add("1ИУ9-11");
        allGroups.add("2ИУ9-21");
        allGroups.add("3ИУ9-31");
        allGroups.add("4ИУ9-41");
        allGroups.add("5ИУ9-51");
        allGroups.add("6ИУ9-61");
        allGroups.add("7ИУ9-71");
        allGroups.add("8ИУ9-81");
        allGroups.add("9ИУ9-91");

        cache.setAllGroups(allGroups);
        return allGroups;
    }

    public ArrayList<Student> getAllStudents() {
        ArrayList<Student> cached = cache.getAllStudents();
        if (cached != null && !allStudentsUpdate) {
            return cached;
        }

        ArrayList<Student> allStudents = new ArrayList<>();
        allStudents.add(new Student(1, "1ИУ9-11", "1Александр", "1Другаков"));
        allStudents.add(new Student(2, "2ИУ9-21", "2Александр", "2Другаков"));
        allStudents.add(new Student(3, "3ИУ9-31", "3Александр", "3Другаков"));
        allStudents.add(new Student(4, "4ИУ9-41", "4Александр", "4Другаков"));
        allStudents.add(new Student(5, "5ИУ9-51", "5Александр", "5Другаков"));
        allStudents.add(new Student(6, "6ИУ9-61", "6Александр", "6Другаков"));
        allStudents.add(new Student(7, "7ИУ9-71", "7Александр", "7Другаков"));
        allStudents.add(new Student(8, "8ИУ9-81", "8Александр", "8Другаков"));
        allStudents.add(new Student(9, "9ИУ9-91", "9Александр", "9Другаков"));

        cache.setAllStudents(allStudents);
        return allStudents;
    }

    public ArrayList<String> getAllSubjects() {
        ArrayList<String> cached = cache.getAllSubjects();
        if (cached != null && !allSubjectsUpdate) {
            return cached;
        }

        ArrayList<String> allSubjects = new ArrayList<>();
        allSubjects.add("Матан");
        allSubjects.add("Мобилки");
        allSubjects.add("Алгебра");
        allSubjects.add("Операционки");
        allSubjects.add("Моделирование");
        allSubjects.add("Методы оптимизации");

        cache.setAllSubjects(allSubjects);
        return allSubjects;
    }

    public ArrayList<Professor> getProfessors() {
        ArrayList<Professor> cached = cache.getProfessors();
        if (cached != null && !professorsUpdate) {
            return cached;
        }

        ArrayList<Professor> professors = new ArrayList<>();
        professors.add(new Professor(1, "Александр", "Коновалов"));
        professors.add(new Professor(2, "Сергей", "Скоробогатов"));
        professors.add(new Professor(3, "Анна", "Домрачева"));
        professors.add(new Professor(4, "Александр", "Дубанов"));
        professors.add(new Professor(5, "Юрий", "Каганов"));

        cache.setProfessors(professors);
        return professors;
    }
}