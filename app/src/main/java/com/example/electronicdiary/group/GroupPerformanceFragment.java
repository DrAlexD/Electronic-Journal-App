package com.example.electronicdiary.group;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.electronicdiary.R;

public class GroupPerformanceFragment extends Fragment {
    private GroupPerformanceViewModel groupPerformanceViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_group_performance, container, false);
        groupPerformanceViewModel = new ViewModelProvider(this).get(GroupPerformanceViewModel.class);
        groupPerformanceViewModel.setGroupAndSubject(getArguments().getString("group"), getArguments().getString("subject"));

        ModulesPagerAdapter sectionsPagerAdapter = new ModulesPagerAdapter(getActivity().getSupportFragmentManager(), groupPerformanceViewModel);
        ViewPager viewPager = root.findViewById(R.id.modules_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(2);

        return root;
    }
}