package com.example.electronicdiary.admin.adding;

import androidx.annotation.Nullable;

import com.example.electronicdiary.R;

public class ProfessorAddingFormState {
    @Nullable
    private final Integer professorNameError;
    @Nullable
    private final Integer professorSecondNameError;
    @Nullable
    private final Integer professorLoginError;
    @Nullable
    private final Integer professorPasswordError;
    private final boolean isNameOrSecondNameChanged;
    private final boolean isDataValid;

    public ProfessorAddingFormState(String professorName, String professorSecondName, String professorLogin, String professorPassword,
                                    boolean isNameOrSecondNameChanged) {
        this.isNameOrSecondNameChanged = isNameOrSecondNameChanged;
        boolean isProfessorNameValid = !professorName.trim().isEmpty();
        boolean isProfessorSecondNameValid = !professorSecondName.trim().isEmpty();
        boolean isProfessorLoginValid = !professorLogin.trim().isEmpty();
        boolean isProfessorPasswordValid = !professorPassword.trim().isEmpty();

        this.professorNameError = !isProfessorNameValid ? R.string.invalid_empty_field : null;
        this.professorSecondNameError = !isProfessorSecondNameValid ? R.string.invalid_empty_field : null;
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