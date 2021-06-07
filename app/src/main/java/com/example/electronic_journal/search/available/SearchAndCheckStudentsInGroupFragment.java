package com.example.electronic_journal.search.available;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.electronic_journal.R;
import com.example.electronic_journal.Result;
import com.example.electronic_journal.data_classes.Event;
import com.example.electronic_journal.data_classes.Group;
import com.example.electronic_journal.data_classes.Lesson;
import com.example.electronic_journal.data_classes.StudentEvent;
import com.example.electronic_journal.data_classes.StudentPerformanceInModule;
import com.example.electronic_journal.search.StudentsAdapter;

import java.util.List;
import java.util.Map;

public class SearchAndCheckStudentsInGroupFragment extends Fragment {
    private StudentsAdapter studentsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search_students_in_group, container, false);

        SearchAndCheckStudentsInGroupViewModel searchAndCheckStudentsInGroupViewModel = new ViewModelProvider(this).get(SearchAndCheckStudentsInGroupViewModel.class);
        searchAndCheckStudentsInGroupViewModel.downloadStudentsInGroup(getArguments().getLong("fromGroupId"));

        int actionCode = getArguments().getInt("actionCode");

        final RecyclerView recyclerView = root.findViewById(R.id.searchedGroupStudentsList);
        searchAndCheckStudentsInGroupViewModel.getStudentsInGroup().observe(getViewLifecycleOwner(), studentsInGroup -> {
            if (studentsInGroup != null) {
                boolean[] checkedStudents = new boolean[studentsInGroup.size()];
                for (int i = 0; i < studentsInGroup.size(); i++) {
                    checkedStudents[i] = false;
                }

                SwitchCompat allStudentsSelectSwitch = root.findViewById(R.id.select_all_students);
                allStudentsSelectSwitch.setOnClickListener(view -> {
                    if (allStudentsSelectSwitch.isChecked()) {
                        for (int i = 0; i < studentsInGroup.size(); i++) {
                            checkedStudents[i] = true;
                        }
                        studentsAdapter.setColor(R.color.very_light_green);
                    } else {
                        for (int i = 0; i < studentsInGroup.size(); i++) {
                            checkedStudents[i] = false;
                        }
                        studentsAdapter.setColor(R.color.white);
                    }
                    studentsAdapter.notifyDataSetChanged();
                });

                Button suggestButton = root.findViewById(R.id.suggest_students);
                suggestButton.setOnClickListener(view -> {
                    if (actionCode == 3) {
                        searchAndCheckStudentsInGroupViewModel.changeGroupInSubjectsInfo(getArguments().getLong("fromGroupId"), getArguments().getLong("toGroupId"));
                        searchAndCheckStudentsInGroupViewModel.downloadGroup(getArguments().getLong("toGroupId"));
                        LiveData<Boolean> firstAnswerLiveData = searchAndCheckStudentsInGroupViewModel.getFirstAnswer();
                        LiveData<Group> groupLiveData = Transformations.switchMap(firstAnswerLiveData, firstAnswer -> searchAndCheckStudentsInGroupViewModel.getGroup());
                        LiveData<Result<Boolean>> secondAnswerLiveData = Transformations.switchMap(groupLiveData, group -> {
                            for (int i = 0; i < studentsInGroup.size(); i++) {
                                if (checkedStudents[i]) {
                                    searchAndCheckStudentsInGroupViewModel.changeStudentGroup(studentsInGroup.get(i), group);
                                }
                                if (i == studentsInGroup.size() - 1)
                                    return searchAndCheckStudentsInGroupViewModel.getSecondAnswer();
                            }

                            return null;
                        });

                        secondAnswerLiveData.observe(getViewLifecycleOwner(), answer -> {
                            if (answer != null) {
                                if (answer instanceof Result.Success) {
                                    Toast.makeText(getContext(), "Успешная смена группы у студентов", Toast.LENGTH_SHORT).show();

                                    Bundle bundle = new Bundle();
                                    bundle.putInt("openPage", 1);
                                    Navigation.findNavController(view).navigate(R.id.action_search_check_students_in_group_to_admin_actions, bundle);
                                }
                            }
                        });
                    } else if (actionCode == 4) {
                        searchAndCheckStudentsInGroupViewModel.downloadEvent(getArguments().getLong("eventId"));

                        LiveData<Event> eventLiveData = searchAndCheckStudentsInGroupViewModel.getEvent();
                        LiveData<Map<String, Map<String, List<StudentEvent>>>> studentsEventsLiveData = Transformations.switchMap(eventLiveData, event -> {
                            searchAndCheckStudentsInGroupViewModel.downloadStudentsEvents(event.getModule().getSubjectInfo().getId());
                            searchAndCheckStudentsInGroupViewModel.downloadStudentsPerformancesInModules(event.getModule().getSubjectInfo().getId());
                            return searchAndCheckStudentsInGroupViewModel.getStudentsEvents();
                        });
                        LiveData<Map<String, List<StudentPerformanceInModule>>> studentsModulesLiveData = Transformations.switchMap(studentsEventsLiveData, studentsEvents ->
                                searchAndCheckStudentsInGroupViewModel.getStudentsPerformancesInModules());

                        LiveData<Boolean> answerLiveData = Transformations.switchMap(studentsModulesLiveData, studentsModules -> {
                            Event event = searchAndCheckStudentsInGroupViewModel.getEvent().getValue();

                            for (int i = 0; i < studentsInGroup.size(); i++) {
                                int moduleNumber = event.getModule().getModuleNumber();
                                List<StudentPerformanceInModule> studentPerformanceInModules = studentsModules.get(String.valueOf(moduleNumber));
                                StudentPerformanceInModule studentPerformanceInModule = null;
                                for (int j = 0; j < studentPerformanceInModules.size(); j++) {
                                    if (studentsInGroup.get(i).getId() == studentPerformanceInModules.get(j).getStudentPerformanceInSubject().getStudent().getId()) {
                                        studentPerformanceInModule = studentPerformanceInModules.get(j);
                                        break;
                                    }
                                }
                                Map<String, Map<String, List<StudentEvent>>> studentsEvents = searchAndCheckStudentsInGroupViewModel.getStudentsEvents().getValue();

                                int lastAttempt = 0;
                                if (studentsEvents != null && studentsEvents.get(String.valueOf(moduleNumber)) != null &&
                                        studentsEvents.get(String.valueOf(moduleNumber)).get(String.valueOf(studentsInGroup.get(i).getId())) != null) {
                                    for (StudentEvent studentEvent : studentsEvents.get(String.valueOf(moduleNumber)).get(String.valueOf(studentsInGroup.get(i).getId()))) {
                                        if (studentEvent.getEvent().getId() == event.getId()
                                                && studentEvent.getAttemptNumber() > lastAttempt) {
                                            lastAttempt = studentEvent.getAttemptNumber();
                                        }
                                    }
                                }

                                searchAndCheckStudentsInGroupViewModel.addStudentEvent(lastAttempt + 1,
                                        studentPerformanceInModule, event, checkedStudents[i]);
                                if (i == studentsInGroup.size() - 1)
                                    return searchAndCheckStudentsInGroupViewModel.getFirstAnswer();
                            }

                            return null;
                        });

                        answerLiveData.observe(getViewLifecycleOwner(), answer -> {
                            if (answer != null) {
                                Toast.makeText(getContext(), "Успешный учет посещаемости", Toast.LENGTH_SHORT).show();

                                Bundle bundle = new Bundle();
                                bundle.putLong("subjectInfoId", searchAndCheckStudentsInGroupViewModel.getEvent().getValue().getModule().getSubjectInfo().getId());

                                Navigation.findNavController(view).navigate(R.id.action_search_check_students_in_group_to_group_performance_events, bundle);
                            }
                        });
                    } else if (actionCode == 5) {
                        searchAndCheckStudentsInGroupViewModel.downloadLesson(getArguments().getLong("lessonId"));

                        LiveData<Lesson> lessonLiveData = searchAndCheckStudentsInGroupViewModel.getLesson();
                        LiveData<Map<String, List<StudentPerformanceInModule>>> studentsModulesLiveData = Transformations.switchMap(lessonLiveData, lesson -> {
                            searchAndCheckStudentsInGroupViewModel.downloadStudentsPerformancesInModules(lesson.getModule().getSubjectInfo().getId());
                            return searchAndCheckStudentsInGroupViewModel.getStudentsPerformancesInModules();
                        });

                        LiveData<Boolean> answerLiveData = Transformations.switchMap(studentsModulesLiveData, studentsModules -> {
                            Lesson lesson = searchAndCheckStudentsInGroupViewModel.getLesson().getValue();

                            for (int i = 0; i < studentsInGroup.size(); i++) {
                                int moduleNumber = lesson.getModule().getModuleNumber();
                                List<StudentPerformanceInModule> studentPerformanceInModules = studentsModules.get(String.valueOf(moduleNumber));
                                StudentPerformanceInModule studentPerformanceInModule = null;
                                for (int j = 0; j < studentPerformanceInModules.size(); j++) {
                                    if (studentsInGroup.get(i).getId() == studentPerformanceInModules.get(j).getStudentPerformanceInSubject().getStudent().getId()) {
                                        studentPerformanceInModule = studentPerformanceInModules.get(j);
                                        break;
                                    }
                                }

                                searchAndCheckStudentsInGroupViewModel.addStudentLesson(studentPerformanceInModule, lesson, checkedStudents[i]);
                                if (i == studentsInGroup.size() - 1)
                                    return searchAndCheckStudentsInGroupViewModel.getFirstAnswer();
                            }

                            return null;
                        });

                        answerLiveData.observe(getViewLifecycleOwner(), answer -> {
                            if (answer != null) {
                                Toast.makeText(getContext(), "Успешный учет посещаемости", Toast.LENGTH_SHORT).show();

                                Bundle bundle = new Bundle();
                                bundle.putInt("openPage", searchAndCheckStudentsInGroupViewModel.getLesson().getValue().getModule().getModuleNumber() - 1);
                                bundle.putLong("subjectInfoId", searchAndCheckStudentsInGroupViewModel.getLesson().getValue().getModule().getSubjectInfo().getId());

                                Navigation.findNavController(view).navigate(R.id.action_search_check_students_in_group_to_group_performance_lessons, bundle);
                            }
                        });
                    }
                });

                View.OnClickListener onItemClickListener = view -> {
                    RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
                    int position = viewHolder.getAdapterPosition();

                    for (int i = 0; i < studentsInGroup.size(); i++) {
                        if (studentsAdapter.getStudents().get(position).getId() == studentsInGroup.get(i).getId()) {
                            checkedStudents[i] = !checkedStudents[i];
                            if (checkedStudents[i])
                                view.setBackgroundColor(getResources().getColor(R.color.very_light_green));
                            else
                                view.setBackgroundColor(getResources().getColor(R.color.white));
                            break;
                        }
                    }
                };

                studentsAdapter = new StudentsAdapter(getContext(), studentsInGroup, onItemClickListener);
                recyclerView.setAdapter(studentsAdapter);
                recyclerView.setHasFixedSize(false);
            }
        });

        return root;
    }
}
