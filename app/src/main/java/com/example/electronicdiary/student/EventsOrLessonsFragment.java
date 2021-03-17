package com.example.electronicdiary.student;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.electronicdiary.R;
import com.example.electronicdiary.Repository;

public class EventsOrLessonsFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_events_or_lessons, container, false);

        int position = getArguments().getInt("position");

        StudentPerformanceViewModel studentPerformanceViewModel = new ViewModelProvider(getParentFragment()).get(StudentPerformanceViewModel.class);

        final RecyclerView recyclerView = root.findViewById(R.id.student_events_list);
        if (position == 1) {
            studentPerformanceViewModel.getStudentEvents().observe(getViewLifecycleOwner(), studentEvents -> {
                if (studentEvents == null) {
                    return;
                }

                EventsAdapter eventsAdapter = new EventsAdapter(getContext(), studentEvents);
                recyclerView.setAdapter(eventsAdapter);
                recyclerView.setHasFixedSize(false);
            });
        } else {
            recyclerView.setVisibility(View.GONE);

            //TODO можно ли создать expandableList с двойной вложенностью?
            final ExpandableListView expandableListView = root.findViewById(R.id.lessons_by_modules);
            studentPerformanceViewModel.getStudentLessonsByModules().observe(getViewLifecycleOwner(),
                    studentLessonsByModules -> {
                        if (studentLessonsByModules == null) {
                            return;
                        }

                        LessonsAdapter lessonsAdapter = new LessonsAdapter(getContext(),
                                Repository.getInstance().getModules(),
                                studentLessonsByModules);
                        expandableListView.setVisibility(View.VISIBLE);
                        expandableListView.setAdapter(lessonsAdapter);
                    });
        }

        return root;
    }
}