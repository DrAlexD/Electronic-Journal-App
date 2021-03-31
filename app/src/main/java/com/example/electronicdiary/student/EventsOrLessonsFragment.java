package com.example.electronicdiary.student;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.electronicdiary.R;
import com.example.electronicdiary.Repository;

import java.util.ArrayList;

public class EventsOrLessonsFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_events_or_lessons, container, false);

        int position = getArguments().getInt("position");
        ArrayList<Integer> modules = Repository.getInstance().getModules();

        StudentPerformanceViewModel studentPerformanceViewModel = new ViewModelProvider(getParentFragment()).get(StudentPerformanceViewModel.class);

        final RecyclerView recyclerView = root.findViewById(R.id.student_events_list);
        if (position == 1) {
            View.OnClickListener onItemClickListener = view -> {
                RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
                int pos = viewHolder.getAdapterPosition();

                Bundle bundle = new Bundle();
                bundle.putInt("studentId", studentPerformanceViewModel.getStudent().getValue().getId());
                bundle.putInt("eventId", studentPerformanceViewModel.getEvents().getValue().get(pos).getId());
                Navigation.findNavController(view).navigate(R.id.action_student_performance_to_dialog_student_event, bundle);
            };

            studentPerformanceViewModel.getEvents().observe(getViewLifecycleOwner(), events -> {
                if (events == null) {
                    return;
                }

                EventsAdapter eventsAdapter = new EventsAdapter(getContext(), events,
                        studentPerformanceViewModel.getStudentEvents().getValue(), onItemClickListener);
                recyclerView.setAdapter(eventsAdapter);
                recyclerView.setHasFixedSize(false);
            });

            studentPerformanceViewModel.getStudentEvents().observe(getViewLifecycleOwner(), studentEvents -> {
                if (studentEvents == null) {
                    return;
                }

                EventsAdapter eventsAdapter = new EventsAdapter(getContext(),
                        studentPerformanceViewModel.getEvents().getValue(), studentEvents, onItemClickListener);
                recyclerView.setAdapter(eventsAdapter);
                recyclerView.setHasFixedSize(false);
            });
        } else if (position == 2) {
            recyclerView.setVisibility(View.GONE);

            ExpandableListView.OnChildClickListener onLectureClickListener = (parent, v, groupPosition, childPosition, id) -> {
                Bundle bundle = new Bundle();
                bundle.putInt("moduleNumber", modules.get(groupPosition));
                bundle.putInt("studentId", studentPerformanceViewModel.getStudent().getValue().getId());
                bundle.putInt("lessonId", studentPerformanceViewModel.getLecturesByModules().
                        getValue().get(modules.get(groupPosition)).get(childPosition).getId());
                Navigation.findNavController(v).navigate(R.id.action_student_performance_to_dialog_student_lesson, bundle);
                return true;
            };

            //TODO можно ли создать expandableList с двойной вложенностью?
            final ExpandableListView expandableListView = root.findViewById(R.id.lessons_by_modules);
            studentPerformanceViewModel.getLecturesByModules().observe(getViewLifecycleOwner(),
                    lecturesByModules -> {
                        if (lecturesByModules == null) {
                            return;
                        }

                        LessonsAdapter lessonsAdapter = new LessonsAdapter(getContext(), modules,
                                lecturesByModules, studentPerformanceViewModel.getStudentLessonsByModules().getValue());
                        expandableListView.setVisibility(View.VISIBLE);
                        expandableListView.setAdapter(lessonsAdapter);
                        expandableListView.setOnChildClickListener(onLectureClickListener);
                    });

            studentPerformanceViewModel.getStudentLessonsByModules().observe(getViewLifecycleOwner(),
                    studentLessonsByModules -> {
                        if (studentLessonsByModules == null) {
                            return;
                        }

                        LessonsAdapter lessonsAdapter = new LessonsAdapter(getContext(), modules,
                                studentPerformanceViewModel.getLecturesByModules().getValue(), studentLessonsByModules);
                        expandableListView.setVisibility(View.VISIBLE);
                        expandableListView.setAdapter(lessonsAdapter);
                        expandableListView.setOnChildClickListener(onLectureClickListener);
                    });
        } else if (position == 3) {
            recyclerView.setVisibility(View.GONE);

            ExpandableListView.OnChildClickListener onSeminarClickListener = (parent, v, groupPosition, childPosition, id) -> {
                Bundle bundle = new Bundle();
                bundle.putInt("moduleNumber", modules.get(groupPosition));
                bundle.putInt("studentId", studentPerformanceViewModel.getStudent().getValue().getId());
                bundle.putInt("lessonId", studentPerformanceViewModel.getSeminarsByModules().
                        getValue().get(modules.get(groupPosition)).get(childPosition).getId());
                Navigation.findNavController(v).navigate(R.id.action_student_performance_to_dialog_student_lesson, bundle);
                return true;
            };

            final ExpandableListView expandableListView = root.findViewById(R.id.lessons_by_modules);
            studentPerformanceViewModel.getSeminarsByModules().observe(getViewLifecycleOwner(),
                    seminarsByModules -> {
                        if (seminarsByModules == null) {
                            return;
                        }

                        LessonsAdapter lessonsAdapter = new LessonsAdapter(getContext(), modules,
                                seminarsByModules, studentPerformanceViewModel.getStudentLessonsByModules().getValue());
                        expandableListView.setVisibility(View.VISIBLE);
                        expandableListView.setAdapter(lessonsAdapter);
                        expandableListView.setOnChildClickListener(onSeminarClickListener);
                    });

            studentPerformanceViewModel.getStudentLessonsByModules().observe(getViewLifecycleOwner(),
                    studentLessonsByModules -> {
                        if (studentLessonsByModules == null) {
                            return;
                        }

                        LessonsAdapter lessonsAdapter = new LessonsAdapter(getContext(), modules,
                                studentPerformanceViewModel.getSeminarsByModules().getValue(), studentLessonsByModules);
                        expandableListView.setVisibility(View.VISIBLE);
                        expandableListView.setAdapter(lessonsAdapter);
                        expandableListView.setOnChildClickListener(onSeminarClickListener);
                    });
        }

        return root;
    }
}