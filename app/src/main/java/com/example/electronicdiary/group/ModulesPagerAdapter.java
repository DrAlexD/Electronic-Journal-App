package com.example.electronicdiary.group;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


public class ModulesPagerAdapter extends FragmentPagerAdapter {
    private final GroupPerformanceViewModel groupPerformanceViewModel;

    public ModulesPagerAdapter(FragmentManager fm, GroupPerformanceViewModel groupPerformanceViewModel) {
        super(fm);
        this.groupPerformanceViewModel = groupPerformanceViewModel;
    }

    @Override
    public Fragment getItem(int position) {
        return ModuleFragment.newInstance(position + 1, groupPerformanceViewModel);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return (ModuleFragment.getTitle(position + 1));
    }

    @Override
    public int getCount() {
        return 3;
    }
}