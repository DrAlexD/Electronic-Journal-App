package com.example.electronicdiary.group;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import com.example.electronicdiary.Event;
import com.example.electronicdiary.R;
import com.example.electronicdiary.Student;
import com.example.electronicdiary.StudentEvent;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class GroupPerformanceFragment extends Fragment {
    private int semesterId;
    private int groupId;
    private int subjectId;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_group_performance, container, false);

        semesterId = getArguments().getInt("semesterId");
        groupId = getArguments().getInt("groupId");
        subjectId = getArguments().getInt("subjectId");

        GroupPerformanceViewModel groupPerformanceViewModel = new ViewModelProvider(this).get(GroupPerformanceViewModel.class);
        groupPerformanceViewModel.setSemesterId(semesterId);
        groupPerformanceViewModel.setGroupId(groupId);
        groupPerformanceViewModel.setSubjectId(subjectId);

        groupPerformanceViewModel.downloadStudentsInGroup(groupId);
        groupPerformanceViewModel.downloadEvents(groupId, subjectId, semesterId);
        groupPerformanceViewModel.downloadLessonsByModules(groupId, subjectId, semesterId);
        groupPerformanceViewModel.downloadStudentsEvents(groupId, subjectId, semesterId);
        groupPerformanceViewModel.downloadStudentsLessonsByModules(groupId, subjectId, semesterId);

        //TODO подумать как оставлять первую строку и первый столбец на месте при скроллинге
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            boolean isProfessorRules = sharedPreferences.getBoolean(getString(R.string.is_professor_rules), false);

            Button addEventButton = root.findViewById(R.id.addEventButton);
            addEventButton.setVisibility(isProfessorRules ? View.VISIBLE : View.GONE);
            addEventButton.setOnClickListener(view -> {
                Bundle bundle = new Bundle();
                bundle.putInt("semesterId", semesterId);
                bundle.putInt("groupId", groupId);
                bundle.putInt("subjectId", subjectId);
                Navigation.findNavController(view).navigate(R.id.action_group_performance_to_dialog_event_adding, bundle);
            });

            //FIXME при обновлении студентов обновляются ли автоматически и оценки по мероприятиям
/*            groupPerformanceViewModel.getStudentsInGroup().observe(getViewLifecycleOwner(), students -> {
                if (students == null) {
                    return;
                }

                generateEventsTable(root, students, groupPerformanceViewModel.getStudentsEvents().getValue());
            });*/

            groupPerformanceViewModel.getStudentsEvents().observe(getViewLifecycleOwner(), studentsEvents -> {
                if (studentsEvents == null) {
                    return;
                }

                generateEventsTable(root, groupPerformanceViewModel.getStudentsInGroup().getValue(),
                        groupPerformanceViewModel.getEvents().getValue(), studentsEvents);
            });
        } else {
            int page = getArguments() != null ? getArguments().getInt("openPage") : 0;
            //FIXME фрагменты-дети не удаляются, при этом создаются лишние
            ModulesPagerAdapter modulesPagerAdapter = new ModulesPagerAdapter(this);
            ViewPager2 viewPager = root.findViewById(R.id.modules_pager);
            viewPager.setAdapter(modulesPagerAdapter);
            viewPager.setCurrentItem(page, false);
            viewPager.setOffscreenPageLimit(2);
            viewPager.setUserInputEnabled(false);

            //TODO добавить вторую панель перелистывания для семинара/лекции
            TabLayout tabLayout = root.findViewById(R.id.modules_tab_layout);
            new TabLayoutMediator(tabLayout, viewPager, (tab, position) ->
                    tab.setText("Модуль " + (position + 1))).attach();
        }

        return root;
    }

    private void generateEventsTable(View root, ArrayList<Student> students, ArrayList<Event> events, ArrayList<ArrayList<StudentEvent>> studentsEvents) {
        TableLayout studentsInModuleEventsTable = root.findViewById(R.id.studentsInModuleEventsTable);
        int padding2inDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());
        int padding5inDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());

        TableRow eventsRow = generateEventsRow(padding2inDp, padding5inDp, events);
        studentsInModuleEventsTable.addView(eventsRow);

        for (int i = 0; i < students.size(); i++) {
            TableRow pointsRow = generatePointsRow(padding2inDp, padding5inDp, students.get(i), events, studentsEvents.get(i));
            studentsInModuleEventsTable.addView(pointsRow);
        }
    }

    private TableRow generateEventsRow(int padding2inDp, int padding5inDp, ArrayList<Event> events) {
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

        for (Event event : events) {
            TextView eventView = new TextView(getContext());
            eventView.setTextSize(20);
            eventView.setText(event.getTitle());
            eventView.setPadding(padding5inDp, padding2inDp, padding5inDp, padding2inDp);
            eventView.setGravity(Gravity.CENTER);
            eventView.setOnClickListener(view -> {
                Bundle bundle = new Bundle();
                bundle.putInt("eventId", event.getId());
                Navigation.findNavController(view).navigate(R.id.action_group_performance_to_dialog_event_editing, bundle);
            });
            eventsRow.addView(eventView);
        }

        return eventsRow;
    }

    private TableRow generatePointsRow(int padding2inDp, int padding5inDp, Student student, ArrayList<Event> events,
                                       ArrayList<StudentEvent> studentEvents) {
        TableRow pointsRow = new TableRow(getContext());
        pointsRow.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE |
                LinearLayout.SHOW_DIVIDER_BEGINNING | LinearLayout.SHOW_DIVIDER_END);
        pointsRow.setDividerDrawable(getResources().getDrawable(R.drawable.divider));

        TextView studentView = new TextView(getContext());
        studentView.setText(student.getFullName());
        studentView.setTextSize(20);
        studentView.setPadding(padding5inDp, padding2inDp, padding5inDp, padding2inDp);
        studentView.setGravity(Gravity.CENTER);
        studentView.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putInt("studentId", student.getId());
            bundle.putInt("semesterId", semesterId);
            Navigation.findNavController(view).navigate(R.id.action_group_performance_to_student_profile, bundle);
        });
        pointsRow.addView(studentView);

        for (Event event : events) {
            TextView pointsView = new TextView(getContext());
            pointsView.setTextSize(20);
            pointsView.setPadding(padding5inDp, padding2inDp, padding5inDp, padding2inDp);
            StudentEvent studentEventChosen = null;
            int lastAttempt = 0;
            for (StudentEvent studentEvent : studentEvents) {
                if (studentEvent.getStudentId() == student.getId() && studentEvent.getEventId() == event.getId()
                        && studentEvent.getAttemptNumber() > lastAttempt) {
                    studentEventChosen = studentEvent;
                    lastAttempt = studentEvent.getAttemptNumber();
                }
            }
            pointsView.setText(lastAttempt == 0 ? "" : String.valueOf(studentEventChosen.getEarnedPoints() + studentEventChosen.getBonusPoints()));
            pointsView.setGravity(Gravity.CENTER);
            pointsView.setOnClickListener(view -> {
                Bundle bundle = new Bundle();
                bundle.putInt("studentId", student.getId());
                bundle.putInt("eventId", event.getId());
                Navigation.findNavController(view).navigate(R.id.action_group_performance_to_dialog_student_event, bundle);
            });
            pointsRow.addView(pointsView);
        }

        return pointsRow;
    }
}