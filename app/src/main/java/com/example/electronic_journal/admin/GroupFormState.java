package com.example.electronic_journal.admin;

import androidx.annotation.Nullable;

import com.example.electronic_journal.R;

public class GroupFormState {
    @Nullable
    private final Integer groupTitleError;
    private final boolean isDataValid;

    public GroupFormState(String groupTitle) {
        boolean isGroupTitleValid = groupTitle.trim().matches("[А-Я]{1,4}([1-9]|1[0-5])-([1-8][1-9]Б|[1-4][1-9]М)");

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