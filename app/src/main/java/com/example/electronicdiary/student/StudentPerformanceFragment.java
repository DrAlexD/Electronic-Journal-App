package com.example.electronicdiary.student;

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

public class StudentPerformanceFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_student_performance, container, false);

        int page = getArguments().getInt("openPage");
        int studentId = getArguments().getInt("studentId");
        int subjectId = getArguments().getInt("subjectId");
        int semesterId = getArguments().getInt("semesterId");

        StudentPerformanceViewModel studentPerformanceViewModel = new ViewModelProvider(this).get(StudentPerformanceViewModel.class);
        studentPerformanceViewModel.downloadStudentById(studentId);

        studentPerformanceViewModel.getStudent().observe(getViewLifecycleOwner(), student -> {
            if (student == null) {
                return;
            }

            studentPerformanceViewModel.downloadSubjectInfo(student.getGroupId(), subjectId, semesterId);
            studentPerformanceViewModel.getSubjectInfo().observe(getViewLifecycleOwner(), subjectInfo -> {
                if (subjectInfo == null) {
                    return;
                }

                studentPerformanceViewModel.downloadEventsAndLessons(studentId, student.getGroupId(), subjectId,
                        subjectInfo.getLecturerId(), subjectInfo.getSeminarianId(), semesterId);
            });
        });

        EventsOrLessonsPagerAdapter eventsOrLessonsPagerAdapter = new EventsOrLessonsPagerAdapter(this);
        ViewPager2 viewPager = root.findViewById(R.id.events_or_lessons_pager);
        viewPager.setAdapter(eventsOrLessonsPagerAdapter);
        viewPager.setCurrentItem(page, false);
        viewPager.setOffscreenPageLimit(2);

        TabLayout tabLayout = root.findViewById(R.id.events_or_lessons_tab_layout);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0)
                tab.setText("Мероприятия");
            else if (position == 1)
                tab.setText("Лекции");
            else if (position == 2)
                tab.setText("Семинары");
        }).attach();

        return root;
    }
}
