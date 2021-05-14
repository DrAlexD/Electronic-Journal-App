package com.example.electronicdiary.group.actions.info;

import androidx.annotation.Nullable;

import com.example.electronicdiary.R;

public class ModuleFormState {
    @Nullable
    private final Integer minPointsError;
    @Nullable
    private final Integer maxPointsError;
    private final boolean isDataValid;

    public ModuleFormState(String minPoints, String maxPoints) {
        boolean isMinPointsValid = !minPoints.trim().isEmpty();
        boolean isMaxPointsValid = !maxPoints.trim().isEmpty();

        this.minPointsError = !isMinPointsValid ? R.string.invalid_empty_field : null;
        this.maxPointsError = !isMaxPointsValid ? R.string.invalid_empty_field : null;
        this.isDataValid = isMinPointsValid && isMaxPointsValid;
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