package com.example.electronic_journal.group;

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

import com.example.electronic_journal.R;
import com.example.electronic_journal.data_classes.Lesson;
import com.example.electronic_journal.data_classes.Module;
import com.example.electronic_journal.data_classes.Student;
import com.example.electronic_journal.data_classes.StudentLesson;
import com.example.electronic_journal.data_classes.StudentPerformanceInModule;
import com.example.electronic_journal.data_classes.SubjectInfo;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;
import java.util.Map;

public class GroupPerformanceLessonsFragment extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_group_performance_lessons, container, false);

        long subjectInfoId = getArguments().getLong("subjectInfoId");

        GroupPerformanceLessonsViewModel groupPerformanceLessonsViewModel = new ViewModelProvider(this).get(GroupPerformanceLessonsViewModel.class);
        groupPerformanceLessonsViewModel.downloadEntities(subjectInfoId);
        LiveData<SubjectInfo> subjectInfoLiveData = groupPerformanceLessonsViewModel.getSubjectInfo();
        LiveData<List<Student>> studentsInGroupLiveData = Transformations.switchMap(subjectInfoLiveData, s -> {
            groupPerformanceLessonsViewModel.downloadStudentsInGroup(s.getGroup().getId());
            return groupPerformanceLessonsViewModel.getStudentsInGroup();
        });
        LiveData<Map<String, Module>> modulesLiveData = Transformations.switchMap(studentsInGroupLiveData,
                s -> groupPerformanceLessonsViewModel.getModules());
        LiveData<Map<String, List<StudentPerformanceInModule>>> studentsPerformancesInModulesLiveData = Transformations.switchMap(modulesLiveData, m ->
                groupPerformanceLessonsViewModel.getStudentsPerformancesInModules());

        groupPerformanceLessonsViewModel.downloadLessons(subjectInfoId);
        LiveData<Map<String, List<Lesson>>> lessonsLiveData = Transformations.switchMap(studentsPerformancesInModulesLiveData,
                e -> groupPerformanceLessonsViewModel.getLessons());
        LiveData<Map<String, Map<String, List<StudentLesson>>>> studentsLessonsLiveData = Transformations.switchMap(lessonsLiveData,
                e -> groupPerformanceLessonsViewModel.getStudentsLessons());

        studentsLessonsLiveData.observe(getViewLifecycleOwner(), studentsLessons -> {
            int page = getArguments() != null ? getArguments().getInt("openPage") : 0;
            ModulesPagerAdapter modulesPagerAdapter = new ModulesPagerAdapter(this);
            ViewPager2 viewPager = root.findViewById(R.id.modules_pager);
            viewPager.setAdapter(modulesPagerAdapter);
            viewPager.setCurrentItem(page, false);
            viewPager.setOffscreenPageLimit(2);
            viewPager.setUserInputEnabled(false);

            TabLayout tabLayout = root.findViewById(R.id.modules_tab_layout);
            new TabLayoutMediator(tabLayout, viewPager, (tab, position) ->
                    tab.setText("Модуль " + (position + 1))).attach();
        });


        return root;
    }
}
