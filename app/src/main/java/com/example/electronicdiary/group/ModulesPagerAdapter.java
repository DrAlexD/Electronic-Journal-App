package com.example.electronicdiary.group;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;


public class ModulesPagerAdapter extends FragmentStateAdapter {

    public ModulesPagerAdapter(Fragment groupPerformanceFragment) {
        super(groupPerformanceFragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment moduleFragment = new ModuleFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position + 1);
        moduleFragment.setArguments(bundle);
        return moduleFragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}