package com.example.electronicdiary.student.performance;

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
import com.example.electronicdiary.student.Event;
import com.example.electronicdiary.student.Visit;

import java.util.ArrayList;
import java.util.HashMap;

public class EventsOrVisitsFragment extends Fragment {
    private int position;

    private ArrayList<Event> events;

    private HashMap<String, ArrayList<Visit>> visitsByModules;
    private ArrayList<String> modules;
    private ArrayList<ArrayList<Visit>> visits;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_events_or_visits, container, false);

        position = getArguments().getInt("position");

        StudentPerformanceViewModel studentPerformanceViewModel = new ViewModelProvider(getParentFragment()).get(StudentPerformanceViewModel.class);
        events = studentPerformanceViewModel.getEvents().getValue();
        visitsByModules = studentPerformanceViewModel.getVisitsByModules().getValue();
        modules = studentPerformanceViewModel.getModules().getValue();
        visits = studentPerformanceViewModel.getVisits().getValue();

        EventsOrVisitsViewModel eventsOrVisitsViewModel = new ViewModelProvider(this).get(EventsOrVisitsViewModel.class);
        eventsOrVisitsViewModel.setPosition(position);

        if (position == 1) {
            generateEventsList(root);
        } else {
            generateVisitsByModuleList(root);
        }

        return root;
    }

    private void generateEventsList(View root) {
        EventsAdapter eventsAdapter = new EventsAdapter(getContext(), events);
        final RecyclerView recyclerView = root.findViewById(R.id.student_events_list);
        recyclerView.setAdapter(eventsAdapter);
        recyclerView.setHasFixedSize(false);
    }

    private void generateVisitsByModuleList(View root) {
        final RecyclerView recyclerView = root.findViewById(R.id.student_events_list);
        recyclerView.setVisibility(View.GONE);

        //TODO можно ли создать expandableList с двойной вложенностью?
        VisitsAdapter visitsAdapter = new VisitsAdapter(getContext(), modules, visitsByModules);

        final ExpandableListView expandableListView = root.findViewById(R.id.visits_by_modules);
        expandableListView.setVisibility(View.VISIBLE);
        expandableListView.setAdapter(visitsAdapter);
    }
}