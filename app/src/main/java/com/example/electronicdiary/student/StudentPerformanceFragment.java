package com.example.electronicdiary.student;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.electronicdiary.R;
import com.example.electronicdiary.data_classes.Event;
import com.example.electronicdiary.data_classes.Lesson;
import com.example.electronicdiary.data_classes.Module;
import com.example.electronicdiary.data_classes.StudentPerformanceInModule;
import com.example.electronicdiary.data_classes.StudentPerformanceInSubject;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;
import java.util.Map;

public class StudentPerformanceFragment extends Fragment {
    private StudentPerformanceViewModel studentPerformanceViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_student_performance, container, false);

        int page = getArguments().getInt("openPage");
        int moduleExpand = getArguments().getInt("moduleExpand", -1);
        long studentPerformanceInSubjectId = getArguments().getLong("studentPerformanceInSubjectId");

        studentPerformanceViewModel = new ViewModelProvider(this).get(StudentPerformanceViewModel.class);
        studentPerformanceViewModel.setModuleExpand(moduleExpand);
        studentPerformanceViewModel.setOpenPage(page);
        studentPerformanceViewModel.downloadStudentPerformanceInSubject(studentPerformanceInSubjectId);

        LiveData<StudentPerformanceInSubject> studentPerformanceInSubjectLiveData = studentPerformanceViewModel.getStudentPerformanceInSubject();
        LiveData<Map<String, Module>> modulesLiveData = Transformations.switchMap(studentPerformanceInSubjectLiveData,
                s -> {
                    studentPerformanceViewModel.downloadEventsAndLessons(s.getSubjectInfo().getId());
                    return studentPerformanceViewModel.getModules();
                });
        LiveData<Map<String, StudentPerformanceInModule>> studentPerformanceInModulesLiveData = Transformations.switchMap(modulesLiveData,
                s -> studentPerformanceViewModel.getStudentPerformanceInModules());

        LiveData<Map<String, List<Event>>> eventsLiveData = Transformations.switchMap(studentPerformanceInModulesLiveData,
                s -> studentPerformanceViewModel.getEvents());
        LiveData<Map<String, List<Lesson>>> lecturesLiveData = Transformations.switchMap(studentPerformanceInModulesLiveData,
                s -> studentPerformanceViewModel.getLectures());
        LiveData<Map<String, List<Lesson>>> seminarsLiveData = Transformations.switchMap(studentPerformanceInModulesLiveData,
                s -> studentPerformanceViewModel.getSeminars());

        eventsLiveData.observe(getViewLifecycleOwner(), events -> {
            if (events != null && studentPerformanceViewModel.getLectures().getValue() == null &&
                    studentPerformanceViewModel.getSeminars().getValue() == null) {

                openPages(root, studentPerformanceInSubjectId, page);
            }
        });

        lecturesLiveData.observe(getViewLifecycleOwner(), lectures -> {
            if (lectures != null && studentPerformanceViewModel.getEvents().getValue() == null &&
                    studentPerformanceViewModel.getSeminars().getValue() == null) {

                openPages(root, studentPerformanceInSubjectId, page);
            }
        });

        seminarsLiveData.observe(getViewLifecycleOwner(), seminars -> {
            if (seminars != null && studentPerformanceViewModel.getLectures().getValue() == null &&
                    studentPerformanceViewModel.getEvents().getValue() == null) {

                openPages(root, studentPerformanceInSubjectId, page);
            }
        });

        return root;
    }

    private void openPages(View root, Long studentPerformanceInSubjectId, int page) {
        studentPerformanceViewModel.downloadStudentEvents(studentPerformanceInSubjectId);
        studentPerformanceViewModel.getStudentEvents().observe(getViewLifecycleOwner(), studentEvents -> {
            studentPerformanceViewModel.downloadStudentLessons(studentPerformanceInSubjectId);
            studentPerformanceViewModel.getStudentLessons().observe(getViewLifecycleOwner(), studentLessons -> {
                EventsOrLessonsPagerAdapter eventsOrLessonsPagerAdapter = new EventsOrLessonsPagerAdapter(StudentPerformanceFragment.this);
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
            });
        });
    }
}
