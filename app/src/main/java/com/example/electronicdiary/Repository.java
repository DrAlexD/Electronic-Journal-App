package com.example.electronicdiary;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Repository {
    private static volatile Repository repository;
    private final Webservice webservice = new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/api/").
            addConverterFactory(GsonConverterFactory.create()).build().create(Webservice.class);
    public boolean allStudentsUpdate = false;
    public boolean professorsUpdate = false;
    public boolean allGroupsUpdate = false;
    public boolean allSubjectsUpdate = false;
    public boolean semestersUpdate = false;
    public boolean availableStudentsUpdate = false;
    public boolean availableSubjectsUpdate = false;
    public boolean availableSubjectsWithGroupsUpdate = false;
    private String headerAuthorization;
    //            "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJxd2VydHkiLCJpYXQiOjE2MjAwNDQ2MTksImV4cCI6MTYyMDEzMTAxOX0.J_OPc06XGCFBG1GU9TnDpOPJPJpSc9iOUVF8Gtm2Hg3cjoqnqSWxhp7QbKcpIU4S_IiU_SVObIUBS4rsw7Rn6g";
    private Cache cache = new Cache();
    private User user = null;

    private Repository() {
    }

    public static Repository getInstance() {
        if (repository == null) {
            repository = new Repository();
        }
        return repository;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void getLoggedUser(JwtResponse jwtResponse, MutableLiveData<? extends User> user) {
        headerAuthorization = "Bearer " + jwtResponse.getToken();
        if (jwtResponse.isProfessor()) {
            getProfessorById(jwtResponse.getId(), (MutableLiveData<Professor>) user);
        } else {
            getStudentById(jwtResponse.getId(), (MutableLiveData<Student>) user);
        }
    }

    public void logout() {
        cache = new Cache();
        user = null;
    }

    public void login(String username, String password, MutableLiveData<Result<JwtResponse>> result) {
        webservice.authenticateUser(new LoginRequest(username, password)).enqueue(new Callback<JwtResponse>() {
            @Override
            public void onResponse(Call<JwtResponse> call, Response<JwtResponse> response) {
                if (response.isSuccessful()) {
                    result.postValue(new Result.Success<>(response.body()));
                } else {
                    result.postValue(new Result.Error("Login failed: " + response.code()));
                    Log.e("ERROR_CODE", String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<JwtResponse> call, Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    public List<Integer> getModules() {
        List<Integer> modules = new ArrayList<>();
        modules.add(1);
        modules.add(2);
        modules.add(3);

        return modules;
    }

    public long getLastStudentId() {
        /*List<Student> students = getAllStudents();
        return students.get(students.size() - 1).getId() + 1;*/
        return 5;
    }

    public long getLastProfessorId() {
        /*List<Professor> professors = getProfessors();
        return professors.get(professors.size() - 1).getId() + 1;*/
        return 5;
    }

    public long getLastSemesterId() {
        /*List<Semester> semesters = getSemesters();

        return semesters.get(semesters.size() - 1).getId();*/
        return 1;
    }

    public void getAllStudents(MutableLiveData<List<Student>> allStudents) {
        List<Student> cached = cache.getAllStudents();
        if (cached != null && !allStudentsUpdate) {
            allStudents.setValue(cached);
        }

        webservice.getAllStudents(headerAuthorization).enqueue(new Callback<List<Student>>() {
            @Override
            public void onResponse(Call<List<Student>> call, Response<List<Student>> response) {
                if (response.isSuccessful()) {
                    allStudentsUpdate = false;
                    allStudents.postValue(response.body());
                    cache.setAllStudents(response.body());
                } else
                    Log.e("ERROR_CODE", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(Call<List<Student>> call, Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    public void getProfessors(MutableLiveData<List<Professor>> professors) {
        List<Professor> cached = cache.getProfessors();
        if (cached != null && !professorsUpdate) {
            professors.setValue(cached);
        }

        webservice.getProfessors(headerAuthorization).enqueue(new Callback<List<Professor>>() {
            @Override
            public void onResponse(Call<List<Professor>> call, Response<List<Professor>> response) {
                if (response.isSuccessful()) {
                    professorsUpdate = false;
                    professors.postValue(response.body());
                    cache.setProfessors(response.body());
                } else
                    Log.e("ERROR_CODE", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(Call<List<Professor>> call, Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    public List<Group> getAllGroups() {
        List<Group> cached = cache.getAllGroups();
        if (cached != null && !allGroupsUpdate) {
            return cached;
        }

        List<Group> allGroups = new ArrayList<>();
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

    public List<Subject> getAllSubjects() {
        List<Subject> cached = cache.getAllSubjects();
        if (cached != null && !allSubjectsUpdate) {
            return cached;
        }

        List<Subject> allSubjects = new ArrayList<>();
        allSubjects.add(new Subject(1, "Матан"));
        allSubjects.add(new Subject(2, "Мобилки"));
        allSubjects.add(new Subject(3, "Алгебра"));
        allSubjects.add(new Subject(4, "Операционки"));
        allSubjects.add(new Subject(5, "Моделирование"));
        allSubjects.add(new Subject(6, "Методы оптимизации"));

        cache.setAllSubjects(allSubjects);
        return allSubjects;
    }

    public List<Semester> getSemesters() {
        List<Semester> cached = cache.getSemesters();
        if (cached != null && !semestersUpdate) {
            return cached;
        }

        List<Semester> semesters = new ArrayList<>();
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

    /*http://localhost:8080/api/students?groupId=1*/
    public List<Student> getStudentsInGroup(long groupId) {
        List<Student> studentsInGroup = new ArrayList<>();
        if (groupId == 1) {
            studentsInGroup.add(new Student(1, "1Александр", "1Другаков", new Group(1, "1ИУ9-11"), "ROLE_STUDENT"));
            studentsInGroup.add(new Student(2, "2Александр", "2Другаков", new Group(1, "1ИУ9-11"), "ROLE_STUDENT"));
        } else if (groupId == 2) {
            studentsInGroup.add(new Student(3, "3Александр", "3Другаков", new Group(2, "2ИУ9-21"), "ROLE_STUDENT"));
            studentsInGroup.add(new Student(4, "4Александр", "4Другаков", new Group(2, "2ИУ9-21"), "ROLE_STUDENT"));
        } else if (groupId == 3) {
            studentsInGroup.add(new Student(5, "5Александр", "5Другаков", new Group(3, "3ИУ9-31"), "ROLE_STUDENT"));
            studentsInGroup.add(new Student(6, "6Александр", "6Другаков", new Group(3, "3ИУ9-31"), "ROLE_STUDENT"));
        } else if (groupId == 4) {
            studentsInGroup.add(new Student(7, "7Александр", "7Другаков", new Group(4, "4ИУ9-41"), "ROLE_STUDENT"));
            studentsInGroup.add(new Student(8, "8Александр", "8Другаков", new Group(4, "4ИУ9-41"), "ROLE_STUDENT"));
            studentsInGroup.add(new Student(9, "9Александр", "9Другаков", new Group(4, "4ИУ9-41"), "ROLE_STUDENT"));
        }

        return studentsInGroup;
    }

    /*http://localhost:8080/api/available-students?professorId=1&semesterId=1*/
    public List<Student> getAvailableStudents(long semesterId) {
        List<Student> cached = cache.getAvailableStudents();
        if (cached != null && !availableStudentsUpdate) {
            return cached;
        }

        List<Student> availableStudents = new ArrayList<>();
        availableStudents.add(new Student(1, "1Александр", "1Другаков", new Group(1, "1ИУ9-11"), "ROLE_STUDENT"));
        availableStudents.add(new Student(2, "2Александр", "2Другаков", new Group(1, "1ИУ9-11"), "ROLE_STUDENT"));
        availableStudents.add(new Student(3, "3Александр", "3Другаков", new Group(2, "2ИУ9-21"), "ROLE_STUDENT"));
        availableStudents.add(new Student(4, "4Александр", "4Другаков", new Group(2, "2ИУ9-21"), "ROLE_STUDENT"));
        availableStudents.add(new Student(5, "5Александр", "5Другаков", new Group(3, "3ИУ9-31"), "ROLE_STUDENT"));
        availableStudents.add(new Student(6, "6Александр", "6Другаков", new Group(3, "3ИУ9-31"), "ROLE_STUDENT"));

        cache.setAvailableStudents(availableStudents);
        return availableStudents;
    }

    /*http://localhost:8080/api/available-subjects?professorId=1&semesterId=1*/
    public List<Subject> getAvailableSubjects(long semesterId) {
        List<Subject> cached = cache.getAvailableSubjects();
        if (cached != null && !availableSubjectsUpdate) {
            return cached;
        }

        List<Subject> availableSubjects = new ArrayList<>();
        availableSubjects.add(new Subject(1, "Матан"));
        availableSubjects.add(new Subject(2, "Мобилки"));
        availableSubjects.add(new Subject(3, "Алгебра"));

        cache.setAvailableSubjects(availableSubjects);
        return availableSubjects;
    }

    //FIXME заменить SubjectInfo на Group?
    /*http://localhost:8080/api/available-groups-in-subject?professorId=1&semesterId=1&subjectId=1*/
    public List<SubjectInfo> getAvailableGroupsInSubject(long professorId, long subjectId, long semesterId) {
        List<SubjectInfo> availableGroupsInSubject = new ArrayList<>();
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

    //FIXME заменить SubjectInfo на Group?
    /*http://localhost:8080/api/available-subjects-with-groups?professorId=1&semesterId=1*/
    public HashMap<Subject, List<SubjectInfo>> getAvailableSubjectsWithGroups(long professorId, long semesterId) {
        HashMap<Subject, List<SubjectInfo>> cached = cache.getAvailableSubjectsWithGroups();
        if (cached != null && !availableSubjectsWithGroupsUpdate) {
            return cached;
        }

        HashMap<Subject, List<SubjectInfo>> availableSubjectsWithGroups = new HashMap<>();
        List<Subject> availableSubjects = getAvailableSubjects(semesterId);
        for (int i = 0; i < availableSubjects.size(); i++) {
            List<SubjectInfo> groups = getAvailableGroupsInSubject(professorId, availableSubjects.get(i).getId(), semesterId);
            availableSubjectsWithGroups.put(availableSubjects.get(i), groups);
        }

        cache.setAvailableSubjectsWithGroups(availableSubjectsWithGroups);
        return availableSubjectsWithGroups;
    }

    /*http://localhost:8080/api/available-student-subjects?studentId=1&semesterId=1*/
    public List<Subject> getAvailableStudentSubjects(long studentId, long semesterId) {
        List<Subject> availableStudentSubjects = new ArrayList<>();
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

    public HashMap<Integer, ModuleInfo> getModuleInfo(long groupId, long subjectId, long lecturerId, long seminarianId, long semesterId) {
        HashMap<Integer, ModuleInfo> modulesInfo = new HashMap<>();
        List<Integer> modules = getModules();

        modulesInfo.put(modules.get(0), new ModuleInfo(modules.get(0), groupId, subjectId, lecturerId, seminarianId, semesterId, 23, 35));
        modulesInfo.put(modules.get(1), new ModuleInfo(modules.get(1), groupId, subjectId, lecturerId, seminarianId, semesterId, 18, 30));
        modulesInfo.put(modules.get(2), new ModuleInfo(modules.get(2), groupId, subjectId, lecturerId, seminarianId, semesterId, 23, 35));

        return modulesInfo;
    }

    public HashMap<Integer, List<Event>> getEvents(long groupId, long subjectId, long lecturerId, long seminarianId, long semesterId) {
        HashMap<Integer, List<Event>> events = new HashMap<>();
        List<Integer> modules = getModules();
        List<Event> events1 = new ArrayList<>();

        events1.add(new Event(1, modules.get(0), 1, 1, 2, 3, 7, "РК",
                1, new Date(2021, 0, 1), new Date(2021, 1, 1), 10, 20));
        events1.add(new Event(2, modules.get(0), 1, 1, 2, 3, 7, "ДЗ",
                1, new Date(2021, 5, 1), new Date(2021, 6, 1), 10, 20));
        List<Event> events2 = new ArrayList<>();
        events2.add(new Event(3, modules.get(1), 1, 1, 2, 3, 7, "РК",
                2, new Date(2021, 3, 1), new Date(2021, 4, 1), 10, 20));
        events2.add(new Event(4, modules.get(1), 1, 1, 2, 3, 7, "ДЗ",
                2, new Date(2021, 2, 1), new Date(2021, 3, 1), 10, 20));
        List<Event> events3 = new ArrayList<>();
        events3.add(new Event(5, modules.get(2), 1, 1, 2, 3, 7, "РК",
                3, new Date(2021, 4, 1), new Date(2021, 5, 1), 10, 20));
        events3.add(new Event(6, modules.get(2), 1, 1, 2, 3, 7, "ДЗ",
                3, new Date(2021, 1, 1), new Date(2021, 2, 1), 10, 20));

        events.put(modules.get(0), events1);
        events.put(modules.get(1), events2);
        events.put(modules.get(2), events3);
        return events;
    }

    public HashMap<Integer, List<Lesson>> getLessons(long groupId, long subjectId, long lecturerId, long seminarianId, long semesterId) {
        HashMap<Integer, List<Lesson>> lessons = new HashMap<>();
        List<Integer> modules = getModules();
        List<Lesson> lessons1 = new ArrayList<>();
        lessons1.add(new Lesson(1, modules.get(0), 1, 1, 2, 3,
                7, new Date(2021, 0, 15, 15, 20), true, 1));
        lessons1.add(new Lesson(2, modules.get(0), 1, 1, 2, 3,
                7, new Date(2021, 1, 15, 15, 20), false, 2));
        List<Lesson> lessons2 = new ArrayList<>();
        lessons2.add(new Lesson(3, modules.get(1), 1, 1, 2, 3,
                7, new Date(2021, 2, 15, 15, 20), false, 1));
        lessons2.add(new Lesson(4, modules.get(1), 1, 1, 2, 3,
                7, new Date(2021, 3, 15, 15, 20), true, 3));
        List<Lesson> lessons3 = new ArrayList<>();
        lessons3.add(new Lesson(5, modules.get(2), 1, 1, 2, 3,
                7, new Date(2021, 4, 15, 15, 20), true, 4));
        lessons3.add(new Lesson(6, modules.get(2), 1, 1, 2, 3,
                7, new Date(2021, 5, 15, 15, 20), true, 1));

        lessons.put(modules.get(0), lessons1);
        lessons.put(modules.get(1), lessons2);
        lessons.put(modules.get(2), lessons3);
        return lessons;
    }

    public HashMap<Integer, List<Lesson>> getLectures(long groupId, long subjectId, long lecturerId, long seminarianId, long semesterId) {
        return getSpecificLessons(groupId, subjectId, lecturerId, seminarianId, semesterId, true);
    }

    public HashMap<Integer, List<Lesson>> getSeminars(long groupId, long subjectId, long lecturerId, long seminarianId, long semesterId) {
        return getSpecificLessons(groupId, subjectId, lecturerId, seminarianId, semesterId, false);
    }

    public HashMap<Integer, List<Lesson>> getSpecificLessons(long groupId, long subjectId, long lecturerId, long seminarianId, long semesterId, boolean isLecture) {
        HashMap<Integer, List<Lesson>> studentsSpecificLessons = new HashMap<>();
        HashMap<Integer, List<Lesson>> studentsLessons = getLessons(groupId, subjectId, lecturerId, seminarianId, semesterId);

        List<Integer> modules = getModules();

        List<Lesson> lessons1 = new ArrayList<>();
        for (Lesson lesson : studentsLessons.get(modules.get(0))) {
            if (lesson.isLecture() == isLecture) {
                lessons1.add(lesson);
            }
        }

        List<Lesson> lessons2 = new ArrayList<>();
        for (Lesson lesson : studentsLessons.get(modules.get(1))) {
            if (lesson.isLecture()) {
                lessons2.add(lesson);
            }
        }

        List<Lesson> lessons3 = new ArrayList<>();
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

    public List<StudentPerformanceInSubject> getStudentsPerformancesInSubject(long groupId, long subjectId, long lecturerId, long seminarianId, long semesterId) {
        List<StudentPerformanceInSubject> studentsPerformances = new ArrayList<>();

        studentsPerformances.add(new StudentPerformanceInSubject(1, groupId, subjectId, lecturerId, seminarianId, semesterId, 0, 0, 0, false, 0, 0));
        studentsPerformances.add(new StudentPerformanceInSubject(2, groupId, subjectId, lecturerId, seminarianId, semesterId, 0, 10, 2, true, 0, 0));

        return studentsPerformances;
    }

    public HashMap<Integer, StudentPerformanceInModule> getStudentPerformanceInModules(long studentId, long groupId, long subjectId, long lecturerId, long seminarianId, long semesterId) {
        HashMap<Integer, StudentPerformanceInModule> studentPerformanceInModule = new HashMap<>();
        List<Integer> modules = getModules();

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

    public HashMap<Integer, List<StudentPerformanceInModule>> getStudentsPerformancesInModules(long groupId, long subjectId, long lecturerId, long seminarianId, long semesterId) {
        HashMap<Integer, List<StudentPerformanceInModule>> studentsLessons = new HashMap<>();
        List<StudentPerformanceInModule> firstModule = new ArrayList<>();
        List<StudentPerformanceInModule> secondModule = new ArrayList<>();
        List<StudentPerformanceInModule> thirdModule = new ArrayList<>();

        List<Integer> modules = getModules();
        List<Student> students = getStudentsInGroup(groupId);
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

    public List<StudentEvent> getStudentEventsInModule(long studentId, int moduleNumber, long subjectId, long lecturerId, long seminarianId, long semesterId) {
        List<StudentEvent> studentEventsInModule = new ArrayList<>();
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

    public HashMap<Integer, List<StudentEvent>> getStudentEvents(long studentId, long subjectId, long lecturerId, long seminarianId, long semesterId) { //moduleNumber?
        HashMap<Integer, List<StudentEvent>> studentEventsByModules = new HashMap<>();
        List<Integer> modules = getModules();
        for (int i = 0; i < modules.size(); i++) {
            List<StudentEvent> studentEvents = getStudentEventsInModule(studentId, modules.get(i), subjectId, lecturerId, seminarianId, semesterId);
            studentEventsByModules.put(modules.get(i), studentEvents);
        }

        return studentEventsByModules;
    }

    public List<StudentLesson> getStudentLessonsInModule(long studentId, int moduleNumber, long subjectId, long lecturerId, long seminarianId, long semesterId) {
        List<StudentLesson> studentLessonsInModule = new ArrayList<>();
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

    public HashMap<Integer, List<StudentLesson>> getStudentLessons(long studentId, long subjectId, long lecturerId, long seminarianId, long semesterId) {
        HashMap<Integer, List<StudentLesson>> studentLessonsByModules = new HashMap<>();
        List<Integer> modules = getModules();
        for (int i = 0; i < modules.size(); i++) {
            List<StudentLesson> studentLessons = getStudentLessonsInModule(studentId, modules.get(i), subjectId, lecturerId, seminarianId, semesterId);
            studentLessonsByModules.put(modules.get(i), studentLessons);
        }

        return studentLessonsByModules;
    }

    public HashMap<Integer, List<List<StudentEvent>>> getStudentsEvents(long groupId, long subjectId, long lecturerId, long seminarianId, long semesterId) {
        HashMap<Integer, List<List<StudentEvent>>> studentsEvents = new HashMap<>();
        List<List<StudentEvent>> firstModule = new ArrayList<>();
        List<List<StudentEvent>> secondModule = new ArrayList<>();
        List<List<StudentEvent>> thirdModule = new ArrayList<>();

        List<Integer> modules = getModules();
        List<Student> students = getStudentsInGroup(groupId);
        for (int i = 0; i < students.size(); i++) {
            HashMap<Integer, List<StudentEvent>> studentEvents =
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

    public HashMap<Integer, List<List<StudentLesson>>> getStudentsLessons(long groupId, long subjectId, long lecturerId, long seminarianId, long semesterId) {
        HashMap<Integer, List<List<StudentLesson>>> studentsLessons = new HashMap<>();
        List<List<StudentLesson>> firstModule = new ArrayList<>();
        List<List<StudentLesson>> secondModule = new ArrayList<>();
        List<List<StudentLesson>> thirdModule = new ArrayList<>();

        List<Integer> modules = getModules();
        List<Student> students = getStudentsInGroup(groupId);
        for (int i = 0; i < students.size(); i++) {
            HashMap<Integer, List<StudentLesson>> studentLessons =
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

    public Group getGroupById(long groupId) {
        return new Group(groupId, groupId + "ИУ9-11");
    }

    public SubjectInfo getSubjectInfo(long groupId, long subjectId, long semesterId) {
        return new SubjectInfo(new Group(groupId, "1ИУ9-11"), subjectId, 2, 3, semesterId, true, false);
    }

    public SubjectInfo getSubjectInfo(long groupId, long subjectId, long lecturerId, long seminarianId, long semesterId) {
        return new SubjectInfo(new Group(groupId, "1ИУ9-11"), subjectId, lecturerId, seminarianId, semesterId, true, false);
    }

    public ModuleInfo getModuleInfoByNumber(int moduleNumber, long groupId, long subjectId, long lecturerId, long seminarianId, long semesterId) {
        return new ModuleInfo(moduleNumber, groupId, subjectId, lecturerId, seminarianId, semesterId, 23, 35);
    }

    public StudentPerformanceInSubject getStudentPerformanceInSubject(long studentId, long groupId, long subjectId, long lecturerId, long seminarianId, long semesterId) {
        return new StudentPerformanceInSubject(studentId, groupId, subjectId, lecturerId, seminarianId, semesterId, 0, 0, 0, false, 0, 0);
    }

    public Subject getSubjectById(long subjectId) {
        return new Subject(subjectId, "Матан");
    }

    /*http://localhost:8080/api/students/1*/
    public void getStudentById(long studentId, MutableLiveData<Student> student) {
        webservice.getStudentById(headerAuthorization, studentId).enqueue(new Callback<Student>() {
            @Override
            public void onResponse(Call<Student> call, Response<Student> response) {
                if (response.isSuccessful()) {
                    student.postValue(response.body());
                } else
                    Log.e("ERROR_CODE", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(Call<Student> call, Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    public Student getStudentByIdWithLogin(long studentId) {
        return new Student(studentId, studentId + "Александр", studentId + "Другаков", new Group(1, "1ИУ9-11"), "fdfdf", "aaaabbb", "ROLE_STUDENT");
    }

    /*http://localhost:8080/api/professors/1*/
    public void getProfessorById(long professorId, MutableLiveData<Professor> professor) {
        webservice.getProfessorById(headerAuthorization, professorId).enqueue(new Callback<Professor>() {
            @Override
            public void onResponse(Call<Professor> call, Response<Professor> response) {
                if (response.isSuccessful()) {
                    professor.postValue(response.body());
                } else
                    Log.e("ERROR_CODE", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(Call<Professor> call, Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    public Professor getProfessorByIdWithLogin(long professorId) {
        return new Professor(professorId, "Александр", "Коновалов", "qwweetrt", "zzzxxx", "ROLE_PROFESSOR");
    }

    public Event getEventById(long eventId) {
        return new Event(eventId, 1, 1, 1, 2, 3,
                7, "РК", 1, new Date(2021, 0, 1),
                new Date(2021, 1, 1), 10, 15);
    }

    public Lesson getLessonById(long lessonId) {
        return new Lesson(lessonId, 1, 1, 1, 2, 3,
                7, new Date(2021, 0, 15, 15, 20), true, 1);
    }

    public Semester getSemesterById(long semesterId) {
        return new Semester(semesterId, 2021, true);
    }

    public StudentEvent getStudentEventById(int attemptNumber, long eventId, long studentId) {
        return new StudentEvent(attemptNumber, eventId, 1, studentId, 1, 1, 2, 3, 7, true, 1);
    }

    public StudentLesson getStudentLessonById(long lessonId, long studentId) {
        return new StudentLesson(lessonId, 1, studentId, 1, 1, 2, 3, 7, true, 1);
    }

    public void changeStudentGroup(long studentId, long newStudentGroupId) {
        /*Repository.getInstance().deleteStudent(studentId);
        Student student = getStudentById(studentId);
        Repository.getInstance().addStudent(student.getFirstName(), student.getSecondName(), newStudentGroupId,
                student.getUsername(), student.getPassword());*/
    }

    public void addStudent(String firstName, String secondName, long groupId, String login, String password) {
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

    public void addAvailableSubject(long professorId, boolean isLecturer, boolean isSeminarian, long groupId,
                                    long subjectId, long semesterId, boolean isExam, boolean isDifferentiatedCredit) {
        //
    }

    public void addGroupInAvailableSubject(long lecturerId, long seminarianId, long groupId, long subjectId, long semesterId, boolean isExam) {
        //
    }

    public void addSemester(int year, boolean isFirstHalf) {
        //
    }

    public void addEvent(int moduleNumber, long groupId, long subjectId, long lecturerId, long seminarianId, long semesterId,
                         String type, Date startDate, Date deadlineDate, int minPoints, int maxPoints) {
        //
    }

    public void addLesson(int moduleNumber, long groupId, long subjectId, long lecturerId, long seminarianId, long semesterId,
                          Date dateAndTime, boolean isLecture, int pointsPerVisit) {
        //
    }

    public void addStudentEvent(int attemptNumber, long eventId, long studentId, int moduleNumber, long groupId, long subjectId,
                                long lecturerId, long seminarianId, long semesterId, boolean isAttended, int variantNumber) {
        //
    }

    public void addStudentLesson(long lessonId, int moduleNumber, long studentId, long groupId, long subjectId, long lecturerId,
                                 long seminarianId, long semesterId, boolean isAttended) {
        //
    }

    public void editStudent(long id, String firstName, String secondName, String login, String password) {
        //
    }

    public void editProfessor(long id, String firstName, String secondName, String login, String password) {
        //
    }

    public void editGroup(long id, String title) {
        //
    }

    public void editModuleInfo(int moduleNumber, long groupId, long subjectId, long lecturerId, long seminarianId, long semesterId, int minPoints, int maxPoints) {
        //
    }

    public void editSubjectInfo(long groupId, long subjectId, long lecturerId, long seminarianId, long semesterId,
                                boolean isSwapLecturerAndSeminarian, boolean isExam, boolean isDifferentiatedCredit, boolean isForAllGroups) {
        //
    }

    public void editStudentPerformanceInSubject(long studentId, long groupId, long subjectId, long lecturerId, long seminarianId, long semesterId, int earnedPoints, int bonusPoints,
                                                boolean isHaveCreditOrAdmission, int earnedExamPoints, int mark) {
        //
    }

    public void editStudentPerformanceInSubject(long studentId, long groupId, long subjectId, long lecturerId, long seminarianId, long semesterId, int earnedPoints, int bonusPoints,
                                                boolean isHaveCreditOrAdmission) {
        //
    }

    public void editSubject(long id, String title) {
        //
    }

    public void editSemester(long id, int year, boolean isFirstHalf) {
        //
    }

    public void editEvent(long eventId, Date startDate, Date deadlineDate, int minPoints, int maxPoints) {
        //
    }

    public void editLesson(long lessonId, Date dateAndTime, boolean isLecture, int pointsPerVisit) {
        //
    }

    public void editStudentEvent(int attemptNumber, long eventId, long studentId, boolean isAttended, int variantNumber,
                                 Date finishDate, int earnedPoints, int bonusPoints, boolean isHaveCredit) {
        //
    }

    public void editStudentLesson(long lessonId, long studentId, boolean isAttended, int bonusPoints) {
        //
    }

    public void deleteStudent(long studentId) {
        //
    }

    public void deleteProfessor(long professorId) {
        //
    }

    public void deleteGroup(long groupId) {
        //
    }

    public void deleteSubject(long subjectId) {
        //
    }

    public void deleteAvailableSubject(long professorId, long subjectId, long semesterId) {
        //
    }

    public void deleteGroupInAvailableSubject(long professorId, long groupId, long subjectId, long semesterId) {
        //
    }

    public void deleteSemester(long semesterId) {
        //
    }

    public void deleteEvent(long eventId) {
        //
    }

    public void deleteLesson(long lessonId) {
        //
    }

    public void deleteStudentEvent(int attemptNumber, long eventId, long studentId) {
        //
    }

    public void deleteStudentLesson(long lessonId, long studentId) {
        //
    }
}