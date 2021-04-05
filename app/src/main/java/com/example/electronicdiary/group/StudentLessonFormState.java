package com.example.electronicdiary.group;

import androidx.annotation.Nullable;

import com.example.electronicdiary.R;

public class StudentLessonFormState {
    @Nullable
    private final Integer bonusPointsError;
    private final boolean isDataValid;

    public StudentLessonFormState(String bonusPoints) {
        boolean isBonusPointsValid = !bonusPoints.trim().isEmpty();

        this.bonusPointsError = !isBonusPointsValid ? R.string.invalid_empty_field : null;
        this.isDataValid = isBonusPointsValid;
    }

    @Nullable
    public Integer getBonusPointsError() {
        return bonusPointsError;
    }

    public boolean isDataValid() {
        return isDataValid;
    }
}