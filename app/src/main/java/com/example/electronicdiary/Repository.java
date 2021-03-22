package com.example.electronicdiary;

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

    public ArrayList<Student> getAllStudents() {
        ArrayList<Student> cached = cache.getAllStudents();
        if (cached != null && !allStudentsUpdate) {
            return cached;
        }

        ArrayList<Student> allStudents = new ArrayList<>();
        allStudents.add(new Student(1, "1Александр", "1Другаков", 1));
        allStudents.add(new Student(2, "2Александр", "2Другаков", 1));
        allStudents.add(new Student(3, "3Александр", "3Другаков", 2));
        allStudents.add(new Student(4, "4Александр", "4Другаков", 2));
        allStudents.add(new Student(5, "5Александр", "5Другаков", 3));
        allStudents.add(new Student(6, "6Александр", "6Другаков", 3));
        allStudents.add(new Student(7, "7Александр", "7Другаков", 4));
        allStudents.add(new Student(8, "8Александр", "8Другаков", 4));
        allStudents.add(new Student(9, "9Александр", "9Другаков", 4));

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
            studentsInGroup.add(new Student(1, "1Александр", "1Другаков", 1));
            studentsInGroup.add(new Student(2, "2Александр", "2Другаков", 1));
        } else if (groupId == 2) {
            studentsInGroup.add(new Student(3, "3Александр", "3Другаков", 2));
            studentsInGroup.add(new Student(4, "4Александр", "4Другаков", 2));
        } else if (groupId == 3) {
            studentsInGroup.add(new Student(5, "5Александр", "5Другаков", 3));
            studentsInGroup.add(new Student(6, "6Александр", "6Другаков", 3));
        } else if (groupId == 4) {
            studentsInGroup.add(new Student(7, "7Александр", "7Другаков", 4));
            studentsInGroup.add(new Student(8, "8Александр", "8Другаков", 4));
            studentsInGroup.add(new Student(9, "9Александр", "9Другаков", 4));
        }

        return studentsInGroup;
    }

    public ArrayList<Student> getAvailableStudents(int semesterId) {
        ArrayList<Student> cached = cache.getAvailableStudents();
        if (cached != null && !availableStudentsUpdate) {
            return cached;
        }

        ArrayList<Student> availableStudents = new ArrayList<>();
        availableStudents.add(new Student(1, "1Александр", "1Другаков", 1));
        availableStudents.add(new Student(2, "2Александр", "2Другаков", 1));
        availableStudents.add(new Student(3, "3Александр", "3Другаков", 2));
        availableStudents.add(new Student(4, "4Александр", "4Другаков", 2));
        availableStudents.add(new Student(5, "5Александр", "5Другаков", 3));
        availableStudents.add(new Student(6, "6Александр", "6Другаков", 3));

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

    public ArrayList<Group> getAvailableGroupsInSubject(int subjectId, int semesterId) {
        ArrayList<Group> availableGroupsInSubject = new ArrayList<>();
        if (subjectId == 1) {
            availableGroupsInSubject.add(new Group(1, "1ИУ9-11"));
            availableGroupsInSubject.add(new Group(2, "2ИУ9-21"));
            availableGroupsInSubject.add(new Group(3, "3ИУ9-31"));
        } else if (subjectId == 2) {
            availableGroupsInSubject.add(new Group(4, "4ИУ9-41"));
            availableGroupsInSubject.add(new Group(5, "5ИУ9-51"));
            availableGroupsInSubject.add(new Group(6, "6ИУ9-61"));
        } else if (subjectId == 3) {
            availableGroupsInSubject.add(new Group(7, "7ИУ9-71"));
            availableGroupsInSubject.add(new Group(8, "8ИУ9-81"));
            availableGroupsInSubject.add(new Group(9, "9ИУ9-91"));
        }

        return availableGroupsInSubject;
    }

    public HashMap<Subject, ArrayList<Group>> getAvailableSubjectsWithGroups(int semesterId) {
        HashMap<Subject, ArrayList<Group>> cached = cache.getAvailableSubjectsWithGroups();
        if (cached != null && !availableSubjectsWithGroupsUpdate) {
            return cached;
        }

        HashMap<Subject, ArrayList<Group>> availableSubjectsWithGroups = new HashMap<>();
        ArrayList<Subject> availableSubjects = getAvailableSubjects(semesterId);
        for (int i = 0; i < availableSubjects.size(); i++) {
            ArrayList<Group> groups = getAvailableGroupsInSubject(availableSubjects.get(i).getId(), semesterId);
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

    public ArrayList<StudentEvent> getStudentEvents(int studentId, int subjectId, int semesterId) { //moduleNumber?
        ArrayList<StudentEvent> studentEvents = new ArrayList<>();
        if (studentId == 1) {
            studentEvents.add(new StudentEvent(1, 1, 1, 1, 1, 1, 2, 3, 7, true, 1));
            studentEvents.add(new StudentEvent(1, 2, 1, 1, 1, 1, 2, 3, 7, true, 1));
            studentEvents.add(new StudentEvent(1, 3, 2, 1, 1, 1, 2, 3, 7, true, 1));
            studentEvents.add(new StudentEvent(1, 4, 2, 1, 1, 1, 2, 3, 7, true, 1));
            studentEvents.add(new StudentEvent(1, 5, 3, 1, 1, 1, 2, 3, 7, true, 1));
            studentEvents.add(new StudentEvent(1, 6, 3, 1, 1, 1, 2, 3, 7, true, 1));
        } else if (studentId == 2) {
            studentEvents.add(new StudentEvent(1, 1, 1, 2, 1, 1, 2, 3, 7, true, 2));
            studentEvents.add(new StudentEvent(1, 2, 1, 2, 1, 1, 2, 3, 7, true, 2));
            studentEvents.add(new StudentEvent(1, 3, 2, 2, 1, 1, 2, 3, 7, true, 2));
            studentEvents.add(new StudentEvent(1, 4, 2, 2, 1, 1, 2, 3, 7, true, 2));
            studentEvents.add(new StudentEvent(1, 5, 3, 2, 1, 1, 2, 3, 7, true, 2));
            studentEvents.add(new StudentEvent(1, 6, 3, 2, 1, 1, 2, 3, 7, true, 2));
        } else if (studentId == 3) {
            studentEvents.add(new StudentEvent(1, 1, 1, 3, 2, 1, 2, 3, 7, true, 3));
            studentEvents.add(new StudentEvent(1, 2, 1, 3, 2, 1, 2, 3, 7, true, 3));
            studentEvents.add(new StudentEvent(1, 3, 2, 3, 2, 1, 2, 3, 7, true, 3));
            studentEvents.add(new StudentEvent(1, 4, 2, 3, 2, 1, 2, 3, 7, true, 3));
            studentEvents.add(new StudentEvent(1, 5, 3, 3, 2, 1, 2, 3, 7, true, 3));
            studentEvents.add(new StudentEvent(1, 6, 3, 3, 2, 1, 2, 3, 7, true, 3));
        }

        return studentEvents;
    }

    public ArrayList<StudentLesson> getStudentLessonsInModule(int studentId, int moduleNumber, int subjectId, int semesterId) {
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

    public HashMap<Integer, ArrayList<StudentLesson>> getStudentLessonsByModules(int studentId, int subjectId, int semesterId) {
        HashMap<Integer, ArrayList<StudentLesson>> studentLessonsByModules = new HashMap<>();
        ArrayList<Integer> modules = getModules();
        for (int i = 0; i < modules.size(); i++) {
            ArrayList<StudentLesson> studentLessons = getStudentLessonsInModule(studentId, modules.get(i), subjectId, semesterId);
            studentLessonsByModules.put(modules.get(i), studentLessons);
        }

        return studentLessonsByModules;
    }

    public ArrayList<ArrayList<StudentEvent>> getStudentsEvents(int groupId, int subjectId, int semesterId) {
        ArrayList<ArrayList<StudentEvent>> studentsEvents = new ArrayList<>();
        ArrayList<Student> students = getStudentsInGroup(groupId);
        for (int i = 0; i < students.size(); i++) {
            studentsEvents.add(getStudentEvents(students.get(i).getId(), subjectId, semesterId));
        }

        return studentsEvents;
    }

    public HashMap<Integer, ArrayList<ArrayList<StudentLesson>>> getStudentsLessonsByModules(int groupId, int subjectId, int semesterId) {
        HashMap<Integer, ArrayList<ArrayList<StudentLesson>>> studentsLessonsByModules = new HashMap<>();
        ArrayList<ArrayList<StudentLesson>> firstModule = new ArrayList<>();
        ArrayList<ArrayList<StudentLesson>> secondModule = new ArrayList<>();
        ArrayList<ArrayList<StudentLesson>> thirdModule = new ArrayList<>();

        ArrayList<Integer> modules = getModules();
        ArrayList<Student> students = getStudentsInGroup(groupId);
        for (int i = 0; i < students.size(); i++) {
            HashMap<Integer, ArrayList<StudentLesson>> studentLessonsByModules =
                    getStudentLessonsByModules(students.get(i).getId(), subjectId, semesterId);
            firstModule.add(studentLessonsByModules.get(modules.get(0)));
            secondModule.add(studentLessonsByModules.get(modules.get(1)));
            thirdModule.add(studentLessonsByModules.get(modules.get(2)));
        }

        studentsLessonsByModules.put(modules.get(0), firstModule);
        studentsLessonsByModules.put(modules.get(1), secondModule);
        studentsLessonsByModules.put(modules.get(2), thirdModule);
        return studentsLessonsByModules;
    }

    public Group getGroupById(int groupId) {
        return new Group(groupId, groupId + "ИУ9-11");
    }

    public Subject getSubjectById(int subjectId) {
        return new Subject(subjectId, "Матан");
    }

    public Student getStudentById(int studentId) {
        return new Student(studentId, studentId + "Александр", studentId + "Другаков", 1);
    }

    public Professor getProfessorById(int professorId) {
        return new Professor(professorId, "Александр", "Коновалов");
    }

    public Event getEventById(int eventId) {
        return new Event(eventId, 1, 1, 1, 2, 3,
                7, "РК", 1, new Date(2021, 0, 1),
                new Date(2021, 1, 1), 10, 15);
    }

    public Lesson getLessonById(int lessonId) {
        return new Lesson(lessonId, 1, 1, 1, 2, 3,
                7, new Date(2021, 0, 15), true, 1);
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

    public void addAvailableSubject(int professorId, boolean isLecturer, int groupId, int subjectId, int semesterId, boolean isExam) {
        //
    }

    public void addGroupInAvailableSubject(int lecturerId, int seminarianId, int groupId, int subjectId, int semesterId, boolean isExam) {
        //
    }

    public void addSemester(int year, boolean isFirstHalf) {
        //
    }

    public void addEvent(int moduleNumber, int groupId, int subjectId, int lecturerId, int seminarianId, int semesterId,
                         String type, int number, Date startDate, Date deadlineDate, int minPoints, int maxPoints) {
        //
    }

    public void addLesson(int moduleNumber, int groupId, int subjectId, int lecturerId, int seminarianId, int semesterId,
                          Date dateAndTime, boolean isLecture, int pointsPerVisit) {
        //
    }

    public void addStudentEvent(int attemptNumber, int eventId, int moduleNumber, int studentId, int groupId, int subjectId,
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

    public void editSubject(int id, String title) {
        //
    }

    public void editSemester(int id, int year, boolean isFirstHalf) {
        //
    }

    public void editEvent(int id, int moduleNumber, int groupId, int subjectId, int lecturerId, int seminarianId,
                          int semesterId, String type, int number, Date startDate, Date deadlineDate, int minPoints, int maxPoints) {
        //
    }

    public void editLesson(int id, int moduleNumber, int groupId, int subjectId, int lecturerId, int seminarianId,
                           int semesterId, Date dateAndTime, boolean isLecture, int pointsPerVisit) {
        //
    }

    public void editStudentEvent(int attemptNumber, int eventId, int moduleNumber, int studentId, int groupId, int subjectId,
                                 int lecturerId, int seminarianId, int semesterId, boolean isAttended, int variantNumber,
                                 Date finishDate, int earnedPoints, int bonusPoints, boolean isHaveCredit) {
        //
    }

    public void editStudentLesson(int lessonId, int moduleNumber, int studentId, int groupId, int subjectId, int lecturerId,
                                  int seminarianId, int semesterId, boolean isAttended, int bonusPoints) {
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