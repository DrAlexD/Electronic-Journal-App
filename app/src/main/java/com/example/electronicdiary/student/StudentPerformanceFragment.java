package com.example.electronicdiary.student;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.electronicdiary.Event;
import com.example.electronicdiary.Lesson;
import com.example.electronicdiary.R;
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

    private HashMap<String, ArrayList<Lesson>> lessonsByModules;
    private ArrayList<String> modules;
    private ArrayList<ArrayList<Lesson>> lessons;

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
        studentPerformanceViewModel.setLessonsByModules(lessonsByModules);
        studentPerformanceViewModel.setModules(modules);
        studentPerformanceViewModel.setLessons(lessons);

        EventsOrLessonsPagerAdapter eventsOrLessonsPagerAdapter = new EventsOrLessonsPagerAdapter(this);
        ViewPager2 viewPager = root.findViewById(R.id.events_or_lessons_pager);
        viewPager.setAdapter(eventsOrLessonsPagerAdapter);
        viewPager.setCurrentItem(0, false);
        viewPager.setOffscreenPageLimit(2);

        TabLayout tabLayout = root.findViewById(R.id.events_or_lessons_tab_layout);
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
        lessonsByModules = new HashMap<>();

        modules = new ArrayList<>();
        modules.add("Модуль 1");
        modules.add("Модуль 2");
        modules.add("Модуль 3");

        lessons = new ArrayList<>();
        ArrayList<Lesson> lessons1 = new ArrayList<>();
        lessons1.add(new Lesson(new Date(2020, 0, 1), 1));
        lessons1.add(new Lesson(new Date(2020, 1, 2), 2));
        lessons1.add(new Lesson(new Date(2020, 2, 3), 3));
        ArrayList<Lesson> lessons2 = new ArrayList<>();
        lessons2.add(new Lesson(new Date(2020, 3, 4), 4));
        lessons2.add(new Lesson(new Date(2020, 4, 5), 5));
        lessons2.add(new Lesson(new Date(2020, 5, 6), 6));
        ArrayList<Lesson> lessons3 = new ArrayList<>();
        lessons3.add(new Lesson(new Date(2020, 6, 7), 7));
        lessons3.add(new Lesson(new Date(2020, 7, 8), 8));
        lessons3.add(new Lesson(new Date(2020, 8, 9), 9));
        lessons.add(lessons1);
        lessons.add(lessons2);
        lessons.add(lessons3);

        for (int i = 0; i < modules.size(); i++) {
            lessonsByModules.put(modules.get(i), lessons.get(i));
        }
    }
}
