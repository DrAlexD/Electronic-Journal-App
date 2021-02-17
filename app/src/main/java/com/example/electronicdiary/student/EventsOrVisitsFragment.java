package com.example.electronicdiary.student;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.electronicdiary.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class EventsOrVisitsFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_events_or_visits, container, false);

        int position = getArguments().getInt("position");

        if (position == 1)
            generateEventsList(root);
        else
            generateVisitsModuleList(root);

        return root;
    }

    private void generateEventsList(View root) {
        //TODO загрузка мероприятий студента
        ArrayList<Event> events = new ArrayList<>();
        events.add(new Event("РК1", 5));
        events.add(new Event("ДЗ1", 10));
        events.add(new Event("РК2", 15));
        events.add(new Event("ДЗ2", 20));
        events.add(new Event("РК3", 25));
        events.add(new Event("ДЗ3", 30));

        EventsAdapter eventsAdapter = new EventsAdapter(getContext(), events);

        final RecyclerView recyclerView = root.findViewById(R.id.student_events_list);
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(eventsAdapter);
    }

    private void generateVisitsModuleList(View root) {
        //TODO загрузка посещений студента
        final RecyclerView recyclerView = root.findViewById(R.id.student_events_list);
        recyclerView.setVisibility(View.GONE);

        HashMap<String, ArrayList<Visit>> visitsByModules = new HashMap<>();

        ArrayList<String> modules = new ArrayList<>();
        modules.add("Модуль 1");
        modules.add("Модуль 2");
        modules.add("Модуль 3");

        ArrayList<ArrayList<Visit>> visits = new ArrayList<>();
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

        //TODO можно ли создать expandableList с двойной вложенностью?
        VisitsAdapter visitsAdapter = new VisitsAdapter(getContext(), modules, visitsByModules);

        final ExpandableListView expandableListView = root.findViewById(R.id.visits_by_modules);
        expandableListView.setVisibility(View.VISIBLE);
        expandableListView.setAdapter(visitsAdapter);
        /*expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            Bundle bundle = new Bundle();
            bundle.putString("subject", subjects.get(groupPosition));
            bundle.putString("group", subjectsWithGroups.get(subjects.get(groupPosition)).get(childPosition));
            Navigation.findNavController(v).navigate(R.id.action_profile_to_group_performance, bundle);
            return true;
        });*/
    }
}