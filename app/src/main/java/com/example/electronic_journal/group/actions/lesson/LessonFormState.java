package com.example.electronic_journal.group.actions.lesson;

import androidx.annotation.Nullable;

import com.example.electronic_journal.R;
import com.example.electronic_journal.data_classes.Semester;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LessonFormState {
    @Nullable
    private final Integer pointsPerVisitError;
    private final boolean isDataValid;
    @Nullable
    private Integer dateAndTimeError;

    public LessonFormState(String dateAndTime, String pointsPerVisit, Semester semester) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        boolean isDateAndTimeValid = dateAndTime.matches("(0[1-9]|[1-2][0-9]|3[0-1])\\.(0[1-9]|1[0-2])\\.20[2-5][0-9] (0[1-9]|1[0-9]|2[0-3]):[0-5][0-9]");
        boolean isPointsPerVisitValid = !pointsPerVisit.trim().isEmpty();

        this.dateAndTimeError = !isDateAndTimeValid ? R.string.invalid_date_time_field : null;
        this.pointsPerVisitError = !isPointsPerVisitValid ? R.string.invalid_empty_field : null;

        try {
            Date lessonDateR = sdf.parse(dateAndTime);

            if (semester != null) {
                if (isDateAndTimeValid) {
                    SimpleDateFormat sdf2 = new SimpleDateFormat("dd.MM.yyyy");

                    String semesterStartString = (semester.isFirstHalf() ? "31.08." : "01.02.") + semester.getYear();
                    String semesterEndString = (semester.isFirstHalf() ? "31.12." : "01.07.") + semester.getYear();

                    Date semesterStartDate = sdf2.parse(semesterStartString);
                    Date semesterEndDate = sdf2.parse(semesterEndString);

                    if (lessonDateR.before(semesterStartDate) || lessonDateR.after(semesterEndDate)) {
                        this.dateAndTimeError = R.string.invalid_date_in_semester_range;
                        isDateAndTimeValid = false;
                    }
                }
            }
        } catch (ParseException ignored) {
        }

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