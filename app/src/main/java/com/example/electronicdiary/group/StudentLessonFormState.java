package com.example.electronicdiary.group;

import androidx.annotation.Nullable;

import com.example.electronicdiary.R;

public class StudentLessonFormState {
    @Nullable
    private final Integer studentLessonEarnedPointsError;
    private final boolean isDataValid;

    public StudentLessonFormState(String studentLessonEarnedPoints) {
        boolean isStudentLessonEarnedPointsValid = !studentLessonEarnedPoints.trim().isEmpty();

        this.studentLessonEarnedPointsError = !isStudentLessonEarnedPointsValid ? R.string.invalid_empty_field : null;
        this.isDataValid = isStudentLessonEarnedPointsValid;
    }

    @Nullable
    public Integer getStudentLessonEarnedPointsError() {
        return studentLessonEarnedPointsError;
    }

    public boolean isDataValid() {
        return isDataValid;
    }
}