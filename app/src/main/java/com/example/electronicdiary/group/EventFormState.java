package com.example.electronicdiary.group;

import androidx.annotation.Nullable;

import com.example.electronicdiary.R;

public class EventFormState {
    @Nullable
    private final Integer eventMinPointsError;
    private final boolean isDataValid;

    public EventFormState(String eventMinPoints) {
        boolean isEventMinPointsValid = !eventMinPoints.trim().isEmpty();

        this.eventMinPointsError = !isEventMinPointsValid ? R.string.invalid_empty_field : null;
        this.isDataValid = isEventMinPointsValid;
    }

    @Nullable
    public Integer getEventMinPointsError() {
        return eventMinPointsError;
    }

    public boolean isDataValid() {
        return isDataValid;
    }
}