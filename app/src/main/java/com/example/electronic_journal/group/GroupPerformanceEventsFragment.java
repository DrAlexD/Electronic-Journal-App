package com.example.electronic_journal.group;

import android.content.SharedPreferences;
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
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.electronic_journal.R;
import com.example.electronic_journal.Repository;
import com.example.electronic_journal.data_classes.Event;
import com.example.electronic_journal.data_classes.Module;
import com.example.electronic_journal.data_classes.Student;
import com.example.electronic_journal.data_classes.StudentEvent;
import com.example.electronic_journal.data_classes.StudentPerformanceInModule;
import com.example.electronic_journal.data_classes.StudentPerformanceInSubject;
import com.example.electronic_journal.data_classes.SubjectInfo;

import java.util.List;
import java.util.Map;

public class GroupPerformanceEventsFragment extends Fragment {
    private long subjectInfoId;

    private GroupPerformanceEventsViewModel groupPerformanceEventsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_group_performance_events, container, false);

        subjectInfoId = getArguments().getLong("subjectInfoId");

        groupPerformanceEventsViewModel = new ViewModelProvider(this).get(GroupPerformanceEventsViewModel.class);
        groupPerformanceEventsViewModel.downloadEntities(subjectInfoId);
        LiveData<SubjectInfo> subjectInfoLiveData = groupPerformanceEventsViewModel.getSubjectInfo();
        LiveData<List<Student>> studentsInGroupLiveData = Transformations.switchMap(subjectInfoLiveData, s -> {
            groupPerformanceEventsViewModel.downloadStudentsInGroup(s.getGroup().getId());
            return groupPerformanceEventsViewModel.getStudentsInGroup();
        });
        LiveData<Map<String, Module>> modulesLiveData = Transformations.switchMap(studentsInGroupLiveData,
                s -> groupPerformanceEventsViewModel.getModules());
        LiveData<Map<String, List<StudentPerformanceInModule>>> studentsPerformancesInModulesLiveData = Transformations.switchMap(modulesLiveData, m ->
                groupPerformanceEventsViewModel.getStudentsPerformancesInModules());

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean isProfessorRules = sharedPreferences.getBoolean(getString(R.string.is_professor_rules), false);

        Button addEventButton = root.findViewById(R.id.addEventButton);
        addEventButton.setVisibility(isProfessorRules ? View.VISIBLE : View.GONE);
        addEventButton.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putLong("subjectInfoId", subjectInfoId);
            Navigation.findNavController(view).navigate(R.id.action_group_performance_to_dialog_event_adding, bundle);
        });

        groupPerformanceEventsViewModel.downloadEvents(subjectInfoId);
        LiveData<List<StudentPerformanceInSubject>> studentsPerformancesInSubjectLiveData = Transformations.switchMap(studentsPerformancesInModulesLiveData, s -> {
            Button openGroupPerformanceLessonsButton = root.findViewById(R.id.openGroupPerformanceLessons);
            openGroupPerformanceLessonsButton.setOnClickListener(view -> {
                Bundle bundle = new Bundle();
                bundle.putLong("subjectInfoId", subjectInfoId);
                Navigation.findNavController(view).navigate(R.id.action_group_performance_events_to_group_performance_lessons, bundle);
            });
            return groupPerformanceEventsViewModel.getStudentsPerformancesInSubject();
        });
        LiveData<Map<String, List<Event>>> eventsLiveData = Transformations.switchMap(studentsPerformancesInSubjectLiveData,
                s -> groupPerformanceEventsViewModel.getEvents());
        LiveData<Map<String, Map<String, List<StudentEvent>>>> studentsEventsLiveData = Transformations.switchMap(eventsLiveData,
                e -> groupPerformanceEventsViewModel.getStudentsEvents());

        studentsEventsLiveData.observe(getViewLifecycleOwner(), studentsEvents -> {
            if (groupPerformanceEventsViewModel.getEvents().getValue() != null) {
                generateEventsTable(root);
            }
        });

        return root;
    }

    private void generateEventsTable(View root) {
        TableLayout studentsInModuleEventsTable = root.findViewById(R.id.studentsInModuleEventsTable);
        studentsInModuleEventsTable.removeAllViews();
        int padding2inDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());
        int padding5inDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SwitchCompat showEventsSwitch = root.findViewById(R.id.show_events);
        showEventsSwitch.setChecked(!sharedPreferences.getBoolean(getString(R.string.show_events_switch), true));

        TableRow eventsRow = generateEventsRow(padding2inDp, padding5inDp);
        studentsInModuleEventsTable.addView(eventsRow);
        List<Student> students = groupPerformanceEventsViewModel.getStudentsInGroup().getValue();

        int positionInList = 1;
        if (students != null) {
            for (Student student : students) {
                TableRow pointsRow = generatePointsRow(padding2inDp, padding5inDp, student, positionInList);
                if (pointsRow != null)
                    studentsInModuleEventsTable.addView(pointsRow);
                positionInList++;
            }
        }

        showEventsSwitch.setOnClickListener(view -> {
            sharedPreferences.edit().putBoolean(getString(R.string.show_events_switch), !showEventsSwitch.isChecked()).apply();
            collapseEventColumns(studentsInModuleEventsTable, !showEventsSwitch.isChecked());
        });

        if (sharedPreferences.getBoolean(getString(R.string.show_events_switch), true)) {
            collapseEventColumns(studentsInModuleEventsTable, sharedPreferences.getBoolean(getString(R.string.show_events_switch), true));
        }
    }


    private void collapseEventColumns(TableLayout studentsInModuleEventsTable, boolean isCollapsed) {
        List<Integer> modulesNumbers = Repository.getInstance().getModulesNumbers();
        Map<String, List<Event>> events = groupPerformanceEventsViewModel.getEvents().getValue();

        int j = 1;
        for (Integer moduleNumber : modulesNumbers) {
            if (events.get(String.valueOf(moduleNumber)) != null) {
                for (int i = 0; i < events.get(String.valueOf(moduleNumber)).size(); i++) {
                    studentsInModuleEventsTable.setColumnCollapsed(i + j, isCollapsed);
                }
                j += events.get(String.valueOf(moduleNumber)).size() + 1;
            }
        }
    }

    private TableRow generateEventsRow(int padding2inDp, int padding5inDp) {
        Map<String, List<Event>> events = groupPerformanceEventsViewModel.getEvents().getValue();
        TableRow eventsRow = new TableRow(getContext());
        eventsRow.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE |
                LinearLayout.SHOW_DIVIDER_BEGINNING | LinearLayout.SHOW_DIVIDER_END);
        eventsRow.setDividerDrawable(getResources().getDrawable(R.drawable.divider));

        TextView infoView = new TextView(getContext());
        infoView.setTextSize(20);
        //infoView.setTextColor(getResources().getColor(R.color.black));
        infoView.setText("");
        infoView.setPadding(padding5inDp, padding2inDp, padding5inDp, padding2inDp);
        infoView.setGravity(Gravity.CENTER);
        eventsRow.addView(infoView);

        List<Integer> modulesNumbers = Repository.getInstance().getModulesNumbers();
        Map<String, Module> modules = groupPerformanceEventsViewModel.getModules().getValue();

        for (Integer moduleNumber : modulesNumbers) {
            if (events.get(String.valueOf(moduleNumber)) != null) {
                for (Event event : events.get(String.valueOf(moduleNumber))) {
                    TextView eventView = new TextView(getContext());
                    eventView.setTextSize(20);
                    //eventView.setTextColor(getResources().getColor(R.color.black));
                    eventView.setText(event.getTitle());
                    eventView.setPadding(padding5inDp, padding2inDp, padding5inDp, padding2inDp);
                    eventView.setGravity(Gravity.CENTER);
                    eventView.setOnClickListener(view -> {
                        Bundle bundle = new Bundle();
                        bundle.putLong("eventId", event.getId());
                        bundle.putLong("groupId", groupPerformanceEventsViewModel.getSubjectInfo().getValue().getGroup().getId());
                        bundle.putString("eventTitle", event.getTitle());
                        Navigation.findNavController(view).navigate(R.id.action_group_performance_to_dialog_event_editing, bundle);
                    });
                    eventsRow.addView(eventView);
                }

                TextView moduleView = new TextView(getContext());
                moduleView.setTextSize(20);
                //moduleView.setTextColor(getResources().getColor(R.color.black));
                moduleView.setText("Модуль " + moduleNumber);
                moduleView.setPadding(padding5inDp, padding2inDp, padding5inDp, padding2inDp);
                moduleView.setGravity(Gravity.CENTER);
                moduleView.setOnClickListener(view -> {
                    Bundle bundle = new Bundle();
                    bundle.putLong("moduleId", modules.get(String.valueOf(moduleNumber)).getId());
                    bundle.putInt("moduleNumber", moduleNumber);
                    bundle.putBoolean("isFromEventsPerformance", true);
                    Navigation.findNavController(view).navigate(R.id.action_group_performance_events_to_dialog_module_info_editing, bundle);
                });
                eventsRow.addView(moduleView);
            }
        }

        if ((events.get(String.valueOf(1)) != null && events.get(String.valueOf(2)) != null) ||
                (events.get(String.valueOf(2)) != null && events.get(String.valueOf(3)) != null) ||
                (events.get(String.valueOf(1)) != null && events.get(String.valueOf(3)) != null)) {
            TextView resultPointsView = new TextView(getContext());
            resultPointsView.setTextSize(20);
            //resultPointsView.setTextColor(getResources().getColor(R.color.black));
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
            SubjectInfo subjectInfo = groupPerformanceEventsViewModel.getSubjectInfo().getValue();
            if (subjectInfo.isExam()) {
                TextView examPointsView = new TextView(getContext());
                examPointsView.setTextSize(20);
                //examPointsView.setTextColor(getResources().getColor(R.color.black));
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
                //markView.setTextColor(getResources().getColor(R.color.black));
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

    private TableRow generatePointsRow(int padding2inDp, int padding5inDp, Student student, int positionInList) {
        Map<String, List<Event>> events = groupPerformanceEventsViewModel.getEvents().getValue();
        Map<String, Map<String, List<StudentEvent>>> studentsEvents = groupPerformanceEventsViewModel.getStudentsEvents().getValue();

        TableRow pointsRow = new TableRow(getContext());
        pointsRow.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE |
                LinearLayout.SHOW_DIVIDER_BEGINNING | LinearLayout.SHOW_DIVIDER_END);
        pointsRow.setDividerDrawable(getResources().getDrawable(R.drawable.divider));

        TextView studentView = new TextView(getContext());
        studentView.setText(student.getFirstName().charAt(0) + ". " + student.getSecondName());
        //studentView.setTextColor(getResources().getColor(R.color.black));
        studentView.setTextSize(20);
        studentView.setPadding(padding5inDp, padding2inDp, padding5inDp, padding2inDp);
        studentView.setGravity(Gravity.CENTER);
        studentView.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putLong("studentId", student.getId());
            Navigation.findNavController(view).navigate(R.id.action_group_performance_events_to_student_profile, bundle);
        });
        pointsRow.addView(studentView);

        StudentPerformanceInSubject studentPerformanceInSubject = null;
        for (StudentPerformanceInSubject st : groupPerformanceEventsViewModel.getStudentsPerformancesInSubject().getValue()) {
            if (st.getStudent().getId() == student.getId()) {
                studentPerformanceInSubject = st;
            }
        }

        if (studentPerformanceInSubject != null) {
            List<Integer> modulesNumbers = Repository.getInstance().getModulesNumbers();
            for (int moduleNumber : modulesNumbers) {
                if (events.get(String.valueOf(moduleNumber)) != null) {
                    for (Event event : events.get(String.valueOf(moduleNumber))) {
                        TextView pointsView = new TextView(getContext());
                        pointsView.setTextSize(20);
                        //pointsView.setTextColor(getResources().getColor(R.color.black));
                        pointsView.setPadding(padding5inDp, padding2inDp, padding5inDp, padding2inDp);

                        StudentEvent studentEventChosen = null;
                        int lastAttempt = 0;
                        if (studentsEvents != null && studentsEvents.get(String.valueOf(moduleNumber)) != null &&
                                studentsEvents.get(String.valueOf(moduleNumber)).get(String.valueOf(student.getId())) != null) {
                            for (StudentEvent studentEvent : studentsEvents.get(String.valueOf(moduleNumber)).get(String.valueOf(student.getId()))) {
                                if (studentEvent.getEvent().getId() == event.getId()
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
                                    if (studentEventChosen.isHaveCredit() != null) {
                                        pointsView.setTextColor(studentEventChosen.isHaveCredit() ?
                                                getResources().getColor(R.color.green) : getResources().getColor(R.color.red));
                                    }
                                } else if (studentEventChosen.getBonusPoints() == null) {
                                    pointsView.setText(String.valueOf(studentEventChosen.getEarnedPoints()));
                                    if (studentEventChosen.isHaveCredit() != null) {
                                        pointsView.setTextColor(studentEventChosen.isHaveCredit() ?
                                                getResources().getColor(R.color.green) : getResources().getColor(R.color.red));
                                    }
                                } else {
                                    pointsView.setText(String.valueOf(studentEventChosen.getEarnedPoints() + studentEventChosen.getBonusPoints()));
                                    if (studentEventChosen.isHaveCredit() != null) {
                                        pointsView.setTextColor(studentEventChosen.isHaveCredit() ?
                                                getResources().getColor(R.color.green) : getResources().getColor(R.color.red));
                                    }
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
                            bundle.putBoolean("isHasData", finalLastAttempt != 0);
                            if (finalStudentEventId != null)
                                bundle.putLong("studentEventId", finalStudentEventId);
                            bundle.putLong("eventId", event.getId());
                            bundle.putString("eventTitle", event.getTitle());
                            int numberOfStudents = groupPerformanceEventsViewModel.getStudentsInGroup().getValue().size();
                            if (event.getNumberOfVariants() != null) {
                                if (event.getNumberOfVariants() > numberOfStudents)
                                    bundle.putInt("variantNumber", positionInList);
                                else {
                                    if (positionInList % event.getNumberOfVariants() != 0)
                                        bundle.putInt("variantNumber", positionInList % event.getNumberOfVariants());
                                    else
                                        bundle.putInt("variantNumber", event.getNumberOfVariants());
                                }
                            } else
                                bundle.putInt("variantNumber", -1);
                            bundle.putInt("eventMaxPoints", event.getMaxPoints());
                            bundle.putInt("eventType", event.getTypeNumber());
                            bundle.putLong("studentPerformanceInSubjectId", finalStudentPerformanceInSubjectId);
                            bundle.putLong("subjectInfoId", subjectInfoId);
                            Navigation.findNavController(view).navigate(R.id.action_group_performance_to_dialog_student_event, bundle);
                        });
                        pointsRow.addView(pointsView);
                    }

                    StudentPerformanceInModule studentPerformanceInModule = null;
                    for (StudentPerformanceInModule st : groupPerformanceEventsViewModel.getStudentsPerformancesInModules().getValue().get(String.valueOf(moduleNumber))) {
                        if (st.getStudentPerformanceInSubject().getStudent().getId() == student.getId()) {
                            studentPerformanceInModule = st;
                        }
                    }

                    TextView moduleView = new TextView(getContext());
                    //moduleView.setTextColor(getResources().getColor(R.color.black));
                    moduleView.setTextSize(20);
                    if (studentPerformanceInModule.getEarnedPoints() == null) {
                        moduleView.setText("-");
                    } else {
                        moduleView.setText(String.valueOf(studentPerformanceInModule.getEarnedPoints()));
                        if (studentPerformanceInModule.isHaveCredit() != null) {
                            moduleView.setTextColor(studentPerformanceInModule.isHaveCredit() ?
                                    getResources().getColor(R.color.green) : getResources().getColor(R.color.red));
                        }
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
                //resultPointsView.setTextColor(getResources().getColor(R.color.black));
                if (studentPerformanceInSubject.getEarnedPoints() == null && studentPerformanceInSubject.getBonusPoints() == null) {
                    resultPointsView.setText("-");
                } else if (studentPerformanceInSubject.getEarnedPoints() == null) {
                    resultPointsView.setText(String.valueOf(studentPerformanceInSubject.getBonusPoints()));
                    if (studentPerformanceInSubject.isHaveCreditOrAdmission() != null) {
                        resultPointsView.setTextColor(studentPerformanceInSubject.isHaveCreditOrAdmission() ?
                                getResources().getColor(R.color.green) : getResources().getColor(R.color.red));
                    }
                } else if (studentPerformanceInSubject.getBonusPoints() == null) {
                    resultPointsView.setText(String.valueOf(studentPerformanceInSubject.getEarnedPoints()));
                    if (studentPerformanceInSubject.isHaveCreditOrAdmission() != null) {
                        resultPointsView.setTextColor(studentPerformanceInSubject.isHaveCreditOrAdmission() ?
                                getResources().getColor(R.color.green) : getResources().getColor(R.color.red));
                    }
                } else {
                    resultPointsView.setText(String.valueOf(studentPerformanceInSubject.getEarnedPoints() + studentPerformanceInSubject.getBonusPoints()));
                    if (studentPerformanceInSubject.isHaveCreditOrAdmission() != null) {
                        resultPointsView.setTextColor(studentPerformanceInSubject.isHaveCreditOrAdmission() ?
                                getResources().getColor(R.color.green) : getResources().getColor(R.color.red));
                    }
                }
                resultPointsView.setPadding(padding5inDp, padding2inDp, padding5inDp, padding2inDp);
                resultPointsView.setGravity(Gravity.CENTER);
                StudentPerformanceInSubject finalStudentPerformanceInSubject = studentPerformanceInSubject;
                resultPointsView.setOnClickListener(view -> {
                    Bundle bundle = new Bundle();
                    bundle.putLong("studentPerformanceInSubjectId", finalStudentPerformanceInSubject.getId());
                    bundle.putBoolean("isFromEventsPerformance", true);
                    Navigation.findNavController(view).navigate(R.id.action_group_performance_to_dialog_student_performance_in_subject, bundle);
                });
                pointsRow.addView(resultPointsView);
            }

            SubjectInfo subjectInfo = groupPerformanceEventsViewModel.getSubjectInfo().getValue();
            if (events.get(String.valueOf(1)) != null && events.get(String.valueOf(2)) != null &&
                    events.get(String.valueOf(3)) != null) {
                if (subjectInfo.isExam()) {
                    TextView examPointsView = new TextView(getContext());
                    examPointsView.setTextSize(20);
                    //examPointsView.setTextColor(getResources().getColor(R.color.black));
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
                        bundle.putBoolean("isFromEventsPerformance", true);
                        Navigation.findNavController(view).navigate(R.id.action_group_performance_to_dialog_student_performance_in_subject, bundle);
                    });
                    pointsRow.addView(examPointsView);
                }

                if (subjectInfo.isExam() || subjectInfo.isDifferentiatedCredit()) {
                    TextView markView = new TextView(getContext());
                    markView.setTextSize(20);
                    //markView.setTextColor(getResources().getColor(R.color.black));
                    if (studentPerformanceInSubject.getMark() == null) {
                        markView.setText("");
                    } else {
                        markView.setText(String.valueOf(studentPerformanceInSubject.getMark()));
                        if (studentPerformanceInSubject.getMark() == 2) {
                            markView.setTextColor(getResources().getColor(R.color.red));
                        } else if (studentPerformanceInSubject.getMark() == 3) {
                            markView.setTextColor(getResources().getColor(R.color.yellow));
                        } else if (studentPerformanceInSubject.getMark() == 4) {
                            markView.setTextColor(getResources().getColor(R.color.light_green));
                        } else if (studentPerformanceInSubject.getMark() == 5) {
                            markView.setTextColor(getResources().getColor(R.color.green));
                        }
                    }
                    markView.setPadding(padding5inDp, padding2inDp, padding5inDp, padding2inDp);
                    markView.setGravity(Gravity.CENTER);
                    StudentPerformanceInSubject finalStudentPerformanceInSubject1 = studentPerformanceInSubject;
                    markView.setOnClickListener(view -> {
                        Bundle bundle = new Bundle();
                        bundle.putLong("studentPerformanceInSubjectId", finalStudentPerformanceInSubject1.getId());
                        bundle.putBoolean("isFromEventsPerformance", true);
                        Navigation.findNavController(view).navigate(R.id.action_group_performance_to_dialog_student_performance_in_subject, bundle);
                    });
                    pointsRow.addView(markView);
                }
            }
            return pointsRow;
        }
        return null;
    }
}