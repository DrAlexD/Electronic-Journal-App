package com.example.electronicdiary.group;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.electronicdiary.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class GroupPerformanceFragment extends Fragment {
    private GroupPerformanceViewModel groupPerformanceViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_group_performance, container, false);
        groupPerformanceViewModel = new ViewModelProvider(this).get(GroupPerformanceViewModel.class);
        groupPerformanceViewModel.setGroupAndSubject(getArguments().getString("group"), getArguments().getString("subject"));

        ModulesPagerAdapter sectionsPagerAdapter = new ModulesPagerAdapter(this, groupPerformanceViewModel);
        ViewPager2 viewPager = root.findViewById(R.id.modules_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(2);

        TabLayout tabLayout = root.findViewById(R.id.modules_tab_layout);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) ->
                tab.setText("Модуль " + (position + 1))).attach();

        return root;
    }
}