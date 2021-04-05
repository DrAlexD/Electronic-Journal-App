package com.example.electronicdiary.group;

import androidx.annotation.Nullable;

import com.example.electronicdiary.R;

public class StudentEventFormState {
    @Nullable
    private final Integer variantNumberError;
    private final boolean isDataValid;

    @Nullable
    private Integer finishDateError;
    @Nullable
    private Integer earnedPointsError;
    @Nullable
    private Integer bonusPointsError;

    public StudentEventFormState(String variantNumber) {
        boolean isVariantNumberValid = !variantNumber.trim().isEmpty();

        this.variantNumberError = !isVariantNumberValid ? R.string.invalid_empty_field : null;
        this.isDataValid = isVariantNumberValid;
    }

    public StudentEventFormState(String variantNumber, String finishDate, String earnedPoints, String bonusPoints) {
        boolean isVariantNumberValid = !variantNumber.trim().isEmpty();
        boolean isFinishDateValid = finishDate.trim().matches("[0-9][0-9]\\.[0-1][0-9]\\.2[0-1][0-9][0-9]");
        boolean isEarnedPointsValid = !earnedPoints.trim().isEmpty();
        boolean isBonusPointsValid = !bonusPoints.trim().isEmpty();

        this.variantNumberError = !isVariantNumberValid ? R.string.invalid_empty_field : null;
        this.finishDateError = !isFinishDateValid ? R.string.invalid_date_field : null;
        this.earnedPointsError = !isEarnedPointsValid ? R.string.invalid_empty_field : null;
        this.bonusPointsError = !isBonusPointsValid ? R.string.invalid_empty_field : null;
        this.isDataValid = isVariantNumberValid && isFinishDateValid && isEarnedPointsValid && isBonusPointsValid;
    }

    @Nullable
    public Integer getVariantNumberError() {
        return variantNumberError;
    }

    @Nullable
    public Integer getFinishDateError() {
        return finishDateError;
    }

    @Nullable
    public Integer getEarnedPointsError() {
        return earnedPointsError;
    }

    @Nullable
    public Integer getBonusPointsError() {
        return bonusPointsError;
    }

    public boolean isDataValid() {
        return isDataValid;
    }
}