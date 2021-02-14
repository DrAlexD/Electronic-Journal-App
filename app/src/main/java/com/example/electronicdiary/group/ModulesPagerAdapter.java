package com.example.electronicdiary.group;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;


class ModulesPagerAdapter extends FragmentStateAdapter {

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

    @Override
    public long getItemId(int position) {
        return PagerAdapter.POSITION_NONE;
    }
}