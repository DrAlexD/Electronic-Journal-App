package com.example.electronicdiary.student;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

class EventsOrLessonsPagerAdapter extends FragmentStateAdapter {

    public EventsOrLessonsPagerAdapter(Fragment studentPerformanceFragment) {
        super(studentPerformanceFragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment eventsOrLessonsFragment = new EventsOrLessonsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position + 1);
        eventsOrLessonsFragment.setArguments(bundle);
        return eventsOrLessonsFragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}