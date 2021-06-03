package com.example.electronic_journal.admin;

import androidx.annotation.Nullable;

import com.example.electronic_journal.R;

public class ProfessorFormState {
    @Nullable
    private final Integer professorLoginError;
    @Nullable
    private final Integer professorPasswordError;
    private final boolean isNameOrSecondNameChanged;
    private final boolean isDataValid;
    @Nullable
    private Integer professorNameError;
    @Nullable
    private Integer professorSecondNameError;

    public ProfessorFormState(String professorName, String professorSecondName, String professorLogin, String professorPassword,
                              boolean isNameOrSecondNameChanged) {
        this.isNameOrSecondNameChanged = isNameOrSecondNameChanged;
        boolean isProfessorNameValid = !professorName.trim().isEmpty();
        boolean isProfessorSecondNameValid = !professorSecondName.trim().isEmpty();
        boolean isProfessorLoginValid = !professorLogin.trim().isEmpty();
        boolean isProfessorPasswordValid = !professorPassword.trim().isEmpty();

        this.professorNameError = !isProfessorNameValid ? R.string.invalid_empty_field : null;
        this.professorSecondNameError = !isProfessorSecondNameValid ? R.string.invalid_empty_field : null;

        if (isProfessorNameValid) {
            isProfessorNameValid = professorName.trim().matches("[А-Я][а-я]*");
            this.professorNameError = !isProfessorNameValid ? R.string.invalid_name_field : null;
        }

        if (isProfessorSecondNameValid) {
            isProfessorSecondNameValid = professorSecondName.trim().matches("[А-Я][а-я]*");
            this.professorSecondNameError = !isProfessorSecondNameValid ? R.string.invalid_second_name_field : null;
        }

        this.professorLoginError = !isProfessorLoginValid ? R.string.invalid_empty_field : null;
        this.professorPasswordError = !isProfessorPasswordValid ? R.string.invalid_empty_field : null;
        this.isDataValid = isProfessorNameValid && isProfessorSecondNameValid && isProfessorLoginValid && isProfessorPasswordValid;
    }

    @Nullable
    public Integer getProfessorNameError() {
        return professorNameError;
    }

    @Nullable
    public Integer getProfessorSecondNameError() {
        return professorSecondNameError;
    }

    @Nullable
    public Integer getProfessorLoginError() {
        return professorLoginError;
    }

    @Nullable
    public Integer getProfessorPasswordError() {
        return professorPasswordError;
    }

    public boolean isNameOrSecondNameChanged() {
        return isNameOrSecondNameChanged;
    }

    public boolean isDataValid() {
        return isDataValid;
    }
}