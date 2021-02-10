package com.example.electronicdiary.group;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class ModuleViewModel extends ViewModel {
    private final MutableLiveData<Integer> position = new MutableLiveData<>();
    private GroupPerformanceViewModel groupPerformanceViewModel;
    private final LiveData<String> mText = Transformations.map(position, new Function<Integer, String>() {
        @Override
        public String apply(Integer input) {
            return "Предмет: " + groupPerformanceViewModel.getSubject().getValue() +
                    ", группа: " + groupPerformanceViewModel.getGroup().getValue() +
                    ", модуль: " + position.getValue();
        }
    });

    public void setGroupPerformanceViewModel(GroupPerformanceViewModel groupPerformanceViewModel) {
        this.groupPerformanceViewModel = groupPerformanceViewModel;
    }

    public void setPosition(int position) {
        this.position.setValue(position);
    }

    public LiveData<String> getText() {
        return mText;
    }
}