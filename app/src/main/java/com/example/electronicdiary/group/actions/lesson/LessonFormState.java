package com.example.electronicdiary.group.actions.lesson;

import androidx.annotation.Nullable;

import com.example.electronicdiary.R;

public class LessonFormState {
    @Nullable
    private final Integer dateAndTimeError;
    @Nullable
    private final Integer pointsPerVisitError;
    private final boolean isDataValid;

    public LessonFormState(String dateAndTime, String pointsPerVisit) {
        boolean isDateAndTimeValid = dateAndTime.matches("[0-3][0-9]\\.[0-1][0-9]\\.20[2-9][0-9] [0-2][0-9]:[0-5][0-9]");
        boolean isPointsPerVisitValid = !pointsPerVisit.trim().isEmpty();

        this.dateAndTimeError = !isDateAndTimeValid ? R.string.invalid_date_time_field : null;
        this.pointsPerVisitError = !isPointsPerVisitValid ? R.string.invalid_empty_field : null;
        this.isDataValid = isDateAndTimeValid && isPointsPerVisitValid;
    }

    @Nullable
    public Integer getDateAndTimeError() {
        return dateAndTimeError;
    }

    @Nullable
    public Integer getPointsPerVisitError() {
        return pointsPerVisitError;
    }

    public boolean isDataValid() {
        return isDataValid;
    }
}