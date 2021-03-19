package com.example.electronicdiary.group;

import androidx.annotation.Nullable;

import com.example.electronicdiary.R;

public class StudentEventFormState {
    @Nullable
    private final Integer studentEventEarnedPointsError;
    private final boolean isDataValid;

    public StudentEventFormState(String studentEventEarnedPoints) {
        boolean isStudentEventEarnedPointsValid = !studentEventEarnedPoints.trim().isEmpty();

        this.studentEventEarnedPointsError = !isStudentEventEarnedPointsValid ? R.string.invalid_empty_field : null;
        this.isDataValid = isStudentEventEarnedPointsValid;
    }

    @Nullable
    public Integer getStudentEventEarnedPointsError() {
        return studentEventEarnedPointsError;
    }

    public boolean isDataValid() {
        return isDataValid;
    }
}