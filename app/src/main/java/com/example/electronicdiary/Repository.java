package com.example.electronicdiary;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

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
    public boolean availableStudentsUpdate = false;
    public boolean professorsUpdate = false;
    public boolean lastStudentIdUpdate = false;
    public boolean lastProfessorIdUpdate = false;

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

    public ArrayList<String> getModules() {
        ArrayList<String> modules = new ArrayList<>();
        modules.add("Модуль 1");
        modules.add("Модуль 2");
        modules.add("Модуль 3");

        return modules;
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

    public ArrayList<String> getAvailableGroupsInSubject(String subject) {
        ArrayList<String> availableGroupsInSubject = new ArrayList<>();
        availableGroupsInSubject.add("ИУ9-11");
        availableGroupsInSubject.add("ИУ9-41");
        availableGroupsInSubject.add("ИУ9-31");

        return availableGroupsInSubject;
    }

    public HashMap<String, ArrayList<String>> getAvailableSubjectsWithGroups() {
        HashMap<String, ArrayList<String>> cached = cache.getAvailableSubjectsWithGroups();
        if (cached != null && !availableSubjectsWithGroupsUpdate) {
            return cached;
        }

        HashMap<String, ArrayList<String>> availableSubjectsWithGroups = new HashMap<>();
        ArrayList<String> availableSubjects = getAvailableSubjects();
        for (int i = 0; i < availableSubjects.size(); i++) {
            ArrayList<String> groups = getAvailableGroupsInSubject(availableSubjects.get(i));
            availableSubjectsWithGroups.put(availableSubjects.get(i), groups);
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

    public ArrayList<Student> getAvailableStudents() {
        ArrayList<Student> cached = cache.getAvailableStudents();
        if (cached != null && !availableStudentsUpdate) {
            return cached;
        }

        ArrayList<Student> availableStudents = new ArrayList<>();
        availableStudents.add(new Student(1, "1ИУ9-11", "1Александр", "1Другаков"));
        availableStudents.add(new Student(2, "2ИУ9-21", "2Александр", "2Другаков"));
        availableStudents.add(new Student(3, "3ИУ9-31", "3Александр", "3Другаков"));
        availableStudents.add(new Student(4, "4ИУ9-41", "4Александр", "4Другаков"));
        availableStudents.add(new Student(5, "5ИУ9-51", "5Александр", "5Другаков"));

        cache.setAvailableStudents(availableStudents);
        return availableStudents;
    }

    public ArrayList<String> getAvailableStudentSubjects(String studentName, String group) {
        ArrayList<String> availableStudentSubjects = new ArrayList<>();
        availableStudentSubjects.add("Матан");
        availableStudentSubjects.add("Мобилки");
        availableStudentSubjects.add("Алгебра");

        return availableStudentSubjects;
    }

    public ArrayList<StudentEvent> getStudentEvents(String studentName, String subject, String group) {
        ArrayList<StudentEvent> studentEvents = new ArrayList<>();
        studentEvents.add(new StudentEvent("РК1", 5));
        studentEvents.add(new StudentEvent("ДЗ1", 10));
        studentEvents.add(new StudentEvent("РК2", 15));
        studentEvents.add(new StudentEvent("ДЗ2", 20));
        studentEvents.add(new StudentEvent("РК3", 25));
        studentEvents.add(new StudentEvent("ДЗ3", 30));

        return studentEvents;
    }

    public ArrayList<StudentLesson> getStudentLessonsInModule(String studentName, String moduleTitle) {
        ArrayList<StudentLesson> studentLessonsInModule = new ArrayList<>();
        studentLessonsInModule.add(new StudentLesson(new Date(2020, 0, 1), 1));
        studentLessonsInModule.add(new StudentLesson(new Date(2020, 1, 2), 2));
        studentLessonsInModule.add(new StudentLesson(new Date(2020, 2, 3), 3));

        return studentLessonsInModule;
    }

    public HashMap<String, ArrayList<StudentLesson>> getStudentLessonsByModules(String studentName, String subject, String group) {
        HashMap<String, ArrayList<StudentLesson>> studentLessonsByModules = new HashMap<>();
        ArrayList<String> modules = getModules();
        for (int i = 0; i < modules.size(); i++) {
            ArrayList<StudentLesson> studentLessons = getStudentLessonsInModule(studentName, modules.get(i));
            studentLessonsByModules.put(modules.get(i), studentLessons);
        }

        return studentLessonsByModules;
    }

    public ArrayList<Student> getStudentsInGroup(String group) {
        ArrayList<Student> studentsInGroup = new ArrayList<>();
        studentsInGroup.add(new Student(1, "1ИУ9-11", "1Александр", "1Другаков"));
        studentsInGroup.add(new Student(2, "2ИУ9-21", "2Александр", "2Другаков"));
        studentsInGroup.add(new Student(3, "3ИУ9-31", "3Александр", "3Другаков"));

        return studentsInGroup;
    }

/*   public ArrayList<String> getEvents(String subject, String group) {
        ArrayList<String> events = new ArrayList<>();
        events.add("РК1");
        events.add("ДЗ1");
        events.add("РК2");
        events.add("ДЗ2");
        events.add("РК3");
        events.add("ДЗ3");

        return events;
    }

     public ArrayList<Date> getLessonsInModule(String moduleTitle) {
        ArrayList<Date> lessonsInModule = new ArrayList<>();
        lessonsInModule.add(new Date(2019, 0, 1));
        lessonsInModule.add(new Date(2019, 1, 2));
        lessonsInModule.add(new Date(2019, 2, 3));

        return lessonsInModule;
    }

      public HashMap<String, ArrayList<Date>> getLessonsByModules(String subject, String group) {
        HashMap<String, ArrayList<Date>> lessonsByModules = new HashMap<>();
        ArrayList<String> modules = getModules();
        for (int i = 0; i < modules.size(); i++) {
            ArrayList<Date> lessons = getLessonsInModule(modules.get(i));
            lessonsByModules.put(modules.get(i), lessons);
        }

        return lessonsByModules;
    }*/

    public ArrayList<ArrayList<StudentEvent>> getStudentsEvents(String subject, String group) {
        ArrayList<ArrayList<StudentEvent>> studentsEvents = new ArrayList<>();
        ArrayList<Student> students = getStudentsInGroup(group);
        for (int i = 0; i < students.size(); i++) {
            studentsEvents.add(getStudentEvents(students.get(i).getFullName(), subject, group));
        }

        return studentsEvents;
    }

    public HashMap<String, ArrayList<ArrayList<StudentLesson>>> getStudentsLessonsByModules(String subject, String group) {
        HashMap<String, ArrayList<ArrayList<StudentLesson>>> studentsLessonsByModules = new HashMap<>();
        ArrayList<ArrayList<StudentLesson>> firstModule = new ArrayList<>();
        ArrayList<ArrayList<StudentLesson>> secondModule = new ArrayList<>();
        ArrayList<ArrayList<StudentLesson>> thirdModule = new ArrayList<>();

        ArrayList<String> modules = getModules();
        ArrayList<Student> students = getStudentsInGroup(group);
        for (int i = 0; i < students.size(); i++) {
            HashMap<String, ArrayList<StudentLesson>> studentLessonsByModules =
                    getStudentLessonsByModules(students.get(i).getFullName(), subject, group);
            firstModule.add(studentLessonsByModules.get(modules.get(0)));
            secondModule.add(studentLessonsByModules.get(modules.get(1)));
            thirdModule.add(studentLessonsByModules.get(modules.get(2)));
        }

        studentsLessonsByModules.put(modules.get(0), firstModule);
        studentsLessonsByModules.put(modules.get(1), secondModule);
        studentsLessonsByModules.put(modules.get(2), thirdModule);
        return studentsLessonsByModules;
    }

    public void addGroup(String groupTitle) {
        //
    }

    public void addSemester(String semesterYear) {
        //
    }

    public void addSubject(String subjectTitle) {
        //
    }

    public void addProfessor(String professorName, String professorSecondName, String professorLogin, String professorPassword) {
        //
    }

    public Integer getLastStudentId() {
        Integer cached = cache.getLastStudentId();
        if (cached != null && !lastStudentIdUpdate) {
            return cached;
        }

        Integer lastStudentId = new Random().nextInt(1000);
        cache.setLastStudentId(lastStudentId);
        return lastStudentId;
    }

    public Integer getLastProfessorId() {
        Integer cached = cache.getLastProfessorId();
        if (cached != null && !lastProfessorIdUpdate) {
            return cached;
        }

        Integer lastProfessorId = new Random().nextInt(1000);
        cache.setLastProfessorId(lastProfessorId);
        return lastProfessorId;
    }
}