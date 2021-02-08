package com.example.electronicdiary.group;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class GroupPerformanceViewModelFactory implements ViewModelProvider.Factory {
    private final String group;
    private final String subject;

    public GroupPerformanceViewModelFactory(String group, String subject) {
        super();
        this.group = group;
        this.subject = subject;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(GroupPerformanceViewModel.class)) {
            return (T) new GroupPerformanceViewModel(group, subject);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}