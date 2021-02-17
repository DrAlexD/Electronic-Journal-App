package com.example.electronicdiary.student;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.electronicdiary.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class StudentPerformanceFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_student_performance, container, false);

        String subjectTitle = getArguments().getString("subject");
        String studentName = getArguments().getString("student");
        String studentGroup = getArguments().getString("group");

        EventsOrVisitsPagerAdapter eventsOrVisitsPagerAdapter = new EventsOrVisitsPagerAdapter(this);
        ViewPager2 viewPager = root.findViewById(R.id.events_or_visits_pager);
        viewPager.setAdapter(eventsOrVisitsPagerAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setUserInputEnabled(false);

        TabLayout tabLayout = root.findViewById(R.id.events_or_visits_tab_layout);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0)
                tab.setText("Мероприятия");
            else if (position == 1)
                tab.setText("Посещаемость (лекция)");
            else if (position == 2)
                tab.setText("Посещаемость (семинар)");
        }).attach();

        return root;
    }
}
