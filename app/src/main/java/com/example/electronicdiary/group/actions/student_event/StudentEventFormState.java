package com.example.electronicdiary.group.actions.student_event;

import androidx.annotation.Nullable;

import com.example.electronicdiary.R;

public class StudentEventFormState {
    @Nullable
    private final Integer variantNumberError;
    private final boolean isDataValid;

    public StudentEventFormState(String variantNumber) {
        boolean isVariantNumberValid = !variantNumber.trim().isEmpty();

        this.variantNumberError = !isVariantNumberValid ? R.string.invalid_empty_field : null;
        this.isDataValid = isVariantNumberValid;
    }

    @Nullable
    public Integer getVariantNumberError() {
        return variantNumberError;
    }

    public boolean isDataValid() {
        return isDataValid;
    }
}