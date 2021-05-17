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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import com.example.electronicdiary.R;
import com.example.electronicdiary.Repository;
import com.example.electronicdiary.data_classes.Event;
import com.example.electronicdiary.data_classes.Lesson;
import com.example.electronicdiary.data_classes.Module;
import com.example.electronicdiary.data_classes.Student;
import com.example.electronicdiary.data_classes.StudentEvent;
import com.example.electronicdiary.data_classes.StudentLesson;
import com.example.electronicdiary.data_classes.StudentPerformanceInModule;
import com.example.electronicdiary.data_classes.StudentPerformanceInSubject;
import com.example.electronicdiary.data_classes.SubjectInfo;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;
import java.util.Map;

public class GroupPerformanceFragment extends Fragment {
    private long subjectInfoId;

    private GroupPerformanceViewModel groupPerformanceViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_group_performance, container, false);

        subjectInfoId = getArguments().getLong("subjectInfoId");

        groupPerformanceViewModel = new ViewModelProvider(this).get(GroupPerformanceViewModel.class);
        groupPerformanceViewModel.downloadEntities(subjectInfoId);
        LiveData<SubjectInfo> subjectInfoLiveData = groupPerformanceViewModel.getSubjectInfo();
        LiveData<List<Student>> studentsInGroupLiveData = Transformations.switchMap(subjectInfoLiveData, s -> {
            groupPerformanceViewModel.downloadStudentsInGroup(s.getGroup().getId());
            return groupPerformanceViewModel.getStudentsInGroup();
        });
        LiveData<Map<String, Module>> modulesLiveData = Transformations.switchMap(studentsInGroupLiveData,
                s -> groupPerformanceViewModel.getModules());
        LiveData<Map<String, List<StudentPerformanceInModule>>> studentsPerformancesInModulesLiveData = Transformations.switchMap(modulesLiveData,
                m -> groupPerformanceViewModel.getStudentsPerformancesInModules());

        //TODO подумать как оставлять первую строку и первый столбец на месте при скроллинге
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            boolean isProfessorRules = sharedPreferences.getBoolean(getString(R.string.is_professor_rules), false);

            Button addEventButton = root.findViewById(R.id.addEventButton);
            addEventButton.setVisibility(isProfessorRules ? View.VISIBLE : View.GONE);
            addEventButton.setOnClickListener(view -> {
                Bundle bundle = new Bundle();
                bundle.putLong("subjectInfoId", subjectInfoId);
                Navigation.findNavController(view).navigate(R.id.action_group_performance_to_dialog_event_adding, bundle);
            });

            groupPerformanceViewModel.downloadEvents(subjectInfoId);
            LiveData<List<StudentPerformanceInSubject>> studentsPerformancesInSubjectLiveData = Transformations.switchMap(studentsPerformancesInModulesLiveData,
                    s -> groupPerformanceViewModel.getStudentsPerformancesInSubject());
            LiveData<Map<String, List<Event>>> eventsLiveData = Transformations.switchMap(studentsPerformancesInSubjectLiveData,
                    s -> groupPerformanceViewModel.getEvents());
            LiveData<Map<String, Map<String, List<StudentEvent>>>> studentsEventsLiveData = Transformations.switchMap(eventsLiveData,
                    e -> groupPerformanceViewModel.getStudentsEvents());

            studentsEventsLiveData.observe(getViewLifecycleOwner(), studentsEvents -> {
                if (groupPerformanceViewModel.getEvents().getValue() != null) {
                    generateEventsTable(root);
                }
            });
        } else {
            groupPerformanceViewModel.downloadLessons(subjectInfoId);
            LiveData<Map<String, List<Lesson>>> lessonsLiveData = Transformations.switchMap(studentsPerformancesInModulesLiveData,
                    s -> groupPerformanceViewModel.getLessons());
            LiveData<Map<String, Map<String, List<StudentLesson>>>> studentsLessonsLiveData = Transformations.switchMap(lessonsLiveData,
                    e -> groupPerformanceViewModel.getStudentsLessons());

            studentsLessonsLiveData.observe(getViewLifecycleOwner(), studentsLessons -> {
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
            });
        }

        return root;
    }

    private void generateEventsTable(View root) {
        TableLayout studentsInModuleEventsTable = root.findViewById(R.id.studentsInModuleEventsTable);
        studentsInModuleEventsTable.removeAllViews();
        int padding2inDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());
        int padding5inDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());

        TableRow eventsRow = generateEventsRow(padding2inDp, padding5inDp);
        studentsInModuleEventsTable.addView(eventsRow);
        List<Student> students = groupPerformanceViewModel.getStudentsInGroup().getValue();

        for (Student student : students) {
            TableRow pointsRow = generatePointsRow(padding2inDp, padding5inDp, student);
            studentsInModuleEventsTable.addView(pointsRow);
        }
    }

    private TableRow generateEventsRow(int padding2inDp, int padding5inDp) {
        Map<String, List<Event>> events = groupPerformanceViewModel.getEvents().getValue();
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

        List<Integer> modulesNumbers = Repository.getInstance().getModulesNumbers();
        Map<String, Module> modules = groupPerformanceViewModel.getModules().getValue();

        for (Integer moduleNumber : modulesNumbers) {
            if (events.get(String.valueOf(moduleNumber)) != null) {
                for (Event event : events.get(String.valueOf(moduleNumber))) {
                    TextView eventView = new TextView(getContext());
                    eventView.setTextSize(20);
                    eventView.setText(event.getTitle());
                    eventView.setPadding(padding5inDp, padding2inDp, padding5inDp, padding2inDp);
                    eventView.setGravity(Gravity.CENTER);
                    eventView.setOnClickListener(view -> {
                        Bundle bundle = new Bundle();
                        bundle.putLong("eventId", event.getId());
                        bundle.putString("eventTitle", event.getTitle());
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
                    bundle.putLong("moduleId", modules.get(String.valueOf(moduleNumber)).getId());
                    bundle.putInt("moduleNumber", moduleNumber);
                    Navigation.findNavController(view).navigate(R.id.action_group_performance_to_dialog_module_info_editing, bundle);
                });
                eventsRow.addView(moduleView);
            }
        }

        if ((events.get(String.valueOf(1)) != null && events.get(String.valueOf(2)) != null) ||
                (events.get(String.valueOf(2)) != null && events.get(String.valueOf(3)) != null) ||
                (events.get(String.valueOf(1)) != null && events.get(String.valueOf(3)) != null)) {
            TextView resultPointsView = new TextView(getContext());
            resultPointsView.setTextSize(20);
            resultPointsView.setText("Итоговые баллы");
            resultPointsView.setPadding(padding5inDp, padding2inDp, padding5inDp, padding2inDp);
            resultPointsView.setGravity(Gravity.CENTER);
            resultPointsView.setOnClickListener(view -> {
                Bundle bundle = new Bundle();
                bundle.putLong("subjectInfoId", subjectInfoId);
                Navigation.findNavController(view).navigate(R.id.action_group_performance_to_dialog_subject_info_editing, bundle);
            });
            eventsRow.addView(resultPointsView);
        }

        if (events.get(String.valueOf(1)) != null && events.get(String.valueOf(2)) != null &&
                events.get(String.valueOf(3)) != null) {
            SubjectInfo subjectInfo = groupPerformanceViewModel.getSubjectInfo().getValue();
            if (subjectInfo.isExam()) {
                TextView examPointsView = new TextView(getContext());
                examPointsView.setTextSize(20);
                examPointsView.setText("Экзамен");
                examPointsView.setPadding(padding5inDp, padding2inDp, padding5inDp, padding2inDp);
                examPointsView.setGravity(Gravity.CENTER);
                examPointsView.setOnClickListener(view -> {
                    Bundle bundle = new Bundle();
                    bundle.putLong("subjectInfoId", subjectInfoId);
                    Navigation.findNavController(view).navigate(R.id.action_group_performance_to_dialog_subject_info_editing, bundle);
                });
                eventsRow.addView(examPointsView);
            }

            if (subjectInfo.isExam() || subjectInfo.isDifferentiatedCredit()) {
                TextView markView = new TextView(getContext());
                markView.setTextSize(20);
                markView.setText("Оценка");
                markView.setPadding(padding5inDp, padding2inDp, padding5inDp, padding2inDp);
                markView.setGravity(Gravity.CENTER);
                markView.setOnClickListener(view -> {
                    Bundle bundle = new Bundle();
                    bundle.putLong("subjectInfoId", subjectInfoId);
                    Navigation.findNavController(view).navigate(R.id.action_group_performance_to_dialog_subject_info_editing, bundle);
                });
                eventsRow.addView(markView);
            }
        }

        return eventsRow;
    }

    private TableRow generatePointsRow(int padding2inDp, int padding5inDp, Student student) {
        Map<String, List<Event>> events = groupPerformanceViewModel.getEvents().getValue();
        Map<String, Map<String, List<StudentEvent>>> studentsEvents = groupPerformanceViewModel.getStudentsEvents().getValue();

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
            bundle.putLong("studentId", student.getId());
            Navigation.findNavController(view).navigate(R.id.action_group_performance_to_student_profile, bundle);
        });
        pointsRow.addView(studentView);

        StudentPerformanceInSubject studentPerformanceInSubject = null;
        for (StudentPerformanceInSubject st : groupPerformanceViewModel.getStudentsPerformancesInSubject().getValue()) {
            if (st.getStudent().getId() == student.getId()) {
                studentPerformanceInSubject = st;
            }
        }

        List<Integer> modulesNumbers = Repository.getInstance().getModulesNumbers();
        for (int moduleNumber : modulesNumbers) {
            if (events.get(String.valueOf(moduleNumber)) != null) {
                for (Event event : events.get(String.valueOf(moduleNumber))) {
                    TextView pointsView = new TextView(getContext());
                    pointsView.setTextSize(20);
                    pointsView.setPadding(padding5inDp, padding2inDp, padding5inDp, padding2inDp);

                    StudentEvent studentEventChosen = null;
                    int lastAttempt = 0;
                    if (studentsEvents != null && studentsEvents.get(String.valueOf(moduleNumber)) != null &&
                            studentsEvents.get(String.valueOf(moduleNumber)).get(String.valueOf(student.getId())) != null) {
                        for (StudentEvent studentEvent : studentsEvents.get(String.valueOf(moduleNumber)).get(String.valueOf(student.getId()))) {
                            if (studentEvent.getStudentPerformanceInModule().getStudentPerformanceInSubject().getStudent().getId()
                                    == student.getId() && studentEvent.getEvent().getId() == event.getId()
                                    && studentEvent.getAttemptNumber() > lastAttempt) {
                                studentEventChosen = studentEvent;
                                lastAttempt = studentEvent.getAttemptNumber();
                            }
                        }
                    }
                    if (lastAttempt != 0) {
                        if (!studentEventChosen.isAttended()) {
                            pointsView.setText("Н");
                        } else {
                            if (studentEventChosen.getEarnedPoints() == null && studentEventChosen.getBonusPoints() == null) {
                                pointsView.setText("-");
                            } else if (studentEventChosen.getEarnedPoints() == null) {
                                pointsView.setText(String.valueOf(studentEventChosen.getBonusPoints()));
                                pointsView.setTextColor(studentEventChosen.isHaveCredit() ?
                                        getResources().getColor(R.color.green) : getResources().getColor(R.color.red));
                            } else if (studentEventChosen.getBonusPoints() == null) {
                                pointsView.setText(String.valueOf(studentEventChosen.getEarnedPoints()));
                                pointsView.setTextColor(studentEventChosen.isHaveCredit() ?
                                        getResources().getColor(R.color.green) : getResources().getColor(R.color.red));
                            } else {
                                pointsView.setText(String.valueOf(studentEventChosen.getEarnedPoints() + studentEventChosen.getBonusPoints()));
                                pointsView.setTextColor(studentEventChosen.isHaveCredit() ?
                                        getResources().getColor(R.color.green) : getResources().getColor(R.color.red));
                            }
                        }
                    } else
                        pointsView.setText("");
                    pointsView.setGravity(Gravity.CENTER);

                    int finalLastAttempt = lastAttempt;
                    Long finalStudentEventId;
                    if (studentEventChosen != null)
                        finalStudentEventId = studentEventChosen.getId();
                    else
                        finalStudentEventId = null;
                    Long finalStudentPerformanceInSubjectId = studentPerformanceInSubject.getId();
                    pointsView.setOnClickListener(view -> {
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("isFromGroupPerformance", true);
                        bundle.putInt("attemptNumber", finalLastAttempt);
                        if (finalStudentEventId != null)
                            bundle.putLong("studentEventId", finalStudentEventId);
                        bundle.putLong("eventId", event.getId());
                        bundle.putString("eventTitle", event.getTitle());
                        bundle.putLong("studentPerformanceInSubjectId", finalStudentPerformanceInSubjectId);
                        bundle.putLong("subjectInfoId", subjectInfoId);
                        Navigation.findNavController(view).navigate(R.id.action_group_performance_to_dialog_student_event, bundle);
                    });
                    pointsRow.addView(pointsView);
                }

                StudentPerformanceInModule studentPerformanceInModule = null;
                for (StudentPerformanceInModule st : groupPerformanceViewModel.getStudentsPerformancesInModules().getValue().get(String.valueOf(moduleNumber))) {
                    if (st.getStudentPerformanceInSubject().getStudent().getId() == student.getId()) {
                        studentPerformanceInModule = st;
                    }
                }

                TextView moduleView = new TextView(getContext());
                moduleView.setTextSize(20);
                if (studentPerformanceInModule.getEarnedPoints() == null) {
                    moduleView.setText("-");
                } else {
                    moduleView.setText(String.valueOf(studentPerformanceInModule.getEarnedPoints()));
                    moduleView.setTextColor(studentPerformanceInModule.isHaveCredit() ?
                            getResources().getColor(R.color.green) : getResources().getColor(R.color.red));
                }
                moduleView.setPadding(padding5inDp, padding2inDp, padding5inDp, padding2inDp);
                moduleView.setGravity(Gravity.CENTER);
                pointsRow.addView(moduleView);
            }
        }

        if ((events.get(String.valueOf(1)) != null && events.get(String.valueOf(2)) != null) ||
                (events.get(String.valueOf(2)) != null && events.get(String.valueOf(3)) != null) ||
                (events.get(String.valueOf(1)) != null && events.get(String.valueOf(3)) != null)) {
            TextView resultPointsView = new TextView(getContext());
            resultPointsView.setTextSize(20);
            if (studentPerformanceInSubject.getEarnedPoints() == null && studentPerformanceInSubject.getBonusPoints() == null) {
                resultPointsView.setText("-");
            } else if (studentPerformanceInSubject.getEarnedPoints() == null) {
                resultPointsView.setText(String.valueOf(studentPerformanceInSubject.getBonusPoints()));
                resultPointsView.setTextColor(studentPerformanceInSubject.isHaveCreditOrAdmission() ?
                        getResources().getColor(R.color.green) : getResources().getColor(R.color.red));
            } else if (studentPerformanceInSubject.getBonusPoints() == null) {
                resultPointsView.setText(String.valueOf(studentPerformanceInSubject.getEarnedPoints()));
                resultPointsView.setTextColor(studentPerformanceInSubject.isHaveCreditOrAdmission() ?
                        getResources().getColor(R.color.green) : getResources().getColor(R.color.red));
            } else {
                resultPointsView.setText(String.valueOf(studentPerformanceInSubject.getEarnedPoints() + studentPerformanceInSubject.getBonusPoints()));
                resultPointsView.setTextColor(studentPerformanceInSubject.isHaveCreditOrAdmission() ?
                        getResources().getColor(R.color.green) : getResources().getColor(R.color.red));
            }
            resultPointsView.setPadding(padding5inDp, padding2inDp, padding5inDp, padding2inDp);
            resultPointsView.setGravity(Gravity.CENTER);
            StudentPerformanceInSubject finalStudentPerformanceInSubject = studentPerformanceInSubject;
            resultPointsView.setOnClickListener(view -> {
                Bundle bundle = new Bundle();
                bundle.putLong("studentPerformanceInSubjectId", finalStudentPerformanceInSubject.getId());
                Navigation.findNavController(view).navigate(R.id.action_group_performance_to_dialog_student_performance_in_subject, bundle);
            });
            pointsRow.addView(resultPointsView);
        }

        SubjectInfo subjectInfo = groupPerformanceViewModel.getSubjectInfo().getValue();
        if (events.get(String.valueOf(1)) != null && events.get(String.valueOf(2)) != null &&
                events.get(String.valueOf(3)) != null) {
            if (subjectInfo.isExam()) {
                TextView examPointsView = new TextView(getContext());
                examPointsView.setTextSize(20);
                if (studentPerformanceInSubject.getEarnedExamPoints() == null) {
                    examPointsView.setText("");
                } else {
                    examPointsView.setText(String.valueOf(studentPerformanceInSubject.getEarnedExamPoints()));
                    examPointsView.setTextColor(studentPerformanceInSubject.getEarnedExamPoints() >= 18 ?
                            getResources().getColor(R.color.green) : getResources().getColor(R.color.red));
                }
                examPointsView.setPadding(padding5inDp, padding2inDp, padding5inDp, padding2inDp);
                examPointsView.setGravity(Gravity.CENTER);
                StudentPerformanceInSubject finalStudentPerformanceInSubject2 = studentPerformanceInSubject;
                examPointsView.setOnClickListener(view -> {
                    Bundle bundle = new Bundle();
                    bundle.putLong("studentPerformanceInSubjectId", finalStudentPerformanceInSubject2.getId());
                    Navigation.findNavController(view).navigate(R.id.action_group_performance_to_dialog_student_performance_in_subject, bundle);
                });
                pointsRow.addView(examPointsView);
            }

            if (subjectInfo.isExam() || subjectInfo.isDifferentiatedCredit()) {
                TextView markView = new TextView(getContext());
                markView.setTextSize(20);
                if (studentPerformanceInSubject.getMark() == null) {
                    markView.setText("");
                } else {
                    markView.setText(String.valueOf(studentPerformanceInSubject.getMark()));
                }
                markView.setPadding(padding5inDp, padding2inDp, padding5inDp, padding2inDp);
                markView.setGravity(Gravity.CENTER);
                StudentPerformanceInSubject finalStudentPerformanceInSubject1 = studentPerformanceInSubject;
                markView.setOnClickListener(view -> {
                    Bundle bundle = new Bundle();
                    bundle.putLong("studentPerformanceInSubjectId", finalStudentPerformanceInSubject1.getId());
                    Navigation.findNavController(view).navigate(R.id.action_group_performance_to_dialog_student_performance_in_subject, bundle);
                });
                pointsRow.addView(markView);
            }
        }

        return pointsRow;
    }
}