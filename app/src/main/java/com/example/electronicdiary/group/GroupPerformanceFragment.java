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
import com.example.electronicdiary.GroupInfo;
import com.example.electronicdiary.R;
import com.example.electronicdiary.Repository;
import com.example.electronicdiary.Student;
import com.example.electronicdiary.StudentEvent;
import com.example.electronicdiary.StudentPerformanceInModule;
import com.example.electronicdiary.StudentPerformanceInSubject;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.HashMap;

public class GroupPerformanceFragment extends Fragment {
    private int groupId;
    private int subjectId;
    private int lecturerId;
    private int seminarianId;
    private int semesterId;

    private GroupPerformanceViewModel groupPerformanceViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_group_performance, container, false);

        groupId = getArguments().getInt("groupId");
        subjectId = getArguments().getInt("subjectId");
        lecturerId = getArguments().getInt("lecturerId");
        seminarianId = getArguments().getInt("seminarianId");
        semesterId = getArguments().getInt("semesterId");

        GroupPerformanceViewModel groupPerformanceViewModel = new ViewModelProvider(this).get(GroupPerformanceViewModel.class);
        groupPerformanceViewModel.downloadStudentsEventsAndLessons(groupId, subjectId, lecturerId, seminarianId, semesterId);

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

            groupPerformanceViewModel.getStudentsEvents().observe(getViewLifecycleOwner(), studentsEvents -> {
                if (studentsEvents == null) {
                    return;
                }

                generateEventsTable(root, studentsEvents);
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

    private void generateEventsTable(View root, HashMap<Integer, ArrayList<ArrayList<StudentEvent>>> studentsEvents) {
        TableLayout studentsInModuleEventsTable = root.findViewById(R.id.studentsInModuleEventsTable);
        int padding2inDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());
        int padding5inDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());

        HashMap<Integer, ArrayList<Event>> events = groupPerformanceViewModel.getEvents().getValue();
        TableRow eventsRow = generateEventsRow(padding2inDp, padding5inDp, events);
        studentsInModuleEventsTable.addView(eventsRow);

        ArrayList<Student> students = groupPerformanceViewModel.getStudentsInGroup().getValue();
        for (int i = 0; i < students.size(); i++) {
            TableRow pointsRow = generatePointsRow(i, padding2inDp, padding5inDp, students.get(i), events, studentsEvents);
            studentsInModuleEventsTable.addView(pointsRow);
        }
    }

    private TableRow generateEventsRow(int padding2inDp, int padding5inDp, HashMap<Integer, ArrayList<Event>> events) {
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

        ArrayList<Integer> modules = Repository.getInstance().getModules();

        for (int moduleNumber : modules) {
            for (Event event : events.get(moduleNumber - 1)) {
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

            TextView moduleView = new TextView(getContext());
            moduleView.setTextSize(20);
            moduleView.setText("Модуль " + moduleNumber);
            moduleView.setPadding(padding5inDp, padding2inDp, padding5inDp, padding2inDp);
            moduleView.setGravity(Gravity.CENTER);
            moduleView.setOnClickListener(view -> {
                Bundle bundle = new Bundle();
                bundle.putInt("moduleNumber", moduleNumber);
                bundle.putInt("groupId", groupId);
                bundle.putInt("subjectId", subjectId);
                bundle.putInt("lecturerId", lecturerId);
                bundle.putInt("seminarianId", seminarianId);
                bundle.putInt("semesterId", semesterId);
                Navigation.findNavController(view).navigate(R.id.action_group_performance_to_dialog_module_info_editing, bundle);
            });
            eventsRow.addView(moduleView);
        }

        TextView resultPointsView = new TextView(getContext());
        resultPointsView.setTextSize(20);
        resultPointsView.setText("Итоговые баллы");
        resultPointsView.setPadding(padding5inDp, padding2inDp, padding5inDp, padding2inDp);
        resultPointsView.setGravity(Gravity.CENTER);
        resultPointsView.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putInt("groupId", groupId);
            bundle.putInt("subjectId", subjectId);
            bundle.putInt("lecturerId", lecturerId);
            bundle.putInt("seminarianId", seminarianId);
            bundle.putInt("semesterId", semesterId);
            Navigation.findNavController(view).navigate(R.id.action_group_performance_to_dialog_group_info_editing, bundle);
        });
        eventsRow.addView(resultPointsView);

        GroupInfo groupInfo = groupPerformanceViewModel.getGroupInfo().getValue();
        if (groupInfo.isExam()) {
            TextView examPointsView = new TextView(getContext());
            examPointsView.setTextSize(20);
            examPointsView.setText("Экзамен");
            examPointsView.setPadding(padding5inDp, padding2inDp, padding5inDp, padding2inDp);
            examPointsView.setGravity(Gravity.CENTER);
            examPointsView.setOnClickListener(view -> {
                Bundle bundle = new Bundle();
                bundle.putInt("groupId", groupId);
                bundle.putInt("subjectId", subjectId);
                bundle.putInt("lecturerId", lecturerId);
                bundle.putInt("seminarianId", seminarianId);
                bundle.putInt("semesterId", semesterId);
                Navigation.findNavController(view).navigate(R.id.action_group_performance_to_dialog_group_info_editing, bundle);
            });
            eventsRow.addView(examPointsView);

            TextView markView = new TextView(getContext());
            markView.setTextSize(20);
            markView.setText("Оценка");
            markView.setPadding(padding5inDp, padding2inDp, padding5inDp, padding2inDp);
            markView.setGravity(Gravity.CENTER);
            markView.setOnClickListener(view -> {
                Bundle bundle = new Bundle();
                bundle.putInt("groupId", groupId);
                bundle.putInt("subjectId", subjectId);
                bundle.putInt("lecturerId", lecturerId);
                bundle.putInt("seminarianId", seminarianId);
                bundle.putInt("semesterId", semesterId);
                Navigation.findNavController(view).navigate(R.id.action_group_performance_to_dialog_group_info_editing, bundle);
            });
            eventsRow.addView(markView);
        }

        return eventsRow;
    }

    private TableRow generatePointsRow(int i, int padding2inDp, int padding5inDp, Student student,
                                       HashMap<Integer, ArrayList<Event>> events,
                                       HashMap<Integer, ArrayList<ArrayList<StudentEvent>>> studentsEvents) {
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

        ArrayList<Integer> modules = Repository.getInstance().getModules();
        for (int moduleNumber : modules) {
            for (Event event : events.get(moduleNumber - 1)) {
                TextView pointsView = new TextView(getContext());
                pointsView.setTextSize(20);
                pointsView.setPadding(padding5inDp, padding2inDp, padding5inDp, padding2inDp);

                StudentEvent studentEventChosen = null;
                int lastAttempt = 0;
                for (StudentEvent studentEvent : studentsEvents.get(moduleNumber - 1).get(i)) {
                    if (studentEvent.getStudentId() == student.getId() && studentEvent.getEventId() == event.getId()
                            && studentEvent.getAttemptNumber() > lastAttempt) {
                        studentEventChosen = studentEvent;
                        lastAttempt = studentEvent.getAttemptNumber();
                    }
                }

                pointsView.setText(lastAttempt == 0 ? "" : String.valueOf(studentEventChosen.getEarnedPoints() + studentEventChosen.getBonusPoints()));
                pointsView.setGravity(Gravity.CENTER);

                int finalLastAttempt = lastAttempt;
                pointsView.setOnClickListener(view -> {
                    Bundle bundle = new Bundle();
                    bundle.putInt("attemptNumber", finalLastAttempt);
                    bundle.putInt("eventId", event.getId());
                    bundle.putInt("studentId", student.getId());
                    bundle.putInt("moduleNumber", moduleNumber);
                    bundle.putInt("groupId", groupId);
                    bundle.putInt("subjectId", subjectId);
                    bundle.putInt("lecturerId", lecturerId);
                    bundle.putInt("seminarianId", seminarianId);
                    bundle.putInt("semesterId", semesterId);
                    Navigation.findNavController(view).navigate(R.id.action_group_performance_to_dialog_student_event, bundle);
                });
                pointsRow.addView(pointsView);
            }

            StudentPerformanceInModule studentPerformanceInModule = groupPerformanceViewModel.getStudentsPerformancesInModules().
                    getValue().get(moduleNumber - 1).get(i);
            TextView moduleView = new TextView(getContext());
            moduleView.setTextSize(20);
            moduleView.setText(studentPerformanceInModule.getEarnedPoints());
            moduleView.setTextColor(studentPerformanceInModule.isHaveCredit() ? getResources().getColor(R.color.green) : getResources().getColor(R.color.red));
            moduleView.setPadding(padding5inDp, padding2inDp, padding5inDp, padding2inDp);
            moduleView.setGravity(Gravity.CENTER);
            moduleView.setOnClickListener(view -> {
                Bundle bundle = new Bundle();
                bundle.putInt("moduleNumber", moduleNumber);
                bundle.putInt("studentId", student.getId());
                bundle.putInt("groupId", groupId);
                bundle.putInt("subjectId", subjectId);
                bundle.putInt("lecturerId", lecturerId);
                bundle.putInt("seminarianId", seminarianId);
                bundle.putInt("semesterId", semesterId);
                Navigation.findNavController(view).navigate(R.id.action_group_performance_to_dialog_module_performance_editing, bundle);
            });
            pointsRow.addView(moduleView);
        }

        StudentPerformanceInSubject studentPerformanceInSubject = groupPerformanceViewModel.getStudentsPerformancesInSubject().
                getValue().get(i);
        TextView resultPointsView = new TextView(getContext());
        resultPointsView.setTextSize(20);
        resultPointsView.setText(studentPerformanceInSubject.getEarnedPoints() + studentPerformanceInSubject.getBonusPoints());
        resultPointsView.setTextColor(studentPerformanceInSubject.isHaveCreditOrAdmission() ? getResources().getColor(R.color.green) : getResources().getColor(R.color.red));
        resultPointsView.setPadding(padding5inDp, padding2inDp, padding5inDp, padding2inDp);
        resultPointsView.setGravity(Gravity.CENTER);
        resultPointsView.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putInt("studentId", student.getId());
            bundle.putInt("groupId", groupId);
            bundle.putInt("subjectId", subjectId);
            bundle.putInt("lecturerId", lecturerId);
            bundle.putInt("seminarianId", seminarianId);
            bundle.putInt("semesterId", semesterId);
            Navigation.findNavController(view).navigate(R.id.action_group_performance_to_dialog_subject_performance_editing, bundle);
        });
        pointsRow.addView(resultPointsView);

        GroupInfo groupInfo = groupPerformanceViewModel.getGroupInfo().getValue();
        if (groupInfo.isExam()) {
            TextView examPointsView = new TextView(getContext());
            examPointsView.setTextSize(20);
            examPointsView.setText(studentPerformanceInSubject.getEarnedExamPoints());
            resultPointsView.setTextColor(studentPerformanceInSubject.getEarnedExamPoints() >= 18 ? getResources().getColor(R.color.green) : getResources().getColor(R.color.red));
            examPointsView.setPadding(padding5inDp, padding2inDp, padding5inDp, padding2inDp);
            examPointsView.setGravity(Gravity.CENTER);
            examPointsView.setOnClickListener(view -> {
                Bundle bundle = new Bundle();
                bundle.putInt("studentId", student.getId());
                bundle.putInt("groupId", groupId);
                bundle.putInt("subjectId", subjectId);
                bundle.putInt("lecturerId", lecturerId);
                bundle.putInt("seminarianId", seminarianId);
                bundle.putInt("semesterId", semesterId);
                Navigation.findNavController(view).navigate(R.id.action_group_performance_to_dialog_subject_performance_editing, bundle);
            });
            pointsRow.addView(examPointsView);

            TextView markView = new TextView(getContext());
            markView.setTextSize(20);
            markView.setText(studentPerformanceInSubject.getMark());
            markView.setPadding(padding5inDp, padding2inDp, padding5inDp, padding2inDp);
            markView.setGravity(Gravity.CENTER);
            markView.setOnClickListener(view -> {
                Bundle bundle = new Bundle();
                bundle.putInt("studentId", student.getId());
                bundle.putInt("groupId", groupId);
                bundle.putInt("subjectId", subjectId);
                bundle.putInt("lecturerId", lecturerId);
                bundle.putInt("seminarianId", seminarianId);
                bundle.putInt("semesterId", semesterId);
                Navigation.findNavController(view).navigate(R.id.action_group_performance_to_dialog_subject_performance_editing, bundle);
            });
            pointsRow.addView(markView);
        }

        return pointsRow;
    }
}