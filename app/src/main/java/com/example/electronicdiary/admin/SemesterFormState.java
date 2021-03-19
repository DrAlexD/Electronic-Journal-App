package com.example.electronicdiary.admin;

import androidx.annotation.Nullable;

import com.example.electronicdiary.R;

public class SemesterFormState {
    @Nullable
    private final Integer semesterYearError;
    private final boolean isDataValid;

    public SemesterFormState(String semesterYear) {
        boolean isSemesterYearValid = semesterYear.trim().length() == 4;

        this.semesterYearError = !isSemesterYearValid ? R.string.invalid_semester_year : null;
        this.isDataValid = isSemesterYearValid;
    }

    @Nullable
    public Integer getSemesterYearError() {
        return semesterYearError;
    }

    public boolean isDataValid() {
        return isDataValid;
    }
}