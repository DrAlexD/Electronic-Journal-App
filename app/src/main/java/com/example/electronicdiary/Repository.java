package com.example.electronicdiary;

import com.example.electronicdiary.data_classes.Event;
import com.example.electronicdiary.data_classes.Group;
import com.example.electronicdiary.data_classes.Lesson;
import com.example.electronicdiary.data_classes.ModuleInfo;
import com.example.electronicdiary.data_classes.Professor;
import com.example.electronicdiary.data_classes.Semester;
import com.example.electronicdiary.data_classes.Student;
import com.example.electronicdiary.data_classes.StudentEvent;
import com.example.electronicdiary.data_classes.StudentLesson;
import com.example.electronicdiary.data_classes.StudentPerformanceInModule;
import com.example.electronicdiary.data_classes.StudentPerformanceInSubject;
import com.example.electronicdiary.data_classes.Subject;
import com.example.electronicdiary.data_classes.SubjectInfo;
import com.example.electronicdiary.data_classes.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Repository {
    private static volatile Repository repository;
    private final Webservice webservice = new Retrofit.Builder().baseUrl("https://api.github.com/").
            addConverterFactory(GsonConverterFactory.create()).build().create(Webservice.class);
    public boolean allStudentsUpdate = false;
    public boolean professorsUpdate = false;
    public boolean allGroupsUpdate = false;
    public boolean allSubjectsUpdate = false;
    public boolean semestersUpdate = false;
    public boolean availableStudentsUpdate = false;
    public boolean availableSubjectsUpdate = false;
    public boolean availableSubjectsWithGroupsUpdate = false;
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

    public void setLastLoggedInUser(int userId, boolean isUserProfessor) {
        if (isUserProfessor)
            cache.setUser(getProfessorById(userId));
        else
            cache.setUser(getStudentById(userId));
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

    public ArrayList<Integer> getModules() {
        ArrayList<Integer> modules = new ArrayList<>();
        modules.add(1);
        modules.add(2);
        modules.add(3);

        return modules;
    }

    public int getLastStudentId() {
        Integer cached = cache.getLastStudentId();
        if (cached != null && !lastStudentIdUpdate) {
            return cached;
        }

        ArrayList<Student> students = getAllStudents();
        int lastStudentId = students.get(students.size() - 1).getId() + 1;
        cache.setLastStudentId(lastStudentId);
        return lastStudentId;
    }

    public int getLastProfessorId() {
        Integer cached = cache.getLastProfessorId();
        if (cached != null && !lastProfessorIdUpdate) {
            return cached;
        }

        ArrayList<Professor> professors = getProfessors();
        int lastProfessorId = professors.get(professors.size() - 1).getId() + 1;
        cache.setLastProfessorId(lastProfessorId);
        return lastProfessorId;
    }

    public int getLastSemesterId() {
        ArrayList<Semester> semesters = getSemesters();

        return semesters.get(semesters.size() - 1).getId();
    }

    public ArrayList<Student> getAllStudents() {
        ArrayList<Student> cached = cache.getAllStudents();
        if (cached != null && !allStudentsUpdate) {
            return cached;
        }

        ArrayList<Student> allStudents = new ArrayList<>();
        allStudents.add(new Student(1, "1Александр", "1Другаков", new Group(1, "1ИУ9-11")));
        allStudents.add(new Student(2, "2Александр", "2Другаков", new Group(1, "1ИУ9-11")));
        allStudents.add(new Student(3, "3Александр", "3Другаков", new Group(2, "2ИУ9-21")));
        allStudents.add(new Student(4, "4Александр", "4Другаков", new Group(2, "2ИУ9-21")));
        allStudents.add(new Student(5, "5Александр", "5Другаков", new Group(3, "3ИУ9-31")));
        allStudents.add(new Student(6, "6Александр", "6Другаков", new Group(3, "3ИУ9-31")));
        allStudents.add(new Student(7, "7Александр", "7Другаков", new Group(4, "4ИУ9-41")));
        allStudents.add(new Student(8, "8Александр", "8Другаков", new Group(4, "4ИУ9-41")));
        allStudents.add(new Student(9, "9Александр", "9Другаков", new Group(4, "4ИУ9-41")));

        cache.setAllStudents(allStudents);
        return allStudents;
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

    public ArrayList<Group> getAllGroups() {
        ArrayList<Group> cached = cache.getAllGroups();
        if (cached != null && !allGroupsUpdate) {
            return cached;
        }

        ArrayList<Group> allGroups = new ArrayList<>();
        allGroups.add(new Group(1, "1ИУ9-11"));
        allGroups.add(new Group(2, "2ИУ9-21"));
        allGroups.add(new Group(3, "3ИУ9-31"));
        allGroups.add(new Group(4, "4ИУ9-41"));
        allGroups.add(new Group(5, "5ИУ9-51"));
        allGroups.add(new Group(6, "6ИУ9-61"));
        allGroups.add(new Group(7, "7ИУ9-71"));
        allGroups.add(new Group(8, "8ИУ9-81"));
        allGroups.add(new Group(9, "9ИУ9-91"));

        cache.setAllGroups(allGroups);
        return allGroups;
    }

    public ArrayList<Subject> getAllSubjects() {
        ArrayList<Subject> cached = cache.getAllSubjects();
        if (cached != null && !allSubjectsUpdate) {
            return cached;
        }

        ArrayList<Subject> allSubjects = new ArrayList<>();
        allSubjects.add(new Subject(1, "Матан"));
        allSubjects.add(new Subject(2, "Мобилки"));
        allSubjects.add(new Subject(3, "Алгебра"));
        allSubjects.add(new Subject(4, "Операционки"));
        allSubjects.add(new Subject(5, "Моделирование"));
        allSubjects.add(new Subject(6, "Методы оптимизации"));

        cache.setAllSubjects(allSubjects);
        return allSubjects;
    }

    public ArrayList<Semester> getSemesters() {
        ArrayList<Semester> cached = cache.getSemesters();
        if (cached != null && !semestersUpdate) {
            return cached;
        }

        ArrayList<Semester> semesters = new ArrayList<>();
        semesters.add(new Semester(1, 2018, true));
        semesters.add(new Semester(2, 2018, false));
        semesters.add(new Semester(3, 2019, true));
        semesters.add(new Semester(4, 2019, false));
        semesters.add(new Semester(5, 2020, true));
        semesters.add(new Semester(6, 2020, false));
        semesters.add(new Semester(7, 2021, true));

        cache.setSemesters(semesters);
        return semesters;
    }

    public ArrayList<Student> getStudentsInGroup(int groupId) {
        ArrayList<Student> studentsInGroup = new ArrayList<>();
        if (groupId == 1) {
            studentsInGroup.add(new Student(1, "1Александр", "1Другаков", new Group(1, "1ИУ9-11")));
            studentsInGroup.add(new Student(2, "2Александр", "2Другаков", new Group(1, "1ИУ9-11")));
        } else if (groupId == 2) {
            studentsInGroup.add(new Student(3, "3Александр", "3Другаков", new Group(2, "2ИУ9-21")));
            studentsInGroup.add(new Student(4, "4Александр", "4Другаков", new Group(2, "2ИУ9-21")));
        } else if (groupId == 3) {
            studentsInGroup.add(new Student(5, "5Александр", "5Другаков", new Group(3, "3ИУ9-31")));
            studentsInGroup.add(new Student(6, "6Александр", "6Другаков", new Group(3, "3ИУ9-31")));
        } else if (groupId == 4) {
            studentsInGroup.add(new Student(7, "7Александр", "7Другаков", new Group(4, "4ИУ9-41")));
            studentsInGroup.add(new Student(8, "8Александр", "8Другаков", new Group(4, "4ИУ9-41")));
            studentsInGroup.add(new Student(9, "9Александр", "9Другаков", new Group(4, "4ИУ9-41")));
        }

        return studentsInGroup;
    }

    public ArrayList<Student> getAvailableStudents(int semesterId) {
        ArrayList<Student> cached = cache.getAvailableStudents();
        if (cached != null && !availableStudentsUpdate) {
            return cached;
        }

        ArrayList<Student> availableStudents = new ArrayList<>();
        availableStudents.add(new Student(1, "1Александр", "1Другаков", new Group(1, "1ИУ9-11")));
        availableStudents.add(new Student(2, "2Александр", "2Другаков", new Group(1, "1ИУ9-11")));
        availableStudents.add(new Student(3, "3Александр", "3Другаков", new Group(2, "2ИУ9-21")));
        availableStudents.add(new Student(4, "4Александр", "4Другаков", new Group(2, "2ИУ9-21")));
        availableStudents.add(new Student(5, "5Александр", "5Другаков", new Group(3, "3ИУ9-31")));
        availableStudents.add(new Student(6, "6Александр", "6Другаков", new Group(3, "3ИУ9-31")));

        cache.setAvailableStudents(availableStudents);
        return availableStudents;
    }

    public ArrayList<Subject> getAvailableSubjects(int semesterId) {
        ArrayList<Subject> cached = cache.getAvailableSubjects();
        if (cached != null && !availableSubjectsUpdate) {
            return cached;
        }

        ArrayList<Subject> availableSubjects = new ArrayList<>();
        availableSubjects.add(new Subject(1, "Матан"));
        availableSubjects.add(new Subject(2, "Мобилки"));
        availableSubjects.add(new Subject(3, "Алгебра"));

        cache.setAvailableSubjects(availableSubjects);
        return availableSubjects;
    }

    public ArrayList<SubjectInfo> getAvailableGroupsInSubject(int professorId, int subjectId, int semesterId) {
        ArrayList<SubjectInfo> availableGroupsInSubject = new ArrayList<>();
        if (subjectId == 1) {
            availableGroupsInSubject.add(new SubjectInfo(new Group(1, "1ИУ9-11"), subjectId, professorId, professorId, semesterId, false, false));
            availableGroupsInSubject.add(new SubjectInfo(new Group(2, "2ИУ9-21"), subjectId, professorId, professorId, semesterId, true, false));
            availableGroupsInSubject.add(new SubjectInfo(new Group(3, "31ИУ9-31"), subjectId, professorId, professorId, semesterId, true, false));
        } else if (subjectId == 2) {
            availableGroupsInSubject.add(new SubjectInfo(new Group(4, "4ИУ9-41"), subjectId, professorId, professorId, semesterId, true, false));
            availableGroupsInSubject.add(new SubjectInfo(new Group(5, "5ИУ9-51"), subjectId, professorId, professorId, semesterId, false, false));
            availableGroupsInSubject.add(new SubjectInfo(new Group(6, "6ИУ9-61"), subjectId, professorId, professorId, semesterId, true, false));
        } else if (subjectId == 3) {
            availableGroupsInSubject.add(new SubjectInfo(new Group(7, "7ИУ9-71"), subjectId, professorId, professorId, semesterId, true, false));
            availableGroupsInSubject.add(new SubjectInfo(new Group(8, "8ИУ9-81"), subjectId, professorId, professorId, semesterId, true, false));
            availableGroupsInSubject.add(new SubjectInfo(new Group(9, "9ИУ9-91"), subjectId, professorId, professorId, semesterId, false, false));
        }

        return availableGroupsInSubject;
    }

    public HashMap<Subject, ArrayList<SubjectInfo>> getAvailableSubjectsWithGroups(int professorId, int semesterId) {
        HashMap<Subject, ArrayList<SubjectInfo>> cached = cache.getAvailableSubjectsWithGroups();
        if (cached != null && !availableSubjectsWithGroupsUpdate) {
            return cached;
        }

        HashMap<Subject, ArrayList<SubjectInfo>> availableSubjectsWithGroups = new HashMap<>();
        ArrayList<Subject> availableSubjects = getAvailableSubjects(semesterId);
        for (int i = 0; i < availableSubjects.size(); i++) {
            ArrayList<SubjectInfo> groups = getAvailableGroupsInSubject(professorId, availableSubjects.get(i).getId(), semesterId);
            availableSubjectsWithGroups.put(availableSubjects.get(i), groups);
        }

        cache.setAvailableSubjectsWithGroups(availableSubjectsWithGroups);
        return availableSubjectsWithGroups;
    }

    public ArrayList<Subject> getAvailableStudentSubjects(int studentId, int semesterId) {
        ArrayList<Subject> availableStudentSubjects = new ArrayList<>();
        if (studentId == 1) {
            availableStudentSubjects.add(new Subject(1, "Матан"));
            availableStudentSubjects.add(new Subject(2, "Мобилки"));
        } else if (studentId == 2) {
            availableStudentSubjects.add(new Subject(1, "Матан"));
            availableStudentSubjects.add(new Subject(3, "Алгебра"));
        } else if (studentId == 3) {
            availableStudentSubjects.add(new Subject(2, "Мобилки"));
            availableStudentSubjects.add(new Subject(3, "Алгебра"));
        }

        return availableStudentSubjects;
    }

    public HashMap<Integer, ModuleInfo> getModuleInfo(int groupId, int subjectId, int lecturerId, int seminarianId, int semesterId) {
        HashMap<Integer, ModuleInfo> modulesInfo = new HashMap<>();
        ArrayList<Integer> modules = getModules();

        modulesInfo.put(modules.get(0), new ModuleInfo(modules.get(0), groupId, subjectId, lecturerId, seminarianId, semesterId, 23, 35));
        modulesInfo.put(modules.get(1), new ModuleInfo(modules.get(1), groupId, subjectId, lecturerId, seminarianId, semesterId, 18, 30));
        modulesInfo.put(modules.get(2), new ModuleInfo(modules.get(2), groupId, subjectId, lecturerId, seminarianId, semesterId, 23, 35));

        return modulesInfo;
    }

    public HashMap<Integer, ArrayList<Event>> getEvents(int groupId, int subjectId, int lecturerId, int seminarianId, int semesterId) {
        HashMap<Integer, ArrayList<Event>> events = new HashMap<>();
        ArrayList<Integer> modules = getModules();
        ArrayList<Event> events1 = new ArrayList<>();

        events1.add(new Event(1, modules.get(0), 1, 1, 2, 3, 7, "РК",
                1, new Date(2021, 0, 1), new Date(2021, 1, 1), 10, 20));
        events1.add(new Event(2, modules.get(0), 1, 1, 2, 3, 7, "ДЗ",
                1, new Date(2021, 5, 1), new Date(2021, 6, 1), 10, 20));
        ArrayList<Event> events2 = new ArrayList<>();
        events2.add(new Event(3, modules.get(1), 1, 1, 2, 3, 7, "РК",
                2, new Date(2021, 3, 1), new Date(2021, 4, 1), 10, 20));
        events2.add(new Event(4, modules.get(1), 1, 1, 2, 3, 7, "ДЗ",
                2, new Date(2021, 2, 1), new Date(2021, 3, 1), 10, 20));
        ArrayList<Event> events3 = new ArrayList<>();
        events3.add(new Event(5, modules.get(2), 1, 1, 2, 3, 7, "РК",
                3, new Date(2021, 4, 1), new Date(2021, 5, 1), 10, 20));
        events3.add(new Event(6, modules.get(2), 1, 1, 2, 3, 7, "ДЗ",
                3, new Date(2021, 1, 1), new Date(2021, 2, 1), 10, 20));

        events.put(modules.get(0), events1);
        events.put(modules.get(1), events2);
        events.put(modules.get(2), events3);
        return events;
    }

    public HashMap<Integer, ArrayList<Lesson>> getLessons(int groupId, int subjectId, int lecturerId, int seminarianId, int semesterId) {
        HashMap<Integer, ArrayList<Lesson>> lessons = new HashMap<>();
        ArrayList<Integer> modules = getModules();
        ArrayList<Lesson> lessons1 = new ArrayList<>();
        lessons1.add(new Lesson(1, modules.get(0), 1, 1, 2, 3,
                7, new Date(2021, 0, 15, 15, 20), true, 1));
        lessons1.add(new Lesson(2, modules.get(0), 1, 1, 2, 3,
                7, new Date(2021, 1, 15, 15, 20), false, 2));
        ArrayList<Lesson> lessons2 = new ArrayList<>();
        lessons2.add(new Lesson(3, modules.get(1), 1, 1, 2, 3,
                7, new Date(2021, 2, 15, 15, 20), false, 1));
        lessons2.add(new Lesson(4, modules.get(1), 1, 1, 2, 3,
                7, new Date(2021, 3, 15, 15, 20), true, 3));
        ArrayList<Lesson> lessons3 = new ArrayList<>();
        lessons3.add(new Lesson(5, modules.get(2), 1, 1, 2, 3,
                7, new Date(2021, 4, 15, 15, 20), true, 4));
        lessons3.add(new Lesson(6, modules.get(2), 1, 1, 2, 3,
                7, new Date(2021, 5, 15, 15, 20), true, 1));

        lessons.put(modules.get(0), lessons1);
        lessons.put(modules.get(1), lessons2);
        lessons.put(modules.get(2), lessons3);
        return lessons;
    }

    public HashMap<Integer, ArrayList<Lesson>> getLectures(int groupId, int subjectId, int lecturerId, int seminarianId, int semesterId) {
        return getSpecificLessons(groupId, subjectId, lecturerId, seminarianId, semesterId, true);
    }

    public HashMap<Integer, ArrayList<Lesson>> getSeminars(int groupId, int subjectId, int lecturerId, int seminarianId, int semesterId) {
        return getSpecificLessons(groupId, subjectId, lecturerId, seminarianId, semesterId, false);
    }

    public HashMap<Integer, ArrayList<Lesson>> getSpecificLessons(int groupId, int subjectId, int lecturerId, int seminarianId, int semesterId, boolean isLecture) {
        HashMap<Integer, ArrayList<Lesson>> studentsSpecificLessons = new HashMap<>();
        HashMap<Integer, ArrayList<Lesson>> studentsLessons = getLessons(groupId, subjectId, lecturerId, seminarianId, semesterId);

        ArrayList<Integer> modules = getModules();

        ArrayList<Lesson> lessons1 = new ArrayList<>();
        for (Lesson lesson : studentsLessons.get(modules.get(0))) {
            if (lesson.isLecture() == isLecture) {
                lessons1.add(lesson);
            }
        }

        ArrayList<Lesson> lessons2 = new ArrayList<>();
        for (Lesson lesson : studentsLessons.get(modules.get(1))) {
            if (lesson.isLecture()) {
                lessons2.add(lesson);
            }
        }

        ArrayList<Lesson> lessons3 = new ArrayList<>();
        for (Lesson lesson : studentsLessons.get(modules.get(2))) {
            if (lesson.isLecture()) {
                lessons3.add(lesson);
            }
        }
        studentsSpecificLessons.put(modules.get(0), lessons1);
        studentsSpecificLessons.put(modules.get(1), lessons2);
        studentsSpecificLessons.put(modules.get(2), lessons3);

        return studentsSpecificLessons;
    }

    public ArrayList<StudentPerformanceInSubject> getStudentsPerformancesInSubject(int groupId, int subjectId, int lecturerId, int seminarianId, int semesterId) {
        ArrayList<StudentPerformanceInSubject> studentsPerformances = new ArrayList<>();

        studentsPerformances.add(new StudentPerformanceInSubject(1, groupId, subjectId, lecturerId, seminarianId, semesterId, 0, 0, 0, false, 0, 0));
        studentsPerformances.add(new StudentPerformanceInSubject(2, groupId, subjectId, lecturerId, seminarianId, semesterId, 0, 10, 2, true, 0, 0));

        return studentsPerformances;
    }

    public HashMap<Integer, StudentPerformanceInModule> getStudentPerformanceInModules(int studentId, int groupId, int subjectId, int lecturerId, int seminarianId, int semesterId) {
        HashMap<Integer, StudentPerformanceInModule> studentPerformanceInModule = new HashMap<>();
        ArrayList<Integer> modules = getModules();

        if (studentId == 1) {
            studentPerformanceInModule.put(modules.get(0), new StudentPerformanceInModule(modules.get(0), studentId, groupId, subjectId, lecturerId, seminarianId, semesterId, 15, false));
            studentPerformanceInModule.put(modules.get(1), new StudentPerformanceInModule(modules.get(1), studentId, groupId, subjectId, lecturerId, seminarianId, semesterId, 16, false));
            studentPerformanceInModule.put(modules.get(2), new StudentPerformanceInModule(modules.get(2), studentId, groupId, subjectId, lecturerId, seminarianId, semesterId, 17, false));
        } else if (studentId == 2) {
            studentPerformanceInModule.put(modules.get(0), new StudentPerformanceInModule(modules.get(0), studentId, groupId, subjectId, lecturerId, seminarianId, semesterId, 18, true));
            studentPerformanceInModule.put(modules.get(1), new StudentPerformanceInModule(modules.get(1), studentId, groupId, subjectId, lecturerId, seminarianId, semesterId, 19, true));
            studentPerformanceInModule.put(modules.get(2), new StudentPerformanceInModule(modules.get(2), studentId, groupId, subjectId, lecturerId, seminarianId, semesterId, 20, true));
        } else if (studentId == 3) {
            studentPerformanceInModule.put(modules.get(0), new StudentPerformanceInModule(modules.get(0), studentId, groupId, subjectId, lecturerId, seminarianId, semesterId, 21, true));
            studentPerformanceInModule.put(modules.get(1), new StudentPerformanceInModule(modules.get(1), studentId, groupId, subjectId, lecturerId, seminarianId, semesterId, 22, true));
            studentPerformanceInModule.put(modules.get(2), new StudentPerformanceInModule(modules.get(2), studentId, groupId, subjectId, lecturerId, seminarianId, semesterId, 23, true));
        }

        return studentPerformanceInModule;
    }

    public HashMap<Integer, ArrayList<StudentPerformanceInModule>> getStudentsPerformancesInModules(int groupId, int subjectId, int lecturerId, int seminarianId, int semesterId) {
        HashMap<Integer, ArrayList<StudentPerformanceInModule>> studentsLessons = new HashMap<>();
        ArrayList<StudentPerformanceInModule> firstModule = new ArrayList<>();
        ArrayList<StudentPerformanceInModule> secondModule = new ArrayList<>();
        ArrayList<StudentPerformanceInModule> thirdModule = new ArrayList<>();

        ArrayList<Integer> modules = getModules();
        ArrayList<Student> students = getStudentsInGroup(groupId);
        for (int i = 0; i < students.size(); i++) {
            HashMap<Integer, StudentPerformanceInModule> studentPerformanceInModules =
                    getStudentPerformanceInModules(students.get(i).getId(), groupId, subjectId, lecturerId, seminarianId, semesterId);
            firstModule.add(studentPerformanceInModules.get(modules.get(0)));
            secondModule.add(studentPerformanceInModules.get(modules.get(1)));
            thirdModule.add(studentPerformanceInModules.get(modules.get(2)));
        }

        studentsLessons.put(modules.get(0), firstModule);
        studentsLessons.put(modules.get(1), secondModule);
        studentsLessons.put(modules.get(2), thirdModule);
        return studentsLessons;
    }

    public ArrayList<StudentEvent> getStudentEventsInModule(int studentId, int moduleNumber, int subjectId, int lecturerId, int seminarianId, int semesterId) {
        ArrayList<StudentEvent> studentEventsInModule = new ArrayList<>();
        if (studentId == 1) {
            if (moduleNumber == 1) {
                studentEventsInModule.add(new StudentEvent(1, 1, 1, 1, 1, 1, 2, 3, 7, true, 1));
                studentEventsInModule.add(new StudentEvent(1, 2, 1, 1, 1, 1, 2, 3, 7, true, 1));
            } else if (moduleNumber == 2) {
                studentEventsInModule.add(new StudentEvent(1, 3, 2, 1, 1, 1, 2, 3, 7, true, 1));
                studentEventsInModule.add(new StudentEvent(1, 4, 2, 1, 1, 1, 2, 3, 7, true, 1));
            } else if (moduleNumber == 3) {
                studentEventsInModule.add(new StudentEvent(1, 5, 3, 1, 1, 1, 2, 3, 7, true, 1));
                studentEventsInModule.add(new StudentEvent(1, 6, 3, 1, 1, 1, 2, 3, 7, true, 1));
            }
        } else if (studentId == 2) {
            if (moduleNumber == 1) {
                studentEventsInModule.add(new StudentEvent(1, 1, 1, 2, 1, 1, 2, 3, 7, true, 2));
                studentEventsInModule.add(new StudentEvent(1, 2, 1, 2, 1, 1, 2, 3, 7, true, 2));
            } else if (moduleNumber == 2) {
                studentEventsInModule.add(new StudentEvent(1, 3, 2, 2, 1, 1, 2, 3, 7, true, 2));
                studentEventsInModule.add(new StudentEvent(1, 4, 2, 2, 1, 1, 2, 3, 7, true, 2));
            } else if (moduleNumber == 3) {
                studentEventsInModule.add(new StudentEvent(1, 5, 3, 2, 1, 1, 2, 3, 7, true, 2));
                studentEventsInModule.add(new StudentEvent(1, 6, 3, 2, 1, 1, 2, 3, 7, true, 2));
            }
        } else if (studentId == 3) {
            if (moduleNumber == 1) {
                studentEventsInModule.add(new StudentEvent(1, 7, 1, 3, 2, 1, 2, 3, 7, true, 3));
                studentEventsInModule.add(new StudentEvent(1, 8, 1, 3, 2, 1, 2, 3, 7, true, 3));
            } else if (moduleNumber == 2) {
                studentEventsInModule.add(new StudentEvent(1, 9, 2, 3, 2, 1, 2, 3, 7, true, 3));
                studentEventsInModule.add(new StudentEvent(1, 10, 2, 3, 2, 1, 2, 3, 7, true, 3));
            } else if (moduleNumber == 3) {
                studentEventsInModule.add(new StudentEvent(1, 11, 3, 3, 2, 1, 2, 3, 7, true, 3));
                studentEventsInModule.add(new StudentEvent(1, 12, 3, 3, 2, 1, 2, 3, 7, true, 3));
            }
        }

        return studentEventsInModule;
    }

    public HashMap<Integer, ArrayList<StudentEvent>> getStudentEvents(int studentId, int subjectId, int lecturerId, int seminarianId, int semesterId) { //moduleNumber?
        HashMap<Integer, ArrayList<StudentEvent>> studentEventsByModules = new HashMap<>();
        ArrayList<Integer> modules = getModules();
        for (int i = 0; i < modules.size(); i++) {
            ArrayList<StudentEvent> studentEvents = getStudentEventsInModule(studentId, modules.get(i), subjectId, lecturerId, seminarianId, semesterId);
            studentEventsByModules.put(modules.get(i), studentEvents);
        }

        return studentEventsByModules;
    }

    public ArrayList<StudentLesson> getStudentLessonsInModule(int studentId, int moduleNumber, int subjectId, int lecturerId, int seminarianId, int semesterId) {
        ArrayList<StudentLesson> studentLessonsInModule = new ArrayList<>();
        if (studentId == 1) {
            if (moduleNumber == 1) {
                studentLessonsInModule.add(new StudentLesson(1, 1, 1, 1, 1, 2, 3, 7, true, 1));
                studentLessonsInModule.add(new StudentLesson(2, 1, 1, 1, 1, 2, 3, 7, true, 1));
            } else if (moduleNumber == 2) {
                studentLessonsInModule.add(new StudentLesson(3, 2, 1, 1, 1, 2, 3, 7, true, 1));
                studentLessonsInModule.add(new StudentLesson(4, 2, 1, 1, 1, 2, 3, 7, true, 1));
            } else if (moduleNumber == 3) {
                studentLessonsInModule.add(new StudentLesson(5, 3, 1, 1, 1, 2, 3, 7, true, 1));
                studentLessonsInModule.add(new StudentLesson(6, 3, 1, 1, 1, 2, 3, 7, true, 1));
            }
        } else if (studentId == 2) {
            if (moduleNumber == 1) {
                studentLessonsInModule.add(new StudentLesson(1, 1, 2, 1, 1, 2, 3, 7, true, 2));
                studentLessonsInModule.add(new StudentLesson(2, 1, 2, 1, 1, 2, 3, 7, true, 2));
            } else if (moduleNumber == 2) {
                studentLessonsInModule.add(new StudentLesson(3, 2, 2, 1, 1, 2, 3, 7, true, 2));
                studentLessonsInModule.add(new StudentLesson(4, 2, 2, 1, 1, 2, 3, 7, true, 2));
            } else if (moduleNumber == 3) {
                studentLessonsInModule.add(new StudentLesson(5, 3, 2, 1, 1, 2, 3, 7, true, 2));
                studentLessonsInModule.add(new StudentLesson(6, 3, 2, 1, 1, 2, 3, 7, true, 2));
            }
        } else if (studentId == 3) {
            if (moduleNumber == 1) {
                studentLessonsInModule.add(new StudentLesson(1, 1, 3, 2, 1, 2, 3, 7, true, 3));
                studentLessonsInModule.add(new StudentLesson(2, 1, 3, 2, 1, 2, 3, 7, true, 3));
            } else if (moduleNumber == 2) {
                studentLessonsInModule.add(new StudentLesson(3, 2, 3, 2, 1, 2, 3, 7, true, 3));
                studentLessonsInModule.add(new StudentLesson(4, 2, 3, 2, 1, 2, 3, 7, true, 3));
            } else if (moduleNumber == 3) {
                studentLessonsInModule.add(new StudentLesson(5, 3, 3, 2, 1, 2, 3, 7, true, 3));
                studentLessonsInModule.add(new StudentLesson(6, 3, 3, 2, 1, 2, 3, 7, true, 3));
            }
        }

        return studentLessonsInModule;
    }

    public HashMap<Integer, ArrayList<StudentLesson>> getStudentLessons(int studentId, int subjectId, int lecturerId, int seminarianId, int semesterId) {
        HashMap<Integer, ArrayList<StudentLesson>> studentLessonsByModules = new HashMap<>();
        ArrayList<Integer> modules = getModules();
        for (int i = 0; i < modules.size(); i++) {
            ArrayList<StudentLesson> studentLessons = getStudentLessonsInModule(studentId, modules.get(i), subjectId, lecturerId, seminarianId, semesterId);
            studentLessonsByModules.put(modules.get(i), studentLessons);
        }

        return studentLessonsByModules;
    }

    public HashMap<Integer, ArrayList<ArrayList<StudentEvent>>> getStudentsEvents(int groupId, int subjectId, int lecturerId, int seminarianId, int semesterId) {
        HashMap<Integer, ArrayList<ArrayList<StudentEvent>>> studentsEvents = new HashMap<>();
        ArrayList<ArrayList<StudentEvent>> firstModule = new ArrayList<>();
        ArrayList<ArrayList<StudentEvent>> secondModule = new ArrayList<>();
        ArrayList<ArrayList<StudentEvent>> thirdModule = new ArrayList<>();

        ArrayList<Integer> modules = getModules();
        ArrayList<Student> students = getStudentsInGroup(groupId);
        for (int i = 0; i < students.size(); i++) {
            HashMap<Integer, ArrayList<StudentEvent>> studentEvents =
                    getStudentEvents(students.get(i).getId(), subjectId, lecturerId, seminarianId, semesterId);
            firstModule.add(studentEvents.get(modules.get(0)));
            secondModule.add(studentEvents.get(modules.get(1)));
            thirdModule.add(studentEvents.get(modules.get(2)));
        }

        studentsEvents.put(modules.get(0), firstModule);
        studentsEvents.put(modules.get(1), secondModule);
        studentsEvents.put(modules.get(2), thirdModule);

        return studentsEvents;
    }

    public HashMap<Integer, ArrayList<ArrayList<StudentLesson>>> getStudentsLessons(int groupId, int subjectId, int lecturerId, int seminarianId, int semesterId) {
        HashMap<Integer, ArrayList<ArrayList<StudentLesson>>> studentsLessons = new HashMap<>();
        ArrayList<ArrayList<StudentLesson>> firstModule = new ArrayList<>();
        ArrayList<ArrayList<StudentLesson>> secondModule = new ArrayList<>();
        ArrayList<ArrayList<StudentLesson>> thirdModule = new ArrayList<>();

        ArrayList<Integer> modules = getModules();
        ArrayList<Student> students = getStudentsInGroup(groupId);
        for (int i = 0; i < students.size(); i++) {
            HashMap<Integer, ArrayList<StudentLesson>> studentLessons =
                    getStudentLessons(students.get(i).getId(), subjectId, lecturerId, seminarianId, semesterId);
            firstModule.add(studentLessons.get(modules.get(0)));
            secondModule.add(studentLessons.get(modules.get(1)));
            thirdModule.add(studentLessons.get(modules.get(2)));
        }

        studentsLessons.put(modules.get(0), firstModule);
        studentsLessons.put(modules.get(1), secondModule);
        studentsLessons.put(modules.get(2), thirdModule);
        return studentsLessons;
    }

    public Group getGroupById(int groupId) {
        return new Group(groupId, groupId + "ИУ9-11");
    }

    public SubjectInfo getSubjectInfo(int groupId, int subjectId, int semesterId) {
        return new SubjectInfo(new Group(groupId, "1ИУ9-11"), subjectId, 2, 3, semesterId, true, false);
    }

    public SubjectInfo getSubjectInfo(int groupId, int subjectId, int lecturerId, int seminarianId, int semesterId) {
        return new SubjectInfo(new Group(groupId, "1ИУ9-11"), subjectId, lecturerId, seminarianId, semesterId, true, false);
    }

    public ModuleInfo getModuleInfoByNumber(int moduleNumber, int groupId, int subjectId, int lecturerId, int seminarianId, int semesterId) {
        return new ModuleInfo(moduleNumber, groupId, subjectId, lecturerId, seminarianId, semesterId, 23, 35);
    }

    public StudentPerformanceInSubject getStudentPerformanceInSubject(int studentId, int groupId, int subjectId, int lecturerId, int seminarianId, int semesterId) {
        return new StudentPerformanceInSubject(studentId, groupId, subjectId, lecturerId, seminarianId, semesterId, 0, 0, 0, false, 0, 0);
    }

    public Subject getSubjectById(int subjectId) {
        return new Subject(subjectId, "Матан");
    }

    public Student getStudentById(int studentId) {
        return new Student(studentId, studentId + "Александр", studentId + "Другаков", new Group(1, "1ИУ9-11"));
    }

    public Student getStudentByIdWithLogin(int studentId) {
        return new Student(studentId, studentId + "Александр", studentId + "Другаков", new Group(1, "1ИУ9-11"), "fdfdf", "aaaabbb");
    }

    public Professor getProfessorById(int professorId) {
        return new Professor(professorId, "Александр", "Коновалов");
    }

    public Professor getProfessorByIdWithLogin(int professorId) {
        return new Professor(professorId, "Александр", "Коновалов", "qwweetrt", "zzzxxx");
    }

    public Event getEventById(int eventId) {
        return new Event(eventId, 1, 1, 1, 2, 3,
                7, "РК", 1, new Date(2021, 0, 1),
                new Date(2021, 1, 1), 10, 15);
    }

    public Lesson getLessonById(int lessonId) {
        return new Lesson(lessonId, 1, 1, 1, 2, 3,
                7, new Date(2021, 0, 15, 15, 20), true, 1);
    }

    public Semester getSemesterById(int semesterId) {
        return new Semester(semesterId, 2021, true);
    }

    public StudentEvent getStudentEventById(int attemptNumber, int eventId, int studentId) {
        return new StudentEvent(attemptNumber, eventId, 1, studentId, 1, 1, 2, 3, 7, true, 1);
    }

    public StudentLesson getStudentLessonById(int lessonId, int studentId) {
        return new StudentLesson(lessonId, 1, studentId, 1, 1, 2, 3, 7, true, 1);
    }

    public void changeStudentGroup(int studentId, int newStudentGroupId) {
        Repository.getInstance().deleteStudent(studentId);
        Student student = getStudentById(studentId);
        Repository.getInstance().addStudent(student.getFirstName(), student.getSecondName(), newStudentGroupId,
                student.getLogin(), student.getPassword());
    }

    public void addStudent(String firstName, String secondName, int groupId, String login, String password) {
        //
    }

    public void addProfessor(String firstName, String secondName, String login, String password) {
        //
    }

    public void addGroup(String title) {
        //
    }

    public void addSubject(String title) {
        //
    }

    public void addAvailableSubject(int professorId, boolean isLecturer, boolean isSeminarian, int groupId,
                                    int subjectId, int semesterId, boolean isExam, boolean isDifferentiatedCredit) {
        //
    }

    public void addGroupInAvailableSubject(int lecturerId, int seminarianId, int groupId, int subjectId, int semesterId, boolean isExam) {
        //
    }

    public void addSemester(int year, boolean isFirstHalf) {
        //
    }

    public void addEvent(int moduleNumber, int groupId, int subjectId, int lecturerId, int seminarianId, int semesterId,
                         String type, Date startDate, Date deadlineDate, int minPoints, int maxPoints) {
        //
    }

    public void addLesson(int moduleNumber, int groupId, int subjectId, int lecturerId, int seminarianId, int semesterId,
                          Date dateAndTime, boolean isLecture, int pointsPerVisit) {
        //
    }

    public void addStudentEvent(int attemptNumber, int eventId, int studentId, int moduleNumber, int groupId, int subjectId,
                                int lecturerId, int seminarianId, int semesterId, boolean isAttended, int variantNumber) {
        //
    }

    public void addStudentLesson(int lessonId, int moduleNumber, int studentId, int groupId, int subjectId, int lecturerId,
                                 int seminarianId, int semesterId, boolean isAttended) {
        //
    }

    public void editStudent(int id, String firstName, String secondName, String login, String password) {
        //
    }

    public void editProfessor(int id, String firstName, String secondName, String login, String password) {
        //
    }

    public void editGroup(int id, String title) {
        //
    }

    public void editModuleInfo(int moduleNumber, int groupId, int subjectId, int lecturerId, int seminarianId, int semesterId, int minPoints, int maxPoints) {
        //
    }

    public void editSubjectInfo(int groupId, int subjectId, int lecturerId, int seminarianId, int semesterId,
                                boolean isSwapLecturerAndSeminarian, boolean isExam, boolean isDifferentiatedCredit, boolean isForAllGroups) {
        //
    }

    public void editStudentPerformanceInSubject(int studentId, int groupId, int subjectId, int lecturerId, int seminarianId, int semesterId, int earnedPoints, int bonusPoints,
                                                boolean isHaveCreditOrAdmission, int earnedExamPoints, int mark) {
        //
    }

    public void editStudentPerformanceInSubject(int studentId, int groupId, int subjectId, int lecturerId, int seminarianId, int semesterId, int earnedPoints, int bonusPoints,
                                                boolean isHaveCreditOrAdmission) {
        //
    }

    public void editSubject(int id, String title) {
        //
    }

    public void editSemester(int id, int year, boolean isFirstHalf) {
        //
    }

    public void editEvent(int eventId, Date startDate, Date deadlineDate, int minPoints, int maxPoints) {
        //
    }

    public void editLesson(int lessonId, Date dateAndTime, boolean isLecture, int pointsPerVisit) {
        //
    }

    public void editStudentEvent(int attemptNumber, int eventId, int studentId, boolean isAttended, int variantNumber,
                                 Date finishDate, int earnedPoints, int bonusPoints, boolean isHaveCredit) {
        //
    }

    public void editStudentLesson(int lessonId, int studentId, boolean isAttended, int bonusPoints) {
        //
    }

    public void deleteStudent(int studentId) {
        //
    }

    public void deleteProfessor(int professorId) {
        //
    }

    public void deleteGroup(int groupId) {
        //
    }

    public void deleteSubject(int subjectId) {
        //
    }

    public void deleteAvailableSubject(int professorId, int subjectId, int semesterId) {
        //
    }

    public void deleteGroupInAvailableSubject(int professorId, int groupId, int subjectId, int semesterId) {
        //
    }

    public void deleteSemester(int semesterId) {
        //
    }

    public void deleteEvent(int eventId) {
        //
    }

    public void deleteLesson(int lessonId) {
        //
    }

    public void deleteStudentEvent(int attemptNumber, int eventId, int studentId) {
        //
    }

    public void deleteStudentLesson(int lessonId, int studentId) {
        //
    }
}