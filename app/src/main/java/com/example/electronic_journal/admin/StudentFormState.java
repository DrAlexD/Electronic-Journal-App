package com.example.electronic_journal.admin;

import androidx.annotation.Nullable;

import com.example.electronic_journal.R;

public class StudentFormState {
    @Nullable
    private final Integer studentLoginError;
    @Nullable
    private final Integer studentPasswordError;
    private final boolean isNameOrSecondNameChanged;
    private final boolean isDataValid;
    @Nullable
    private Integer studentNameError;
    @Nullable
    private Integer studentSecondNameError;

    public StudentFormState(String studentName, String studentSecondName, String studentLogin, String studentPassword,
                            boolean isNameOrSecondNameChanged) {
        this.isNameOrSecondNameChanged = isNameOrSecondNameChanged;
        boolean isStudentNameValid = !studentName.trim().isEmpty();
        boolean isStudentSecondNameValid = !studentSecondName.trim().isEmpty();
        boolean isStudentLoginValid = !studentLogin.trim().isEmpty();
        boolean isStudentPasswordValid = !studentPassword.trim().isEmpty();

        this.studentNameError = !isStudentNameValid ? R.string.invalid_empty_field : null;
        this.studentSecondNameError = !isStudentSecondNameValid ? R.string.invalid_empty_field : null;


        if (isStudentNameValid) {
            isStudentNameValid = studentName.trim().matches("[А-Я][а-я]*");
            this.studentNameError = !isStudentNameValid ? R.string.invalid_name_field : null;
        }

        if (isStudentSecondNameValid) {
            isStudentSecondNameValid = studentSecondName.trim().matches("[А-Я][а-я]*");
            this.studentSecondNameError = !isStudentSecondNameValid ? R.string.invalid_second_name_field : null;
        }

        this.studentLoginError = !isStudentLoginValid ? R.string.invalid_empty_field : null;
        this.studentPasswordError = !isStudentPasswordValid ? R.string.invalid_empty_field : null;
        this.isDataValid = isStudentNameValid && isStudentSecondNameValid && isStudentLoginValid && isStudentPasswordValid;
    }

    @Nullable
    public Integer getStudentNameError() {
        return studentNameError;
    }

    @Nullable
    public Integer getStudentSecondNameError() {
        return studentSecondNameError;
    }

    @Nullable
    public Integer getStudentLoginError() {
        return studentLoginError;
    }

    @Nullable
    public Integer getStudentPasswordError() {
        return studentPasswordError;
    }

    public boolean isNameOrSecondNameChanged() {
        return isNameOrSecondNameChanged;
    }

    public boolean isDataValid() {
        return isDataValid;
    }
}