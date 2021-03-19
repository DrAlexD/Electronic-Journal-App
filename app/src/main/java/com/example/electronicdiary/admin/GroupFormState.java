package com.example.electronicdiary.admin;

import androidx.annotation.Nullable;

import com.example.electronicdiary.R;

public class GroupFormState {
    @Nullable
    private final Integer groupTitleError;
    private final boolean isDataValid;

    public GroupFormState(String groupTitle) {
        boolean isGroupTitleValid = groupTitle.trim().length() > 4;

        this.groupTitleError = !isGroupTitleValid ? R.string.invalid_group_title : null;
        this.isDataValid = isGroupTitleValid;
    }

    @Nullable
    public Integer getGroupTitleError() {
        return groupTitleError;
    }

    public boolean isDataValid() {
        return isDataValid;
    }
}