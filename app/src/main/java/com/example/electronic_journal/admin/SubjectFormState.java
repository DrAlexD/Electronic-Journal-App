package com.example.electronic_journal.admin;

import androidx.annotation.Nullable;

import com.example.electronic_journal.R;

public class SubjectFormState {
    @Nullable
    private final Integer subjectTitleError;
    private final boolean isDataValid;

    public SubjectFormState(String subjectTitle) {
        boolean isSubjectTitleValid = !subjectTitle.trim().isEmpty();

        this.subjectTitleError = !isSubjectTitleValid ? R.string.invalid_empty_field : null;
        this.isDataValid = isSubjectTitleValid;
    }

    @Nullable
    public Integer getSubjectTitleError() {
        return subjectTitleError;
    }

    public boolean isDataValid() {
        return isDataValid;
    }
}