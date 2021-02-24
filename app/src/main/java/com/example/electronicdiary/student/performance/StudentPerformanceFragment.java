package com.example.electronicdiary.student.performance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.electronicdiary.R;
import com.example.electronicdiary.student.Event;
import com.example.electronicdiary.student.Visit;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class StudentPerformanceFragment extends Fragment {
    private String subject;
    private String studentName;
    private String group;

    private ArrayList<Event> events;

    private HashMap<String, ArrayList<Visit>> visitsByModules;
    private ArrayList<String> modules;
    private ArrayList<ArrayList<Visit>> visits;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_student_performance, container, false);

        subject = getArguments().getString("subject");
        studentName = getArguments().getString("student");
        group = getArguments().getString("group");

        downloadData();

        StudentPerformanceViewModel studentPerformanceViewModel = new ViewModelProvider(this).get(StudentPerformanceViewModel.class);
        studentPerformanceViewModel.setGroup(group);
        studentPerformanceViewModel.setSubject(subject);
        studentPerformanceViewModel.setStudentName(studentName);

        studentPerformanceViewModel.setEvents(events);
        studentPerformanceViewModel.setVisitsByModules(visitsByModules);
        studentPerformanceViewModel.setModules(modules);
        studentPerformanceViewModel.setVisits(visits);

        EventsOrVisitsPagerAdapter eventsOrVisitsPagerAdapter = new EventsOrVisitsPagerAdapter(this);
        ViewPager2 viewPager = root.findViewById(R.id.events_or_visits_pager);
        viewPager.setAdapter(eventsOrVisitsPagerAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(2);

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

    private void downloadData() {
        //TODO загрузка мероприятий студента
        events = new ArrayList<>();
        events.add(new Event("РК1", 5));
        events.add(new Event("ДЗ1", 10));
        events.add(new Event("РК2", 15));
        events.add(new Event("ДЗ2", 20));
        events.add(new Event("РК3", 25));
        events.add(new Event("ДЗ3", 30));

        //TODO загрузка посещений студента
        visitsByModules = new HashMap<>();

        modules = new ArrayList<>();
        modules.add("Модуль 1");
        modules.add("Модуль 2");
        modules.add("Модуль 3");

        visits = new ArrayList<>();
        ArrayList<Visit> visits1 = new ArrayList<>();
        visits1.add(new Visit(new Date(2020, 0, 1), 1));
        visits1.add(new Visit(new Date(2020, 1, 2), 2));
        visits1.add(new Visit(new Date(2020, 2, 3), 3));
        ArrayList<Visit> visits2 = new ArrayList<>();
        visits2.add(new Visit(new Date(2020, 3, 4), 4));
        visits2.add(new Visit(new Date(2020, 4, 5), 5));
        visits2.add(new Visit(new Date(2020, 5, 6), 6));
        ArrayList<Visit> visits3 = new ArrayList<>();
        visits3.add(new Visit(new Date(2020, 6, 7), 7));
        visits3.add(new Visit(new Date(2020, 7, 8), 8));
        visits3.add(new Visit(new Date(2020, 8, 9), 9));
        visits.add(visits1);
        visits.add(visits2);
        visits.add(visits3);

        for (int i = 0; i < modules.size(); i++) {
            visitsByModules.put(modules.get(i), visits.get(i));
        }
    }
}
