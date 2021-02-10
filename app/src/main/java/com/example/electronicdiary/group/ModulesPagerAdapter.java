package com.example.electronicdiary.group;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;


public class ModulesPagerAdapter extends FragmentStateAdapter {
    private final GroupPerformanceViewModel groupPerformanceViewModel;

    public ModulesPagerAdapter(Fragment groupPerformanceFragment, GroupPerformanceViewModel groupPerformanceViewModel) {
        super(groupPerformanceFragment);
        this.groupPerformanceViewModel = groupPerformanceViewModel;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return new ModuleFragment(position + 1, groupPerformanceViewModel);
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}