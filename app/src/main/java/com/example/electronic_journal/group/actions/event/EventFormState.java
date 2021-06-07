package com.example.electronic_journal.group.actions.event;

import androidx.annotation.Nullable;

import com.example.electronic_journal.R;
import com.example.electronic_journal.data_classes.Semester;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EventFormState {
    private final boolean isDataValid;
    @Nullable
    private Integer startDateError;
    @Nullable
    private Integer deadlineDateError;
    @Nullable
    private Integer minPointsError;
    @Nullable
    private Integer maxPointsError;

    public EventFormState(String startDate, String deadlineDate, String minPoints, String maxPoints, Semester semester) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        boolean isStartDateValid = startDate.trim().matches("(0[1-9]|[1-2][0-9]|3[0-1])\\.(0[1-9]|1[0-2])\\.20[2-5][0-9]");
        boolean isDeadlineDateValid = deadlineDate.trim().matches("(0[1-9]|[1-2][0-9]|3[0-1])\\.(0[1-9]|1[0-2])\\.20[2-5][0-9]");
        boolean isMinPointsValid = !minPoints.trim().isEmpty();
        boolean isMaxPointsValid = !maxPoints.trim().isEmpty();

        this.startDateError = !isStartDateValid ? R.string.invalid_date_field : null;
        this.deadlineDateError = !isDeadlineDateValid ? R.string.invalid_date_field : null;
        this.minPointsError = !isMinPointsValid ? R.string.invalid_empty_field : null;
        this.maxPointsError = !isMaxPointsValid ? R.string.invalid_empty_field : null;

        boolean isPointsRangeValid = true;
        if (isMinPointsValid && isMaxPointsValid && Integer.parseInt(minPoints) > Integer.parseInt(maxPoints)) {
            this.minPointsError = R.string.invalid_points_range;
            this.maxPointsError = R.string.invalid_points_range;
            isPointsRangeValid = false;
        }

        boolean isDateRangeValid = true;
        try {
            Date startDateR = sdf.parse(startDate);
            Date deadlineDateR = sdf.parse(deadlineDate);

            if (semester != null) {
                if (isStartDateValid && isDeadlineDateValid) {
                    String semesterStartString = (semester.isFirstHalf() ? "31.08." : "01.02.") + semester.getYear();
                    String semesterEndString = (semester.isFirstHalf() ? "31.12." : "01.07.") + semester.getYear();

                    Date semesterStartDate = sdf.parse(semesterStartString);
                    Date semesterEndDate = sdf.parse(semesterEndString);

                    if (startDateR.before(semesterStartDate) || startDateR.after(semesterEndDate)) {
                        this.startDateError = R.string.invalid_date_in_semester_range;
                        isStartDateValid = false;
                    }

                    if (deadlineDateR.before(semesterStartDate) || deadlineDateR.after(semesterEndDate)) {
                        this.deadlineDateError = R.string.invalid_date_in_semester_range;
                        isDeadlineDateValid = false;
                    }
                }
            }

            if (isStartDateValid && isDeadlineDateValid) {
                if (startDateR.after(deadlineDateR)) {
                    this.startDateError = R.string.invalid_date_range;
                    this.deadlineDateError = R.string.invalid_date_range;
                    isDateRangeValid = false;
                }
            }
        } catch (ParseException ignored) {
        }

        this.isDataValid = isStartDateValid && isDeadlineDateValid && isMinPointsValid &&
                isMaxPointsValid && isPointsRangeValid && isDateRangeValid;
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