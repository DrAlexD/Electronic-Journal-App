package com.example.electronicdiary.group;

import androidx.annotation.Nullable;

import com.example.electronicdiary.R;

public class LessonFormState {
    @Nullable
    private final Integer lessonAttendPointsError;
    private final boolean isDataValid;
    @Nullable
    private Integer lessonDateError;

    public LessonFormState(String lessonDate, String lessonAttendPoints) {
        boolean isLessonDateValid = !lessonDate.trim().isEmpty();
        boolean isLessonAttendPointsValid = !lessonAttendPoints.trim().isEmpty();

        this.lessonDateError = !isLessonDateValid ? R.string.invalid_empty_field : null;
        this.lessonAttendPointsError = !isLessonAttendPointsValid ? R.string.invalid_empty_field : null;
        this.isDataValid = isLessonDateValid && isLessonAttendPointsValid;
    }

    public LessonFormState(String lessonAttendPoints) {
        boolean isLessonAttendPointsValid = !lessonAttendPoints.trim().isEmpty();

        this.lessonAttendPointsError = !isLessonAttendPointsValid ? R.string.invalid_empty_field : null;
        this.isDataValid = isLessonAttendPointsValid;
    }

    @Nullable
    public Integer getLessonDateError() {
        return lessonDateError;
    }

    @Nullable
    public Integer getLessonAttendPointsError() {
        return lessonAttendPointsError;
    }

    public boolean isDataValid() {
        return isDataValid;
    }
}