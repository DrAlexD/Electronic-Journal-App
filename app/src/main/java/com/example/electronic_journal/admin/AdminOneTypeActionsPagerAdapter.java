package com.example.electronic_journal.admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

class AdminOneTypeActionsPagerAdapter extends FragmentStateAdapter {

    public AdminOneTypeActionsPagerAdapter(Fragment adminActions) {
        super(adminActions);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment adminOneTypeActionsFragment = new AdminOneTypeActionsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position + 1);
        adminOneTypeActionsFragment.setArguments(bundle);
        return adminOneTypeActionsFragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}