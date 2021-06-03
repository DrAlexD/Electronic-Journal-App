package com.example.electronic_journal;

import com.example.electronic_journal.data_classes.Event;
import com.example.electronic_journal.data_classes.Group;
import com.example.electronic_journal.data_classes.Lesson;
import com.example.electronic_journal.data_classes.Module;
import com.example.electronic_journal.data_classes.Professor;
import com.example.electronic_journal.data_classes.Semester;
import com.example.electronic_journal.data_classes.Student;
import com.example.electronic_journal.data_classes.StudentEvent;
import com.example.electronic_journal.data_classes.StudentLesson;
import com.example.electronic_journal.data_classes.StudentPerformanceInModule;
import com.example.electronic_journal.data_classes.StudentPerformanceInSubject;
import com.example.electronic_journal.data_classes.Subject;
import com.example.electronic_journal.data_classes.SubjectInfo;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Webservice {
    @POST("auth/signin")
    Call<JwtResponse> authenticateUser(@Body LoginRequest loginRequest);

    @GET("students")
    Call<List<Student>> getAllStudents(@Header("Authorization") String authorization);

    @GET("professors")
    Call<List<Professor>> getProfessors(@Header("Authorization") String authorization);

    @GET("groups")
    Call<List<Group>> getAllGroups(@Header("Authorization") String authorization);

    @GET("subjects")
    Call<List<Subject>> getAllSubjects(@Header("Authorization") String authorization);

    @GET("semesters")
    Call<List<Semester>> getSemesters(@Header("Authorization") String authorization);

    @GET("students")
    Call<List<Student>> getStudentsInGroup(@Header("Authorization") String authorization, @Query("groupId") long groupId);

    @GET("available-students")
    Call<List<Student>> getAvailableStudents(@Header("Authorization") String authorization,
                                             @Query("professorId") long professorId, @Query("semesterId") long semesterId);

    @GET("available-subjects")
    Call<List<Subject>> getAvailableSubjects(@Header("Authorization") String authorization,
                                             @Query("professorId") long professorId, @Query("semesterId") long semesterId);

    @GET("available-groups")
    Call<List<SubjectInfo>> getAvailableGroupsInSubject(@Header("Authorization") String authorization,
                                                        @Query("professorId") long professorId, @Query("semesterId") long semesterId,
                                                        @Query("subjectId") long subjectId);

    @GET("available-subjects-with-groups")
    Call<Map<String, List<SubjectInfo>>> getAvailableSubjectsWithGroups(@Header("Authorization") String authorization,
                                                                        @Query("professorId") long professorId, @Query("semesterId") long semesterId);

    @GET("available-student-subjects")
    Call<List<StudentPerformanceInSubject>> getAvailableStudentSubjects(@Header("Authorization") String authorization,
                                                                        @Query("studentId") long studentId, @Query("semesterId") long semesterId);

    @GET("modules")
    Call<Map<String, Module>> getModules(@Header("Authorization") String authorization, @Query("subjectInfoId") long subjectInfoId);

    @GET("events")
    Call<Map<String, List<Event>>> getEvents(@Header("Authorization") String authorization, @Query("subjectInfoId") long subjectInfoId);

    @GET("last-number-of-event-type")
    Call<Integer> getLastNumberOfEventType(@Header("Authorization") String authorization, @Query("subjectInfoId") long subjectInfoId,
                                           @Query("type") int type);

    @GET("lessons")
    Call<Map<String, List<Lesson>>> getLessons(@Header("Authorization") String authorization, @Query("subjectInfoId") long subjectInfoId);


    @GET("lessons")
    Call<Map<String, List<Lesson>>> getSpecificLessons(@Header("Authorization") String authorization, @Query("subjectInfoId") long subjectInfoId,
                                                       @Query("isLecture") boolean isLecture);

    @GET("students-performances-in-subject")
    Call<List<StudentPerformanceInSubject>> getStudentsPerformancesInSubject(@Header("Authorization") String authorization, @Query("subjectInfoId") long subjectInfoId);

    @GET("student-performance-in-modules")
    Call<Map<String, StudentPerformanceInModule>> getStudentPerformanceInModules(@Header("Authorization") String authorization,
                                                                                 @Query("studentPerformanceInSubjectId") long studentPerformanceInSubjectId);

    @GET("students-performances-in-modules")
    Call<Map<String, List<StudentPerformanceInModule>>> getStudentsPerformancesInModules(@Header("Authorization") String authorization, @Query("subjectInfoId") long subjectInfoId);

    @GET("student-events")
    Call<Map<String, List<StudentEvent>>> getStudentEvents(@Header("Authorization") String authorization,
                                                           @Query("studentPerformanceInSubjectId") long studentPerformanceInSubjectId);

    @GET("student-lessons")
    Call<Map<String, List<StudentLesson>>> getStudentLessons(@Header("Authorization") String authorization,
                                                             @Query("studentPerformanceInSubjectId") long studentPerformanceInSubjectId);

    @GET("students-events")
    Call<Map<String, Map<String, List<StudentEvent>>>> getStudentsEvents(@Header("Authorization") String authorization, @Query("subjectInfoId") long subjectInfoId);

    @GET("students-lessons")
    Call<Map<String, Map<String, List<StudentLesson>>>> getStudentsLessons(@Header("Authorization") String authorization, @Query("subjectInfoId") long subjectInfoId);

    @GET("groups/{id}")
    Call<Group> getGroupById(@Header("Authorization") String authorization, @Path("id") long id);

    @GET("subjects-info/{id}")
    Call<SubjectInfo> getSubjectInfoById(@Header("Authorization") String authorization, @Path("id") long id);

    @GET("modules/{id}")
    Call<Module> getModuleById(@Header("Authorization") String authorization, @Path("id") long id);

    @GET("students-performances-in-subject/{id}")
    Call<StudentPerformanceInSubject> getStudentPerformanceInSubjectById(@Header("Authorization") String authorization, @Path("id") long id);

    @GET("subjects/{id}")
    Call<Subject> getSubjectById(@Header("Authorization") String authorization, @Path("id") long id);

    @GET("students/{id}")
    Call<Student> getStudentById(@Header("Authorization") String authorization, @Path("id") long id);

    @GET("professors/{id}")
    Call<Professor> getProfessorById(@Header("Authorization") String authorization, @Path("id") long id);

    @GET("events/{id}")
    Call<Event> getEventById(@Header("Authorization") String authorization, @Path("id") long id);

    @GET("lessons/{id}")
    Call<Lesson> getLessonById(@Header("Authorization") String authorization, @Path("id") long id);

    @GET("semesters/{id}")
    Call<Semester> getSemesterById(@Header("Authorization") String authorization, @Path("id") long id);

    @GET("students-events/{id}")
    Call<StudentEvent> getStudentEventById(@Header("Authorization") String authorization, @Path("id") long id);

    @GET("students-lessons/{id}")
    Call<StudentLesson> getStudentLessonById(@Header("Authorization") String authorization, @Path("id") long id);

    @POST("students")
    Call<Void> addStudent(@Header("Authorization") String authorization, @Body Student student);

    @POST("professors")
    Call<Void> addProfessor(@Header("Authorization") String authorization, @Body Professor professor);

    @POST("groups")
    Call<Void> addGroup(@Header("Authorization") String authorization, @Body Group group);

    @POST("subjects")
    Call<Void> addSubject(@Header("Authorization") String authorization, @Body Subject subject);

    @POST("semesters")
    Call<Void> addSemester(@Header("Authorization") String authorization, @Body Semester semester);

    @POST("subjects-info")
    Call<Void> addSubjectInfo(@Header("Authorization") String authorization, @Body SubjectInfo subjectInfo);

    @POST("events")
    Call<Integer> addEvent(@Header("Authorization") String authorization, @Body Event event);

    @POST("lessons")
    Call<Void> addLesson(@Header("Authorization") String authorization, @Body Lesson lesson);

    @POST("students-events")
    Call<Void> addStudentEvent(@Header("Authorization") String authorization, @Body StudentEvent studentEvent);

    @POST("students-lessons")
    Call<Void> addStudentLesson(@Header("Authorization") String authorization, @Body StudentLesson studentLesson);

    @PUT("students/{id}")
    Call<Void> editStudent(@Header("Authorization") String authorization, @Path("id") long id, @Body Student student);

    @PUT("change-group-in-subjects-info/{fromGroupId}/{toGroupId}")
    Call<Void> changeGroupInSubjectsInfo(@Header("Authorization") String authorization, @Path("fromGroupId") long fromGroupId, @Path("toGroupId") long toGroupId);

    @PUT("professors/{id}")
    Call<Void> editProfessor(@Header("Authorization") String authorization, @Path("id") long id, @Body Professor professor);

    @PUT("groups/{id}")
    Call<Void> editGroup(@Header("Authorization") String authorization, @Path("id") long id, @Body Group group);

    @PUT("subjects/{id}")
    Call<Void> editSubject(@Header("Authorization") String authorization, @Path("id") long id, @Body Subject subject);

    @PUT("semesters/{id}")
    Call<Void> editSemester(@Header("Authorization") String authorization, @Path("id") long id, @Body Semester semester);

    @PUT("subjects-info/{id}")
    Call<Void> editSubjectInfo(@Header("Authorization") String authorization, @Path("id") long id, @Body SubjectInfo subjectInfo);

    @PUT("modules/{id}")
    Call<Integer> editModule(@Header("Authorization") String authorization, @Path("id") long id, @Body Module module);

    @PUT("events/{id}")
    Call<Integer> editEvent(@Header("Authorization") String authorization, @Path("id") long id, @Body Event event);

    @PUT("lessons/{id}")
    Call<Void> editLesson(@Header("Authorization") String authorization, @Path("id") long id, @Body Lesson lesson);

    @PUT("students-performances-in-subject/{id}")
    Call<Void> editStudentPerformanceInSubject(@Header("Authorization") String authorization, @Path("id") long id, @Body StudentPerformanceInSubject studentPerformanceInSubject);

    @PUT("students-events/{id}")
    Call<Void> editStudentEvent(@Header("Authorization") String authorization, @Path("id") long id, @Body StudentEvent studentEvent);

    @PUT("students-lessons/{id}")
    Call<Void> editStudentLesson(@Header("Authorization") String authorization, @Path("id") long id, @Body StudentLesson studentLesson);

    @DELETE("students/{id}")
    Call<Void> deleteStudent(@Header("Authorization") String authorization, @Path("id") long id);

    @DELETE("professors/{id}")
    Call<Void> deleteProfessor(@Header("Authorization") String authorization, @Path("id") long id);

    @DELETE("groups/{id}")
    Call<Void> deleteGroup(@Header("Authorization") String authorization, @Path("id") long id);

    @DELETE("subjects/{id}")
    Call<Void> deleteSubject(@Header("Authorization") String authorization, @Path("id") long id);

    @DELETE("semesters/{id}")
    Call<Void> deleteSemester(@Header("Authorization") String authorization, @Path("id") long id);

    @DELETE("subjects-info/{id}")
    Call<Void> deleteSubjectInfo(@Header("Authorization") String authorization, @Path("id") long id,
                                 @Query("professorId") long professorId);

    @DELETE("events/{id}")
    Call<Void> deleteEvent(@Header("Authorization") String authorization, @Path("id") long id);

    @DELETE("lessons/{id}")
    Call<Void> deleteLesson(@Header("Authorization") String authorization, @Path("id") long id);

    @DELETE("students-events/{id}")
    Call<Void> deleteStudentEvent(@Header("Authorization") String authorization, @Path("id") long id);

    @DELETE("students-lessons/{id}")
    Call<Void> deleteStudentLesson(@Header("Authorization") String authorization, @Path("id") long id);
}
