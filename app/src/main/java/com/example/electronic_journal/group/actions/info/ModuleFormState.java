package com.example.electronic_journal.group.actions.info;

import androidx.annotation.Nullable;

import com.example.electronic_journal.R;

public class ModuleFormState {
    private final boolean isDataValid;
    @Nullable
    private final Integer minPointsError;
/*    @Nullable
    private Integer maxPointsError;*/

    public ModuleFormState(String minPoints) {
        boolean isMinPointsValid = !minPoints.trim().isEmpty();
        /* boolean isMaxPointsValid = !maxPoints.trim().isEmpty();*/

        this.minPointsError = !isMinPointsValid ? R.string.invalid_empty_field : null;
        /*        this.maxPointsError = !isMaxPointsValid ? R.string.invalid_empty_field : null;*/

/*
        boolean isPointsRangeValid = true;
        if (isMinPointsValid && isMaxPointsValid && Integer.parseInt(minPoints) > Integer.parseInt(maxPoints)) {
            this.minPointsError = R.string.invalid_points_range;
            this.maxPointsError = R.string.invalid_points_range;
            isPointsRangeValid = false;
        }
*/

        this.isDataValid = isMinPointsValid;
    }

    @Nullable
    public Integer getMinPointsError() {
        return minPointsError;
    }

/*    @Nullable
    public Integer getMaxPointsError() {
        return maxPointsError;
    }*/

    public boolean isDataValid() {
        return isDataValid;
    }
}