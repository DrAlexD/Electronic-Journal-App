package com.example.electronic_journal.group.actions.student_event;

import androidx.annotation.Nullable;

import com.example.electronic_journal.R;
import com.example.electronic_journal.data_classes.Semester;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StudentEventFormState {
    @Nullable
    private final Integer variantNumberError;
    private final boolean isDataValid;
    @Nullable
    private Integer earnedPointsError;
    @Nullable
    private Integer finishDateError;

    public StudentEventFormState(String variantNumber) {
        boolean isVariantNumberValid = !variantNumber.trim().isEmpty();

        this.variantNumberError = !isVariantNumberValid ? R.string.invalid_empty_field : null;
        this.isDataValid = isVariantNumberValid;
    }

    public StudentEventFormState(String variantNumber, String earnedPoints, Integer eventMaxPoints, String finishDate, Semester semester, Date startDate) {
        boolean isVariantNumberValid = !variantNumber.trim().isEmpty();

        this.variantNumberError = !isVariantNumberValid ? R.string.invalid_empty_field : null;

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
                    String semesterStartString = (semester.isFirstHalf() ? "01.02." : "31.08.") + semester.getYear();
                    String semesterEndString = (semester.isFirstHalf() ? "01.06." : "31.12.") + semester.getYear();

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

        this.isDataValid = isVariantNumberValid && isEarnedPointsValid && isFinishDateValid;
    }

    @Nullable
    public Integer getVariantNumberError() {
        return variantNumberError;
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