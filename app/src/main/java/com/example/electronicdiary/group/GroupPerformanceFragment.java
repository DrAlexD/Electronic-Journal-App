package com.example.electronicdiary.group;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import com.example.electronicdiary.R;
import com.example.electronicdiary.student.Student;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GroupPerformanceFragment extends Fragment {
    private String group;
    private String subject;

    private ArrayList<Student> students;
    private ArrayList<String> events;
    private ArrayList<Date> visits;
    private ArrayList<StudentInModule> studentsInModule;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_group_performance, container, false);

        group = getArguments().getString("group");
        subject = getArguments().getString("subject");

        downloadData();

        GroupPerformanceViewModel groupPerformanceViewModel = new ViewModelProvider(this).get(GroupPerformanceViewModel.class);
        groupPerformanceViewModel.setGroup(group);
        groupPerformanceViewModel.setSubject(subject);

        groupPerformanceViewModel.setStudentsInModule(studentsInModule);
        groupPerformanceViewModel.setStudents(students);
        groupPerformanceViewModel.setEvents(events);
        groupPerformanceViewModel.setVisits(visits);

        //TODO подумать как оставлять первую строку и первый столбец на месте при скроллинге
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            generateEventsTable(root);
        } else {
            //TODO фрагменты-дети не удаляются, при этом создаются лишние
            ModulesPagerAdapter modulesPagerAdapter = new ModulesPagerAdapter(this);
            ViewPager2 viewPager = root.findViewById(R.id.modules_pager);
            viewPager.setAdapter(modulesPagerAdapter);
            viewPager.setCurrentItem(0);
            viewPager.setOffscreenPageLimit(2);
            viewPager.setUserInputEnabled(false);

            //TODO добавить вторую панель перелистывания для семинара/лекции
            TabLayout tabLayout = root.findViewById(R.id.modules_tab_layout);
            new TabLayoutMediator(tabLayout, viewPager, (tab, position) ->
                    tab.setText("Модуль " + (position + 1))).attach();
        }

        return root;
    }

    private void downloadData() {
        //TODO загрузка всех студентов, событий, посещений: возможно лучше поместить в ViewModel, и здесь поставить observe на поля
        students = new ArrayList<>();
        students.add(new Student("1ИУ9-11", "1Александр", "1Другаков"));
        students.add(new Student("2ИУ9-21", "2Александр", "2Другаков"));
        students.add(new Student("3ИУ9-31", "3Александр", "3Другаков"));
        students.add(new Student("4ИУ9-41", "4Александр", "4Другаков"));
        students.add(new Student("5ИУ9-51", "5Александр", "5Другаков"));

        events = new ArrayList<>();
        events.add("РК1");
        events.add("ДЗ1");
        events.add("РК2");
        events.add("ДЗ2");
        events.add("РК3");
        events.add("ДЗ3");

        visits = new ArrayList<>();
        visits.add(new Date(2020, 0, 1));
        visits.add(new Date(2020, 1, 2));
        visits.add(new Date(2020, 2, 3));
        visits.add(new Date(2020, 3, 4));
        visits.add(new Date(2020, 4, 5));
        visits.add(new Date(2020, 5, 6));
        visits.add(new Date(2020, 6, 7));
        visits.add(new Date(2020, 7, 8));
        visits.add(new Date(2020, 8, 9));
        visits.add(new Date(2020, 9, 10));
        visits.add(new Date(2020, 10, 11));
        visits.add(new Date(2020, 11, 12));

        studentsInModule = new ArrayList<>();
        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);

            Map<String, Integer> eventsWithPoints = new HashMap<>();
            for (int j = 5; (j - 5) / 5 < events.size(); j += 5) {
                String event = events.get((j - 5) / 5);
                eventsWithPoints.put(event, j + i);
            }

            Map<Date, Integer> visitsWithPoints = new HashMap<>();
            for (int j = 3; (j - 3) / 3 < visits.size(); j += 3) {
                Date visit = visits.get((j - 3) / 3);
                visitsWithPoints.put(visit, j + i);
            }

            studentsInModule.add(new StudentInModule(student, eventsWithPoints, visitsWithPoints));
        }
    }

    private void generateEventsTable(View root) {
        TableLayout studentsInModuleEventsTable = root.findViewById(R.id.studentsInModuleEventsTable);
        int padding2inDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());
        int padding5inDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());

        TableRow eventsRow = generateEventsRow(padding2inDp, padding5inDp);
        studentsInModuleEventsTable.addView(eventsRow);

        for (int i = 0; i < studentsInModule.size(); i++) {
            TableRow pointsRow = generatePointsRow(i, padding2inDp, padding5inDp);
            studentsInModuleEventsTable.addView(pointsRow);
        }
    }

    private TableRow generateEventsRow(int padding2inDp, int padding5inDp) {
        TableRow eventsRow = new TableRow(getContext());
        eventsRow.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE |
                LinearLayout.SHOW_DIVIDER_BEGINNING | LinearLayout.SHOW_DIVIDER_END);
        eventsRow.setDividerDrawable(getResources().getDrawable(R.drawable.divider));

        TextView infoView = new TextView(getContext());
        infoView.setTextSize(20);
        infoView.setText("Студент\\Мероприятие");
        infoView.setPadding(padding5inDp, padding2inDp, padding5inDp, padding2inDp);
        infoView.setGravity(Gravity.CENTER);
        eventsRow.addView(infoView);

        for (String eventTitle : events) {
            TextView eventView = new TextView(getContext());
            eventView.setTextSize(20);
            eventView.setText(eventTitle);
            eventView.setPadding(padding5inDp, padding2inDp, padding5inDp, padding2inDp);
            eventView.setGravity(Gravity.CENTER);
            eventView.setOnClickListener(view -> {
                EventInfoDialogFragment eventInfoDialogFragment = new EventInfoDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putString("eventTitle", eventTitle);
                eventInfoDialogFragment.setArguments(bundle);
                eventInfoDialogFragment.show(getChildFragmentManager(), "eventInfo");
            });
            eventsRow.addView(eventView);
        }

        return eventsRow;
    }

    private TableRow generatePointsRow(int i, int padding2inDp, int padding5inDp) {
        StudentInModule studentInModule = studentsInModule.get(i);

        TableRow pointsRow = new TableRow(getContext());
        pointsRow.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE |
                LinearLayout.SHOW_DIVIDER_BEGINNING | LinearLayout.SHOW_DIVIDER_END);
        pointsRow.setDividerDrawable(getResources().getDrawable(R.drawable.divider));

        TextView studentView = new TextView(getContext());
        studentView.setText(students.get(i).getFullName());
        studentView.setTextSize(20);
        studentView.setPadding(padding5inDp, padding2inDp, padding5inDp, padding2inDp);
        studentView.setGravity(Gravity.CENTER);
        studentView.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString("student", ((TextView) view).getText().toString());
            bundle.putString("group", group);
            Navigation.findNavController(view).navigate(R.id.action_group_performance_to_student_profile, bundle);
        });
        pointsRow.addView(studentView);

        for (String event : events) {
            TextView pointsView = new TextView(getContext());
            pointsView.setTextSize(20);
            pointsView.setPadding(padding5inDp, padding2inDp, padding5inDp, padding2inDp);
            if (studentInModule.getEventsWithPoints().containsKey(event))
                pointsView.setText(studentInModule.getEventsWithPoints().get(event).toString());
            else
                pointsView.setText("");
            pointsView.setGravity(Gravity.CENTER);
            pointsRow.addView(pointsView);
        }

        return pointsRow;
    }
}