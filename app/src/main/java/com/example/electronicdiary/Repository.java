package com.example.electronicdiary;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.electronicdiary.data_classes.Event;
import com.example.electronicdiary.data_classes.Group;
import com.example.electronicdiary.data_classes.Lesson;
import com.example.electronicdiary.data_classes.Module;
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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Repository {
    private static volatile Repository repository;
    // 10.0.2.2  192.168.0.47  192.168.43.173
    private final Webservice webservice = new Retrofit.Builder().baseUrl("http://192.168.0.47:8080/api/").
            addConverterFactory(GsonConverterFactory.create()).build().create(Webservice.class);
    private String headerAuthorization;
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

    public void login(String username, String password, MutableLiveData<Result<JwtResponse>> result) {
        webservice.authenticateUser(new LoginRequest(username, password)).enqueue(new Callback<JwtResponse>() {
            @Override
            public void onResponse(@NotNull Call<JwtResponse> call, @NotNull Response<JwtResponse> response) {
                if (response.isSuccessful()) {
                    result.postValue(new Result.Success<>(response.body()));
                } else {
                    result.postValue(new Result.Error("Login failed: " + response.code()));
                    Log.e("ERROR_CODE", String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(@NotNull Call<JwtResponse> call, @NotNull Throwable t) {
                result.postValue(new Result.Error("Error: " + t.getMessage()));
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    public void getLoggedUser(JwtResponse jwtResponse, MutableLiveData<? extends Result<? extends User>> user) {
        headerAuthorization = "Bearer " + jwtResponse.getToken();
        Log.i("HEADER", headerAuthorization);
        if (jwtResponse.isProfessor()) {
            getProfessorById(jwtResponse.getId(), (MutableLiveData<Result<Professor>>) user);
        } else {
            getStudentById(jwtResponse.getId(), (MutableLiveData<Result<Student>>) user);
        }
    }

    public void logout() {
        user = null;
        headerAuthorization = "";
    }

    public List<Integer> getModulesNumbers() {
        List<Integer> modules = new ArrayList<>();
        modules.add(1);
        modules.add(2);
        modules.add(3);

        return modules;
    }

    public void getAllStudents(MutableLiveData<List<Student>> allStudents) {
        webservice.getAllStudents(headerAuthorization).enqueue(new Callback<List<Student>>() {
            @Override
            public void onResponse(@NotNull Call<List<Student>> call, @NotNull Response<List<Student>> response) {
                if (response.isSuccessful()) {
                    allStudents.postValue(response.body());
                } else
                    Log.e("ERROR_CODE", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(@NotNull Call<List<Student>> call, @NotNull Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    public void getProfessors(MutableLiveData<List<Professor>> professors) {
        webservice.getProfessors(headerAuthorization).enqueue(new Callback<List<Professor>>() {
            @Override
            public void onResponse(@NotNull Call<List<Professor>> call, @NotNull Response<List<Professor>> response) {
                if (response.isSuccessful()) {
                    professors.postValue(response.body());
                } else
                    Log.e("ERROR_CODE", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(@NotNull Call<List<Professor>> call, @NotNull Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    public void getAllGroups(MutableLiveData<List<Group>> allGroups) {
        webservice.getAllGroups(headerAuthorization).enqueue(new Callback<List<Group>>() {
            @Override
            public void onResponse(@NotNull Call<List<Group>> call, @NotNull Response<List<Group>> response) {
                if (response.isSuccessful()) {
                    allGroups.postValue(response.body());
                } else
                    Log.e("ERROR_CODE", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(@NotNull Call<List<Group>> call, @NotNull Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    public void getAllSubjects(MutableLiveData<List<Subject>> allSubjects) {
        webservice.getAllSubjects(headerAuthorization).enqueue(new Callback<List<Subject>>() {
            @Override
            public void onResponse(@NotNull Call<List<Subject>> call, @NotNull Response<List<Subject>> response) {
                if (response.isSuccessful()) {
                    allSubjects.postValue(response.body());
                } else
                    Log.e("ERROR_CODE", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(@NotNull Call<List<Subject>> call, @NotNull Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    public void getSemesters(MutableLiveData<List<Semester>> semesters) {
        webservice.getSemesters(headerAuthorization).enqueue(new Callback<List<Semester>>() {
            @Override
            public void onResponse(@NotNull Call<List<Semester>> call, @NotNull Response<List<Semester>> response) {
                if (response.isSuccessful()) {
                    semesters.postValue(response.body());
                } else
                    Log.e("ERROR_CODE", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(@NotNull Call<List<Semester>> call, @NotNull Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    public void getStudentsInGroup(long groupId, MutableLiveData<List<Student>> studentsInGroup) {
        webservice.getStudentsInGroup(headerAuthorization, groupId).enqueue(new Callback<List<Student>>() {
            @Override
            public void onResponse(@NotNull Call<List<Student>> call, @NotNull Response<List<Student>> response) {
                if (response.isSuccessful()) {
                    studentsInGroup.postValue(response.body());
                } else
                    Log.e("ERROR_CODE", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(@NotNull Call<List<Student>> call, @NotNull Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    public void getAvailableStudents(long professorId, long semesterId, MutableLiveData<List<Student>> availableStudents) {
        webservice.getAvailableStudents(headerAuthorization, professorId, semesterId).enqueue(new Callback<List<Student>>() {
            @Override
            public void onResponse(@NotNull Call<List<Student>> call, @NotNull Response<List<Student>> response) {
                if (response.isSuccessful()) {
                    availableStudents.postValue(response.body());
                } else
                    Log.e("ERROR_CODE", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(@NotNull Call<List<Student>> call, @NotNull Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    public void getAvailableSubjects(long professorId, long semesterId, MutableLiveData<List<Subject>> availableSubjects) {
        webservice.getAvailableSubjects(headerAuthorization, professorId, semesterId).enqueue(new Callback<List<Subject>>() {
            @Override
            public void onResponse(@NotNull Call<List<Subject>> call, @NotNull Response<List<Subject>> response) {
                if (response.isSuccessful()) {
                    availableSubjects.postValue(response.body());
                } else
                    Log.e("ERROR_CODE", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(@NotNull Call<List<Subject>> call, @NotNull Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    public void getAvailableGroupsInSubject(long professorId, long subjectId, long semesterId, MutableLiveData<List<SubjectInfo>> availableGroupsInSubject) {
        webservice.getAvailableGroupsInSubject(headerAuthorization, professorId, semesterId, subjectId).enqueue(new Callback<List<SubjectInfo>>() {
            @Override
            public void onResponse(@NotNull Call<List<SubjectInfo>> call, @NotNull Response<List<SubjectInfo>> response) {
                if (response.isSuccessful()) {
                    availableGroupsInSubject.postValue(response.body());
                } else
                    Log.e("ERROR_CODE", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(@NotNull Call<List<SubjectInfo>> call, @NotNull Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    public void getAvailableSubjectsWithGroups(long professorId, long semesterId, MutableLiveData<Map<String,
            List<SubjectInfo>>> availableSubjectsWithGroups) {
        webservice.getAvailableSubjectsWithGroups(headerAuthorization, professorId, semesterId)
                .enqueue(new Callback<Map<String, List<SubjectInfo>>>() {
                    @Override
                    public void onResponse(@NotNull Call<Map<String, List<SubjectInfo>>> call,
                                           @NotNull Response<Map<String, List<SubjectInfo>>> response) {
                        if (response.isSuccessful()) {
                            availableSubjectsWithGroups.postValue(response.body());
                        } else
                            Log.e("ERROR_CODE", String.valueOf(response.code()));
                    }

                    @Override
                    public void onFailure(@NotNull Call<Map<String, List<SubjectInfo>>> call, @NotNull Throwable t) {
                        Log.e("ERROR", t.getMessage());
                    }
                });
    }

    public void getAvailableStudentSubjects(long studentId, long semesterId, MutableLiveData<List<StudentPerformanceInSubject>> availableStudentSubjects) {
        webservice.getAvailableStudentSubjects(headerAuthorization, studentId, semesterId).enqueue(new Callback<List<StudentPerformanceInSubject>>() {
            @Override
            public void onResponse(@NotNull Call<List<StudentPerformanceInSubject>> call, @NotNull Response<List<StudentPerformanceInSubject>> response) {
                if (response.isSuccessful()) {
                    availableStudentSubjects.postValue(response.body());
                } else
                    Log.e("ERROR_CODE", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(@NotNull Call<List<StudentPerformanceInSubject>> call, @NotNull Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    public void getModules(long subjectInfoId, MutableLiveData<Map<String, Module>> modules) {
        webservice.getModules(headerAuthorization, subjectInfoId)
                .enqueue(new Callback<Map<String, Module>>() {
                    @Override
                    public void onResponse(@NotNull Call<Map<String, Module>> call, @NotNull Response<Map<String, Module>> response) {
                        if (response.isSuccessful()) {
                            modules.postValue(response.body());
                        } else
                            Log.e("ERROR_CODE", String.valueOf(response.code()));
                    }

                    @Override
                    public void onFailure(@NotNull Call<Map<String, Module>> call, @NotNull Throwable t) {
                        Log.e("ERROR", t.getMessage());
                    }
                });
    }

    public void getEvents(long subjectInfoId, MutableLiveData<Map<String, List<Event>>> events) {
        webservice.getEvents(headerAuthorization, subjectInfoId)
                .enqueue(new Callback<Map<String, List<Event>>>() {
                    @Override
                    public void onResponse(@NotNull Call<Map<String, List<Event>>> call, @NotNull Response<Map<String, List<Event>>> response) {
                        if (response.isSuccessful()) {
                            events.postValue(response.body());
                        } else
                            Log.e("ERROR_CODE", String.valueOf(response.code()));
                    }

                    @Override
                    public void onFailure(@NotNull Call<Map<String, List<Event>>> call, @NotNull Throwable t) {
                        Log.e("ERROR", t.getMessage());
                    }
                });
    }

    public void getLessons(long subjectInfoId,
                           MutableLiveData<Map<String, List<Lesson>>> lessons) {
        webservice.getLessons(headerAuthorization, subjectInfoId)
                .enqueue(new Callback<Map<String, List<Lesson>>>() {
                    @Override
                    public void onResponse(@NotNull Call<Map<String, List<Lesson>>> call, @NotNull Response<Map<String, List<Lesson>>> response) {
                        if (response.isSuccessful()) {
                            lessons.postValue(response.body());
                        } else
                            Log.e("ERROR_CODE", String.valueOf(response.code()));
                    }

                    @Override
                    public void onFailure(@NotNull Call<Map<String, List<Lesson>>> call, @NotNull Throwable t) {
                        Log.e("ERROR", t.getMessage());
                    }
                });
    }

    public void getLectures(long subjectInfoId,
                            MutableLiveData<Map<String, List<Lesson>>> lectures) {
        webservice.getSpecificLessons(headerAuthorization, subjectInfoId, true)
                .enqueue(new Callback<Map<String, List<Lesson>>>() {
                    @Override
                    public void onResponse(@NotNull Call<Map<String, List<Lesson>>> call, @NotNull Response<Map<String, List<Lesson>>> response) {
                        if (response.isSuccessful()) {
                            lectures.postValue(response.body());
                        } else
                            Log.e("ERROR_CODE", String.valueOf(response.code()));
                    }

                    @Override
                    public void onFailure(@NotNull Call<Map<String, List<Lesson>>> call, @NotNull Throwable t) {
                        Log.e("ERROR", t.getMessage());
                    }
                });
    }

    public void getSeminars(long subjectInfoId,
                            MutableLiveData<Map<String, List<Lesson>>> seminars) {
        webservice.getSpecificLessons(headerAuthorization, subjectInfoId, false)
                .enqueue(new Callback<Map<String, List<Lesson>>>() {
                    @Override
                    public void onResponse(@NotNull Call<Map<String, List<Lesson>>> call, @NotNull Response<Map<String, List<Lesson>>> response) {
                        if (response.isSuccessful()) {
                            seminars.postValue(response.body());
                        } else
                            Log.e("ERROR_CODE", String.valueOf(response.code()));
                    }

                    @Override
                    public void onFailure(@NotNull Call<Map<String, List<Lesson>>> call, @NotNull Throwable t) {
                        Log.e("ERROR", t.getMessage());
                    }
                });
    }

    public void getStudentsPerformancesInSubject(long subjectInfoId,
                                                 MutableLiveData<List<StudentPerformanceInSubject>>
                                                         studentsPerformancesInSubject) {
        webservice.getStudentsPerformancesInSubject(headerAuthorization, subjectInfoId)
                .enqueue(new Callback<List<StudentPerformanceInSubject>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<StudentPerformanceInSubject>> call, @NotNull Response<List<StudentPerformanceInSubject>> response) {
                        if (response.isSuccessful()) {
                            studentsPerformancesInSubject.postValue(response.body());
                        } else
                            Log.e("ERROR_CODE", String.valueOf(response.code()));
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<StudentPerformanceInSubject>> call, @NotNull Throwable t) {
                        Log.e("ERROR", t.getMessage());
                    }
                });
    }

    public void getStudentPerformanceInModules(long studentPerformanceInSubjectId, MutableLiveData<Map<String, StudentPerformanceInModule>> studentPerformanceInModules) {
        webservice.getStudentPerformanceInModules(headerAuthorization, studentPerformanceInSubjectId)
                .enqueue(new Callback<Map<String, StudentPerformanceInModule>>() {
                    @Override
                    public void onResponse(@NotNull Call<Map<String, StudentPerformanceInModule>> call, @NotNull Response<Map<String, StudentPerformanceInModule>> response) {
                        if (response.isSuccessful()) {
                            studentPerformanceInModules.postValue(response.body());
                        } else
                            Log.e("ERROR_CODE", String.valueOf(response.code()));
                    }

                    @Override
                    public void onFailure(@NotNull Call<Map<String, StudentPerformanceInModule>> call, @NotNull Throwable t) {
                        Log.e("ERROR", t.getMessage());
                    }
                });
    }

    public void getStudentsPerformancesInModules(long subjectInfoId,
                                                 MutableLiveData<Map<String, List<StudentPerformanceInModule>>> studentsPerformancesInModules) {
        webservice.getStudentsPerformancesInModules(headerAuthorization, subjectInfoId)
                .enqueue(new Callback<Map<String, List<StudentPerformanceInModule>>>() {
                    @Override
                    public void onResponse(@NotNull Call<Map<String, List<StudentPerformanceInModule>>> call,
                                           @NotNull Response<Map<String, List<StudentPerformanceInModule>>> response) {
                        if (response.isSuccessful()) {
                            studentsPerformancesInModules.postValue(response.body());
                        } else
                            Log.e("ERROR_CODE", String.valueOf(response.code()));
                    }

                    @Override
                    public void onFailure(@NotNull Call<Map<String, List<StudentPerformanceInModule>>> call, @NotNull Throwable t) {
                        Log.e("ERROR", t.getMessage());
                    }
                });
    }

    public void getStudentEvents(long studentPerformanceInSubjectId,
                                 MutableLiveData<Map<String, List<StudentEvent>>> studentEvents) {
        webservice.getStudentEvents(headerAuthorization, studentPerformanceInSubjectId)
                .enqueue(new Callback<Map<String, List<StudentEvent>>>() {
                    @Override
                    public void onResponse(@NotNull Call<Map<String, List<StudentEvent>>> call,
                                           @NotNull Response<Map<String, List<StudentEvent>>> response) {
                        if (response.isSuccessful()) {
                            studentEvents.postValue(response.body());
                        } else
                            Log.e("ERROR_CODE", String.valueOf(response.code()));
                    }

                    @Override
                    public void onFailure(@NotNull Call<Map<String, List<StudentEvent>>> call, @NotNull Throwable t) {
                        Log.e("ERROR", t.getMessage());
                    }
                });
    }

    public void getStudentLessons(long studentPerformanceInSubjectId,
                                  MutableLiveData<Map<String, List<StudentLesson>>> studentLessons) {
        webservice.getStudentLessons(headerAuthorization, studentPerformanceInSubjectId)
                .enqueue(new Callback<Map<String, List<StudentLesson>>>() {
                    @Override
                    public void onResponse(@NotNull Call<Map<String, List<StudentLesson>>> call,
                                           @NotNull Response<Map<String, List<StudentLesson>>> response) {
                        if (response.isSuccessful()) {
                            studentLessons.postValue(response.body());
                        } else
                            Log.e("ERROR_CODE", String.valueOf(response.code()));
                    }

                    @Override
                    public void onFailure(@NotNull Call<Map<String, List<StudentLesson>>> call, @NotNull Throwable t) {
                        Log.e("ERROR", t.getMessage());
                    }
                });
    }

    public void getStudentsEvents(long subjectInfoId,
                                  MutableLiveData<Map<String, Map<String, List<StudentEvent>>>> studentsEvents) {
        webservice.getStudentsEvents(headerAuthorization, subjectInfoId)
                .enqueue(new Callback<Map<String, Map<String, List<StudentEvent>>>>() {
                    @Override
                    public void onResponse(@NotNull Call<Map<String, Map<String, List<StudentEvent>>>> call,
                                           @NotNull Response<Map<String, Map<String, List<StudentEvent>>>> response) {
                        if (response.isSuccessful()) {
                            studentsEvents.postValue(response.body());
                        } else
                            Log.e("ERROR_CODE", String.valueOf(response.code()));
                    }

                    @Override
                    public void onFailure(@NotNull Call<Map<String, Map<String, List<StudentEvent>>>> call, @NotNull Throwable t) {
                        Log.e("ERROR", t.getMessage());
                    }
                });
    }

    public void getStudentsLessons(long subjectInfoId,
                                   MutableLiveData<Map<String, Map<String, List<StudentLesson>>>> studentsLessons) {
        webservice.getStudentsLessons(headerAuthorization, subjectInfoId)
                .enqueue(new Callback<Map<String, Map<String, List<StudentLesson>>>>() {
                    @Override
                    public void onResponse(@NotNull Call<Map<String, Map<String, List<StudentLesson>>>> call,
                                           @NotNull Response<Map<String, Map<String, List<StudentLesson>>>> response) {
                        if (response.isSuccessful()) {
                            studentsLessons.postValue(response.body());
                        } else
                            Log.e("ERROR_CODE", String.valueOf(response.code()));
                    }

                    @Override
                    public void onFailure(@NotNull Call<Map<String, Map<String, List<StudentLesson>>>> call, @NotNull Throwable t) {
                        Log.e("ERROR", t.getMessage());
                    }
                });
    }

    public void getGroupById(long groupId, MutableLiveData<Group> group) {
        webservice.getGroupById(headerAuthorization, groupId)
                .enqueue(new Callback<Group>() {
                    @Override
                    public void onResponse(@NotNull Call<Group> call, @NotNull Response<Group> response) {
                        if (response.isSuccessful()) {
                            group.postValue(response.body());
                        } else
                            Log.e("ERROR_CODE", String.valueOf(response.code()));
                    }

                    @Override
                    public void onFailure(@NotNull Call<Group> call, @NotNull Throwable t) {
                        Log.e("ERROR", t.getMessage());
                    }
                });
    }

    public void getSubjectInfoById(long subjectInfoId, MutableLiveData<SubjectInfo> subjectInfo) {
        webservice.getSubjectInfoById(headerAuthorization, subjectInfoId)
                .enqueue(new Callback<SubjectInfo>() {
                    @Override
                    public void onResponse(@NotNull Call<SubjectInfo> call, @NotNull Response<SubjectInfo> response) {
                        if (response.isSuccessful()) {
                            subjectInfo.postValue(response.body());
                        } else
                            Log.e("ERROR_CODE", String.valueOf(response.code()));
                    }

                    @Override
                    public void onFailure(@NotNull Call<SubjectInfo> call, @NotNull Throwable t) {
                        Log.e("ERROR", t.getMessage());
                    }
                });
    }

    public void getModuleById(long moduleId, MutableLiveData<Module> module) {
        webservice.getModuleById(headerAuthorization, moduleId)
                .enqueue(new Callback<Module>() {
                    @Override
                    public void onResponse(@NotNull Call<Module> call, @NotNull Response<Module> response) {
                        if (response.isSuccessful()) {
                            module.postValue(response.body());
                        } else
                            Log.e("ERROR_CODE", String.valueOf(response.code()));
                    }

                    @Override
                    public void onFailure(@NotNull Call<Module> call, @NotNull Throwable t) {
                        Log.e("ERROR", t.getMessage());
                    }
                });
    }

    public void getStudentPerformanceInSubjectById(long studentPerformanceInSubjectId, MutableLiveData<StudentPerformanceInSubject> studentPerformanceInSubject) {
        webservice.getStudentPerformanceInSubjectById(headerAuthorization, studentPerformanceInSubjectId)
                .enqueue(new Callback<StudentPerformanceInSubject>() {
                    @Override
                    public void onResponse(@NotNull Call<StudentPerformanceInSubject> call, @NotNull Response<StudentPerformanceInSubject> response) {
                        if (response.isSuccessful()) {
                            studentPerformanceInSubject.postValue(response.body());
                        } else
                            Log.e("ERROR_CODE", String.valueOf(response.code()));
                    }

                    @Override
                    public void onFailure(@NotNull Call<StudentPerformanceInSubject> call, @NotNull Throwable t) {
                        Log.e("ERROR", t.getMessage());
                    }
                });
    }

    public void getSubjectById(long subjectId, MutableLiveData<Subject> subject) {
        webservice.getSubjectById(headerAuthorization, subjectId)
                .enqueue(new Callback<Subject>() {
                    @Override
                    public void onResponse(@NotNull Call<Subject> call, @NotNull Response<Subject> response) {
                        if (response.isSuccessful()) {
                            subject.postValue(response.body());
                        } else
                            Log.e("ERROR_CODE", String.valueOf(response.code()));
                    }

                    @Override
                    public void onFailure(@NotNull Call<Subject> call, @NotNull Throwable t) {
                        Log.e("ERROR", t.getMessage());
                    }
                });
    }

    public void getStudentById(long studentId, MutableLiveData<Result<Student>> student) {
        webservice.getStudentById(headerAuthorization, studentId).enqueue(new Callback<Student>() {
            @Override
            public void onResponse(@NotNull Call<Student> call, @NotNull Response<Student> response) {
                if (response.isSuccessful()) {
                    student.postValue(new Result.Success<>(response.body()));
                } else {
                    student.postValue(new Result.Error("Error_code: " + response.code()));
                    Log.e("ERROR_CODE", String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(@NotNull Call<Student> call, @NotNull Throwable t) {
                student.postValue(new Result.Error("Error: " + t.getMessage()));
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    public void getProfessorById(long professorId, MutableLiveData<Result<Professor>> professor) {
        webservice.getProfessorById(headerAuthorization, professorId).enqueue(new Callback<Professor>() {
            @Override
            public void onResponse(@NotNull Call<Professor> call, @NotNull Response<Professor> response) {
                if (response.isSuccessful()) {
                    professor.postValue(new Result.Success<>(response.body()));
                } else {
                    professor.postValue(new Result.Error("Error_code: " + response.code()));
                    Log.e("ERROR_CODE", String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(@NotNull Call<Professor> call, @NotNull Throwable t) {
                professor.postValue(new Result.Error("Error: " + t.getMessage()));
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    public void getEventById(long eventId, MutableLiveData<Event> event) {
        webservice.getEventById(headerAuthorization, eventId)
                .enqueue(new Callback<Event>() {
                    @Override
                    public void onResponse(@NotNull Call<Event> call, @NotNull Response<Event> response) {
                        if (response.isSuccessful()) {
                            event.postValue(response.body());
                        } else
                            Log.e("ERROR_CODE", String.valueOf(response.code()));
                    }

                    @Override
                    public void onFailure(@NotNull Call<Event> call, @NotNull Throwable t) {
                        Log.e("ERROR", t.getMessage());
                    }
                });
    }

    public void getLessonById(long lessonId, MutableLiveData<Lesson> lesson) {
        webservice.getLessonById(headerAuthorization, lessonId)
                .enqueue(new Callback<Lesson>() {
                    @Override
                    public void onResponse(@NotNull Call<Lesson> call, @NotNull Response<Lesson> response) {
                        if (response.isSuccessful()) {
                            lesson.postValue(response.body());
                        } else
                            Log.e("ERROR_CODE", String.valueOf(response.code()));
                    }

                    @Override
                    public void onFailure(@NotNull Call<Lesson> call, @NotNull Throwable t) {
                        Log.e("ERROR", t.getMessage());
                    }
                });
    }

    public void getSemesterById(long semesterId, MutableLiveData<Semester> semester) {
        webservice.getSemesterById(headerAuthorization, semesterId)
                .enqueue(new Callback<Semester>() {
                    @Override
                    public void onResponse(@NotNull Call<Semester> call, @NotNull Response<Semester> response) {
                        if (response.isSuccessful()) {
                            semester.postValue(response.body());
                        } else
                            Log.e("ERROR_CODE", String.valueOf(response.code()));
                    }

                    @Override
                    public void onFailure(@NotNull Call<Semester> call, @NotNull Throwable t) {
                        Log.e("ERROR", t.getMessage());
                    }
                });
    }

    public void getStudentEventById(long studentEventId, MutableLiveData<StudentEvent> studentEvent) {
        webservice.getStudentEventById(headerAuthorization, studentEventId)
                .enqueue(new Callback<StudentEvent>() {
                    @Override
                    public void onResponse(@NotNull Call<StudentEvent> call, @NotNull Response<StudentEvent> response) {
                        if (response.isSuccessful()) {
                            studentEvent.postValue(response.body());
                        } else
                            Log.e("ERROR_CODE", String.valueOf(response.code()));
                    }

                    @Override
                    public void onFailure(@NotNull Call<StudentEvent> call, @NotNull Throwable t) {
                        Log.e("ERROR", t.getMessage());
                    }
                });
    }

    public void getStudentLessonById(long studentLessonId, MutableLiveData<StudentLesson> studentLesson) {
        webservice.getStudentLessonById(headerAuthorization, studentLessonId)
                .enqueue(new Callback<StudentLesson>() {
                    @Override
                    public void onResponse(@NotNull Call<StudentLesson> call, @NotNull Response<StudentLesson> response) {
                        if (response.isSuccessful()) {
                            studentLesson.postValue(response.body());
                        } else
                            Log.e("ERROR_CODE", String.valueOf(response.code()));
                    }

                    @Override
                    public void onFailure(@NotNull Call<StudentLesson> call, @NotNull Throwable t) {
                        Log.e("ERROR", t.getMessage());
                    }
                });
    }

    public void addStudent(Student student, MutableLiveData<Boolean> answer) {
        webservice.addStudent(headerAuthorization, student).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if (response.isSuccessful()) {
                    answer.postValue(true);
                } else
                    Log.e("ERROR_CODE", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    public void addProfessor(Professor professor, MutableLiveData<Boolean> answer) {
        webservice.addProfessor(headerAuthorization, professor).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if (response.isSuccessful()) {
                    answer.postValue(true);
                } else
                    Log.e("ERROR_CODE", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    public void addGroup(Group group, MutableLiveData<Boolean> answer) {
        webservice.addGroup(headerAuthorization, group).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if (response.isSuccessful()) {
                    answer.postValue(true);
                } else
                    Log.e("ERROR_CODE", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    public void addSubject(Subject subject, MutableLiveData<Boolean> answer) {
        webservice.addSubject(headerAuthorization, subject).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if (response.isSuccessful()) {
                    answer.postValue(true);
                } else
                    Log.e("ERROR_CODE", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    public void addSemester(Semester semester, MutableLiveData<Boolean> answer) {
        webservice.addSemester(headerAuthorization, semester).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if (response.isSuccessful()) {
                    answer.postValue(true);
                } else
                    Log.e("ERROR_CODE", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    public void addSubjectInfo(SubjectInfo subjectInfo, MutableLiveData<Boolean> answer) {
        webservice.addSubjectInfo(headerAuthorization, subjectInfo).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if (response.isSuccessful()) {
                    answer.postValue(true);
                } else
                    Log.e("ERROR_CODE", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    public void addEvent(Event event, MutableLiveData<Boolean> answer) {
        webservice.addEvent(headerAuthorization, event).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if (response.isSuccessful()) {
                    answer.postValue(true);
                } else
                    Log.e("ERROR_CODE", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    public void getLastNumberOfEventType(long subjectInfoId, int type, MutableLiveData<Integer> lastNumberOfEventType) {
        webservice.getLastNumberOfEventType(headerAuthorization, subjectInfoId, type).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(@NotNull Call<Integer> call, @NotNull Response<Integer> response) {
                if (response.isSuccessful()) {
                    lastNumberOfEventType.postValue(response.body());
                } else
                    Log.e("ERROR_CODE", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(@NotNull Call<Integer> call, @NotNull Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    public void addLesson(Lesson lesson, MutableLiveData<Boolean> answer) {
        webservice.addLesson(headerAuthorization, lesson).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if (response.isSuccessful()) {
                    answer.postValue(true);
                } else
                    Log.e("ERROR_CODE", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    public void addStudentEvent(StudentEvent studentEvent, MutableLiveData<Boolean> answer) {
        webservice.addStudentEvent(headerAuthorization, studentEvent).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if (response.isSuccessful()) {
                    answer.postValue(true);
                } else
                    Log.e("ERROR_CODE", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    public void addStudentLesson(StudentLesson studentLesson, MutableLiveData<Boolean> answer) {
        webservice.addStudentLesson(headerAuthorization, studentLesson).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if (response.isSuccessful()) {
                    answer.postValue(true);
                } else
                    Log.e("ERROR_CODE", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    public void editStudent(long id, Student student, MutableLiveData<Boolean> answer) {
        webservice.editStudent(headerAuthorization, id, student).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if (response.isSuccessful()) {
                    answer.postValue(true);
                } else
                    Log.e("ERROR_CODE", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    public void editProfessor(long id, Professor professor, MutableLiveData<Boolean> answer) {
        webservice.editProfessor(headerAuthorization, id, professor).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if (response.isSuccessful()) {
                    answer.postValue(true);
                } else
                    Log.e("ERROR_CODE", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    public void editGroup(long id, Group group, MutableLiveData<Boolean> answer) {
        webservice.editGroup(headerAuthorization, id, group).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if (response.isSuccessful()) {
                    answer.postValue(true);
                } else
                    Log.e("ERROR_CODE", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    public void editSubject(long id, Subject subject, MutableLiveData<Boolean> answer) {
        webservice.editSubject(headerAuthorization, id, subject).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if (response.isSuccessful()) {
                    answer.postValue(true);
                } else
                    Log.e("ERROR_CODE", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    public void editSemester(long id, Semester semester, MutableLiveData<Boolean> answer) {
        webservice.editSemester(headerAuthorization, id, semester).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if (response.isSuccessful()) {
                    answer.postValue(true);
                } else
                    Log.e("ERROR_CODE", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    public void editSubjectInfo(long id, SubjectInfo subjectInfo, MutableLiveData<Boolean> answer) {
        webservice.editSubjectInfo(headerAuthorization, id, subjectInfo).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if (response.isSuccessful()) {
                    answer.postValue(true);
                } else
                    Log.e("ERROR_CODE", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    public void editModule(long id, Module module, MutableLiveData<Boolean> answer) {
        webservice.editModule(headerAuthorization, id, module).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if (response.isSuccessful()) {
                    answer.postValue(true);
                } else
                    Log.e("ERROR_CODE", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    public void editEvent(long id, Event event, MutableLiveData<Boolean> answer) {
        webservice.editEvent(headerAuthorization, id, event).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if (response.isSuccessful()) {
                    answer.postValue(true);
                } else
                    Log.e("ERROR_CODE", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    public void editLesson(long id, Lesson lesson, MutableLiveData<Boolean> answer) {
        webservice.editLesson(headerAuthorization, id, lesson).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if (response.isSuccessful()) {
                    answer.postValue(true);
                } else
                    Log.e("ERROR_CODE", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    public void editStudentPerformanceInSubject(long id, StudentPerformanceInSubject studentPerformanceInSubject, MutableLiveData<Boolean> answer) {
        webservice.editStudentPerformanceInSubject(headerAuthorization, id, studentPerformanceInSubject).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if (response.isSuccessful()) {
                    answer.postValue(true);
                } else
                    Log.e("ERROR_CODE", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    public void editStudentEvent(long id, StudentEvent studentEvent, MutableLiveData<Boolean> answer) {
        webservice.editStudentEvent(headerAuthorization, id, studentEvent).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if (response.isSuccessful()) {
                    answer.postValue(true);
                } else
                    Log.e("ERROR_CODE", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    public void editStudentLesson(long id, StudentLesson studentLesson, MutableLiveData<Boolean> answer) {
        webservice.editStudentLesson(headerAuthorization, id, studentLesson).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if (response.isSuccessful()) {
                    answer.postValue(true);
                } else
                    Log.e("ERROR_CODE", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    public void deleteStudent(long id, MutableLiveData<Boolean> answer) {
        webservice.deleteStudent(headerAuthorization, id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if (response.isSuccessful()) {
                    answer.postValue(true);
                } else
                    Log.e("ERROR_CODE", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    public void deleteProfessor(long id, MutableLiveData<Boolean> answer) {
        webservice.deleteProfessor(headerAuthorization, id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if (response.isSuccessful()) {
                    answer.postValue(true);
                } else
                    Log.e("ERROR_CODE", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    public void deleteGroup(long id, MutableLiveData<Boolean> answer) {
        webservice.deleteGroup(headerAuthorization, id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if (response.isSuccessful()) {
                    answer.postValue(true);
                } else
                    Log.e("ERROR_CODE", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    public void deleteSubject(long id, MutableLiveData<Boolean> answer) {
        webservice.deleteSubject(headerAuthorization, id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if (response.isSuccessful()) {
                    answer.postValue(true);
                } else
                    Log.e("ERROR_CODE", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    public void deleteSemester(long id, MutableLiveData<Boolean> answer) {
        webservice.deleteSemester(headerAuthorization, id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if (response.isSuccessful()) {
                    answer.postValue(true);
                } else
                    Log.e("ERROR_CODE", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    public void deleteSubjectInfo(long id, long professorId, MutableLiveData<Boolean> answer) {
        webservice.deleteSubjectInfo(headerAuthorization, id, professorId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if (response.isSuccessful()) {
                    answer.postValue(true);
                } else
                    Log.e("ERROR_CODE", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    public void deleteEvent(long id, MutableLiveData<Boolean> answer) {
        webservice.deleteEvent(headerAuthorization, id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if (response.isSuccessful()) {
                    answer.postValue(true);
                } else
                    Log.e("ERROR_CODE", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    public void deleteLesson(long id, MutableLiveData<Boolean> answer) {
        webservice.deleteLesson(headerAuthorization, id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if (response.isSuccessful()) {
                    answer.postValue(true);
                } else
                    Log.e("ERROR_CODE", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    public void deleteStudentEvent(long id, MutableLiveData<Boolean> answer) {
        webservice.deleteStudentEvent(headerAuthorization, id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if (response.isSuccessful()) {
                    answer.postValue(true);
                } else
                    Log.e("ERROR_CODE", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    public void deleteStudentLesson(long id, MutableLiveData<Boolean> answer) {
        webservice.deleteStudentLesson(headerAuthorization, id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if (response.isSuccessful()) {
                    answer.postValue(true);
                } else
                    Log.e("ERROR_CODE", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }
}