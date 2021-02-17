package com.example.electronicdiary.student;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

class EventsOrVisitsPagerAdapter extends FragmentStateAdapter {

    public EventsOrVisitsPagerAdapter(Fragment studentPerformanceFragment) {
        super(studentPerformanceFragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment eventsOrVisitsFragment = new EventsOrVisitsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position + 1);
        eventsOrVisitsFragment.setArguments(bundle);
        return eventsOrVisitsFragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}