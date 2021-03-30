package com.example.electronicdiary.group;

import androidx.annotation.Nullable;

import com.example.electronicdiary.R;

public class EventFormState {
    @Nullable
    private final Integer startDateError;
    @Nullable
    private final Integer deadlineDateError;
    @Nullable
    private final Integer minPointsError;
    @Nullable
    private final Integer maxPointsError;
    private final boolean isDataValid;

    public EventFormState(String startDate, String deadlineDate, String minPoints, String maxPoints) {
        boolean isStartDateValid = startDate.trim().matches("[0-9][0-9]\\.[0-1][0-9]\\.2[0-1][0-9][0-9]");
        boolean isDeadlineDateValid = deadlineDate.trim().matches("[0-9][0-9]\\.[0-1][0-9]\\.2[0-1][0-9][0-9]");
        boolean isMinPointsValid = !minPoints.trim().isEmpty();
        boolean isMaxPointsValid = !maxPoints.trim().isEmpty();

        this.startDateError = !isStartDateValid ? R.string.invalid_date_field : null;
        this.deadlineDateError = !isDeadlineDateValid ? R.string.invalid_date_field : null;
        this.minPointsError = !isMinPointsValid ? R.string.invalid_empty_field : null;
        this.maxPointsError = !isMaxPointsValid ? R.string.invalid_empty_field : null;
        this.isDataValid = isStartDateValid && isDeadlineDateValid && isMinPointsValid && isMaxPointsValid;
    }

    @Nullable
    public Integer getStartDateError() {
        return startDateError;
    }

    @Nullable
    public Integer getDeadlineDateError() {
        return deadlineDateError;
    }

    @Nullable
    public Integer getMinPointsError() {
        return minPointsError;
    }

    @Nullable
    public Integer getMaxPointsError() {
        return maxPointsError;
    }

    public boolean isDataValid() {
        return isDataValid;
    }
}