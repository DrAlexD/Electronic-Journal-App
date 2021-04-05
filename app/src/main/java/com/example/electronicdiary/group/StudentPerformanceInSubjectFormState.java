package com.example.electronicdiary.group;

import androidx.annotation.Nullable;

import com.example.electronicdiary.R;

public class StudentPerformanceInSubjectFormState {
    @Nullable
    private final Integer earnedPointsError;
    @Nullable
    private final Integer bonusPointsError;
    private final boolean isDataValid;

    @Nullable
    private Integer examEarnedPointsError;
    @Nullable
    private Integer markError;

    public StudentPerformanceInSubjectFormState(String earnedPoints, String bonusPoints) {
        boolean isEarnedPointsValid = !earnedPoints.trim().isEmpty();
        boolean isBonusPointsValid = !bonusPoints.trim().isEmpty();

        this.earnedPointsError = !isEarnedPointsValid ? R.string.invalid_empty_field : null;
        this.bonusPointsError = !isBonusPointsValid ? R.string.invalid_empty_field : null;
        this.isDataValid = isEarnedPointsValid && isBonusPointsValid;
    }

    public StudentPerformanceInSubjectFormState(String earnedPoints, String bonusPoints, String mark) {
        boolean isEarnedPointsValid = !earnedPoints.trim().isEmpty();
        boolean isBonusPointsValid = !bonusPoints.trim().isEmpty();
        boolean isMarkValid = !mark.trim().isEmpty();

        this.earnedPointsError = !isEarnedPointsValid ? R.string.invalid_empty_field : null;
        this.bonusPointsError = !isBonusPointsValid ? R.string.invalid_empty_field : null;
        this.markError = !isMarkValid ? R.string.invalid_empty_field : null;
        this.isDataValid = isEarnedPointsValid && isBonusPointsValid && isMarkValid;
    }

    public StudentPerformanceInSubjectFormState(String earnedPoints, String bonusPoints, String earnedExamPoints, String mark) {
        boolean isEarnedPointsValid = !earnedPoints.trim().isEmpty();
        boolean isBonusPointsValid = !bonusPoints.trim().isEmpty();
        boolean isExamEarnedPointsValid = !earnedExamPoints.trim().isEmpty();
        boolean isMarkValid = !mark.trim().isEmpty();

        this.earnedPointsError = !isEarnedPointsValid ? R.string.invalid_empty_field : null;
        this.bonusPointsError = !isBonusPointsValid ? R.string.invalid_empty_field : null;
        this.examEarnedPointsError = !isExamEarnedPointsValid ? R.string.invalid_empty_field : null;
        this.markError = !isMarkValid ? R.string.invalid_empty_field : null;
        this.isDataValid = isEarnedPointsValid && isBonusPointsValid && isExamEarnedPointsValid && isMarkValid;
    }

    @Nullable
    public Integer getEarnedPointsError() {
        return earnedPointsError;
    }

    @Nullable
    public Integer getBonusPointsError() {
        return bonusPointsError;
    }

    @Nullable
    public Integer getExamEarnedPointsError() {
        return examEarnedPointsError;
    }

    @Nullable
    public Integer getMarkError() {
        return markError;
    }

    public boolean isDataValid() {
        return isDataValid;
    }
}