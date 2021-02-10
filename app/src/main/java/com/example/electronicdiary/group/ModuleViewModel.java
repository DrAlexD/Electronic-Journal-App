package com.example.electronicdiary.group;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class ModuleViewModel extends ViewModel {
    private final MutableLiveData<Integer> mIndex = new MutableLiveData<>();
    private GroupPerformanceViewModel groupPerformanceViewModel;
    private final LiveData<String> mText = Transformations.map(mIndex, new Function<Integer, String>() {
        @Override
        public String apply(Integer input) {
            return "Предмет: " + groupPerformanceViewModel.getSubject().getValue() +
                    ", группа: " + groupPerformanceViewModel.getGroup().getValue() +
                    ", модуль: " + mIndex.getValue();
        }
    });

    public void setGroupPerformanceViewModel(GroupPerformanceViewModel groupPerformanceViewModel) {
        this.groupPerformanceViewModel = groupPerformanceViewModel;
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void setIndex(int index) {
        mIndex.setValue(index);
    }
}