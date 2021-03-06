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

import com.example.electronicdiary.Event;
import com.example.electronicdiary.Lesson;
import com.example.electronicdiary.R;

import java.util.ArrayList;
import java.util.HashMap;

public class EventsOrLessonsFragment extends Fragment {
    private int position;

    private ArrayList<Event> events;

    private HashMap<String, ArrayList<Lesson>> lessonsByModules;
    private ArrayList<String> modules;
    private ArrayList<ArrayList<Lesson>> lessons;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_events_or_lessons, container, false);

        position = getArguments().getInt("position");

        StudentPerformanceViewModel studentPerformanceViewModel = new ViewModelProvider(getParentFragment()).get(StudentPerformanceViewModel.class);
        events = studentPerformanceViewModel.getEvents().getValue();
        lessonsByModules = studentPerformanceViewModel.getLessonsByModules().getValue();
        modules = studentPerformanceViewModel.getModules().getValue();
        lessons = studentPerformanceViewModel.getLessons().getValue();

        EventsOrLessonsViewModel eventsOrLessonsViewModel = new ViewModelProvider(this).get(EventsOrLessonsViewModel.class);
        eventsOrLessonsViewModel.setPosition(position);

        if (position == 1) {
            generateEventsList(root);
        } else {
            generateLessonsByModuleList(root);
        }

        return root;
    }

    private void generateEventsList(View root) {
        EventsAdapter eventsAdapter = new EventsAdapter(getContext(), events);
        final RecyclerView recyclerView = root.findViewById(R.id.student_events_list);
        recyclerView.setAdapter(eventsAdapter);
        recyclerView.setHasFixedSize(false);
    }

    private void generateLessonsByModuleList(View root) {
        final RecyclerView recyclerView = root.findViewById(R.id.student_events_list);
        recyclerView.setVisibility(View.GONE);

        //TODO можно ли создать expandableList с двойной вложенностью?
        LessonsAdapter lessonsAdapter = new LessonsAdapter(getContext(), modules, lessonsByModules);

        final ExpandableListView expandableListView = root.findViewById(R.id.lessons_by_modules);
        expandableListView.setVisibility(View.VISIBLE);
        expandableListView.setAdapter(lessonsAdapter);
    }
}