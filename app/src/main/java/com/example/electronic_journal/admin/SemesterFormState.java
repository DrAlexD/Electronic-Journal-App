package com.example.electronic_journal.admin;

import androidx.annotation.Nullable;

import com.example.electronic_journal.R;

public class SemesterFormState {
    @Nullable
    private final Integer semesterYearError;
    private final boolean isDataValid;

    public SemesterFormState(String semesterYear, Boolean isFirstHalf, Boolean isSecondHalf) {
        boolean isSemesterYearValid = semesterYear.trim().matches("20[2-3][0-9]");
        boolean isHalfValid = isFirstHalf || isSecondHalf;

        this.semesterYearError = !isSemesterYearValid ? R.string.invalid_semester_year : null;
        this.isDataValid = isSemesterYearValid && isHalfValid;
    }

    @Nullable
    public Integer getSemesterYearError() {
        return semesterYearError;
    }

    public boolean isDataValid() {
        return isDataValid;
    }
}