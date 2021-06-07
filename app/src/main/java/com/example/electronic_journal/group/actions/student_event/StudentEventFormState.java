package com.example.electronic_journal.group.actions.student_event;

import androidx.annotation.Nullable;

import com.example.electronic_journal.R;
import com.example.electronic_journal.data_classes.Semester;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StudentEventFormState {
    private final boolean isDataValid;
    @Nullable
    private final Integer earnedPointsError;
    @Nullable
    private Integer finishDateError;

    public StudentEventFormState(String earnedPoints, Integer eventMaxPoints, String finishDate, Semester semester, Date startDate) {
        boolean isEarnedPointsValid = true;
        if (!earnedPoints.isEmpty()) {
            isEarnedPointsValid = Integer.parseInt(earnedPoints) <= eventMaxPoints;
        }

        this.earnedPointsError = !isEarnedPointsValid ? R.string.invalid_earned_points_range : null;

        boolean isFinishDateValid = true;
        if (!finishDate.isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            try {
                Date finishDateR = sdf.parse(finishDate);

                if (semester != null) {
                    String semesterStartString = (semester.isFirstHalf() ? "31.08." : "01.02.") + semester.getYear();
                    String semesterEndString = (semester.isFirstHalf() ? "31.12." : "01.07.") + semester.getYear();

                    Date semesterStartDate = sdf.parse(semesterStartString);
                    Date semesterEndDate = sdf.parse(semesterEndString);

                    if (finishDateR.before(semesterStartDate) || finishDateR.after(semesterEndDate)) {
                        isFinishDateValid = false;
                        this.finishDateError = R.string.invalid_date_in_semester_range;
                    }

                    if (isFinishDateValid) {
                        if (finishDateR.before(startDate)) {
                            isFinishDateValid = false;
                            this.finishDateError = R.string.invalid_finish_date_range;
                        }
                    }
                }
            } catch (ParseException ignored) {
            }
        }

        this.isDataValid = isEarnedPointsValid && isFinishDateValid;
    }

    @Nullable
    public Integer getEarnedPointsError() {
        return earnedPointsError;
    }

    @Nullable
    public Integer getFinishDateError() {
        return finishDateError;
    }

    public boolean isDataValid() {
        return isDataValid;
    }
}