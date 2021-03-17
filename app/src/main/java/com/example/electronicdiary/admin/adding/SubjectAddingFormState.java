package com.example.electronicdiary.admin.adding;

import androidx.annotation.Nullable;

import com.example.electronicdiary.R;

public class SubjectAddingFormState {
    @Nullable
    private final Integer subjectTitleError;
    private final boolean isDataValid;

    public SubjectAddingFormState(String subjectTitle) {
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