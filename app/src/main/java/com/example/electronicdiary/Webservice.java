package com.example.electronicdiary;

import com.example.electronicdiary.data_classes.Professor;
import com.example.electronicdiary.data_classes.Student;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Webservice {
    @POST("auth/signin")
    Call<JwtResponse> authenticateUser(@Body LoginRequest loginRequest);

    @GET("students")
    Call<List<Student>> getAllStudents(@Header("Authorization") String authorization);

    @GET("students/{id}")
    Call<Student> getStudentById(@Header("Authorization") String authorization, @Path("id") long id);

    @GET("professors")
    Call<List<Professor>> getProfessors(@Header("Authorization") String authorization);

    @GET("professors/{id}")
    Call<Professor> getProfessorById(@Header("Authorization") String authorization, @Path("id") long id);
}
