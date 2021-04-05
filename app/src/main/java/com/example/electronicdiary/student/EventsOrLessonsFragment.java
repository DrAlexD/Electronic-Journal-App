package com.example.electronicdiary.student;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.electronicdiary.Event;
import com.example.electronicdiary.Lesson;
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

        //TODO можно ли создать expandableList с двойной вложенностью?
        final ExpandableListView expandableListView = root.findViewById(R.id.events_or_lessons_list);
        if (position == 1) {

            ExpandableListView.OnChildClickListener onEventClickListener = (parent, v, groupPosition, childPosition, id) -> {
                Bundle bundle = new Bundle();
                bundle.putBoolean("isFromGroupPerformance", false);
                Event event = studentPerformanceViewModel.getEvents().getValue().get(modules.get(groupPosition)).get(childPosition);
                bundle.putInt("eventMinPoints", event.getMinPoints());
                bundle.putString("eventDeadlineDate", String.valueOf(event.getDeadlineDate().getDate()));
                bundle.putString("eventTitle", event.getTitle());
                TextView attemptNumber = v.findViewById(R.id.attemptNumber);
                bundle.putInt("attemptNumber", Integer.parseInt(attemptNumber.getText().toString()));
                bundle.putInt("eventId", event.getId());
                bundle.putInt("studentId", studentPerformanceViewModel.getStudent().getValue().getId());
                bundle.putInt("moduleNumber", modules.get(groupPosition));
                bundle.putInt("groupId", studentPerformanceViewModel.getStudent().getValue().getGroupId());
                bundle.putInt("subjectId", studentPerformanceViewModel.getSubjectInfo().getValue().getSubjectId());
                bundle.putInt("lecturerId", studentPerformanceViewModel.getSubjectInfo().getValue().getLecturerId());
                bundle.putInt("seminarianId", studentPerformanceViewModel.getSubjectInfo().getValue().getSeminarianId());
                bundle.putInt("semesterId", studentPerformanceViewModel.getSubjectInfo().getValue().getSemesterId());
                Navigation.findNavController(v).navigate(R.id.action_student_performance_to_dialog_student_event, bundle);
                return true;
            };

            studentPerformanceViewModel.getStudentPerformanceInSubject().observe(getViewLifecycleOwner(), studentPerformanceInSubject -> {
                if (studentPerformanceInSubject == null) {
                    return;
                }

                TextView earnedPoints = root.findViewById(R.id.earnedPoints);
                TextView bonusPoints = root.findViewById(R.id.bonusPoints);
                TextView mark = root.findViewById(R.id.mark);
                TextView earnedExamPoints = root.findViewById(R.id.earnedExamPoints);

                earnedPoints.setText(String.valueOf(studentPerformanceInSubject.getEarnedPoints()));
                bonusPoints.setText(String.valueOf(studentPerformanceInSubject.getBonusPoints()));
                earnedPoints.setTextColor(getResources().getColor(studentPerformanceInSubject.isHaveCreditOrAdmission() ?
                        R.color.red : R.color.green));
                bonusPoints.setTextColor(getResources().getColor(studentPerformanceInSubject.isHaveCreditOrAdmission() ?
                        R.color.red : R.color.green));
                mark.setVisibility(studentPerformanceViewModel.getSubjectInfo().getValue().isExam() ||
                        studentPerformanceViewModel.getSubjectInfo().getValue().isDifferentiatedCredit() ? View.VISIBLE : View.INVISIBLE);
                mark.setText(studentPerformanceInSubject.getMark());
                earnedExamPoints.setVisibility(studentPerformanceViewModel.getSubjectInfo().getValue().isExam() ? View.VISIBLE : View.INVISIBLE);
                earnedExamPoints.setText(studentPerformanceInSubject.getEarnedExamPoints());

            });

            studentPerformanceViewModel.getEvents().observe(getViewLifecycleOwner(), events -> {
                if (events == null) {
                    return;
                }

                EventsAdapter eventsAdapter = new EventsAdapter(getContext(), modules,
                        studentPerformanceViewModel.getModuleInfo().getValue(),
                        studentPerformanceViewModel.getStudentPerformanceInModules().getValue(),
                        events, studentPerformanceViewModel.getStudentEvents().getValue());
                expandableListView.setAdapter(eventsAdapter);
                expandableListView.setOnChildClickListener(onEventClickListener);
            });

            studentPerformanceViewModel.getStudentEvents().observe(getViewLifecycleOwner(), studentEvents -> {
                if (studentEvents == null) {
                    return;
                }

                EventsAdapter eventsAdapter = new EventsAdapter(getContext(), modules,
                        studentPerformanceViewModel.getModuleInfo().getValue(),
                        studentPerformanceViewModel.getStudentPerformanceInModules().getValue(),
                        studentPerformanceViewModel.getEvents().getValue(), studentEvents);
                expandableListView.setAdapter(eventsAdapter);
                expandableListView.setOnChildClickListener(onEventClickListener);
            });
        } else if (position == 2) {
            ExpandableListView.OnChildClickListener onLectureClickListener = (parent, v, groupPosition, childPosition, id) -> {
                Bundle bundle = new Bundle();
                bundle.putBoolean("isFromGroupPerformance", false);
                bundle.putBoolean("isLecture", true);
                TextView bonusPoints = v.findViewById(R.id.bonusPoints);
                bundle.putBoolean("isHasData", !"Нет данных".equals(bonusPoints.getText().toString()));
                Lesson lesson = studentPerformanceViewModel.getLecturesByModules().getValue().
                        get(modules.get(groupPosition)).get(childPosition);
                bundle.putString("lessonDate", lesson.getDateAndTime().getDate() + " " + lesson.getDateAndTime().getHours() +
                        ":" + lesson.getDateAndTime().getMinutes());
                bundle.putInt("lessonId", lesson.getId());
                bundle.putInt("studentId", studentPerformanceViewModel.getStudent().getValue().getId());
                bundle.putInt("moduleNumber", modules.get(groupPosition));
                bundle.putInt("groupId", studentPerformanceViewModel.getStudent().getValue().getGroupId());
                bundle.putInt("subjectId", studentPerformanceViewModel.getSubjectInfo().getValue().getSubjectId());
                bundle.putInt("lecturerId", studentPerformanceViewModel.getSubjectInfo().getValue().getLecturerId());
                bundle.putInt("seminarianId", studentPerformanceViewModel.getSubjectInfo().getValue().getSeminarianId());
                bundle.putInt("semesterId", studentPerformanceViewModel.getSubjectInfo().getValue().getSemesterId());

                Navigation.findNavController(v).navigate(R.id.action_student_performance_to_dialog_student_lesson, bundle);
                return true;
            };

            studentPerformanceViewModel.getLecturesByModules().observe(getViewLifecycleOwner(),
                    lecturesByModules -> {
                        if (lecturesByModules == null) {
                            return;
                        }

                        LessonsAdapter lessonsAdapter = new LessonsAdapter(getContext(), modules,
                                studentPerformanceViewModel.getModuleInfo().getValue(),
                                studentPerformanceViewModel.getStudentPerformanceInModules().getValue(),
                                lecturesByModules, studentPerformanceViewModel.getStudentLessonsByModules().getValue());
                        expandableListView.setAdapter(lessonsAdapter);
                        expandableListView.setOnChildClickListener(onLectureClickListener);
                    });

            studentPerformanceViewModel.getStudentLessonsByModules().observe(getViewLifecycleOwner(),
                    studentLessonsByModules -> {
                        if (studentLessonsByModules == null) {
                            return;
                        }

                        LessonsAdapter lessonsAdapter = new LessonsAdapter(getContext(), modules,
                                studentPerformanceViewModel.getModuleInfo().getValue(),
                                studentPerformanceViewModel.getStudentPerformanceInModules().getValue(),
                                studentPerformanceViewModel.getLecturesByModules().getValue(), studentLessonsByModules);
                        expandableListView.setAdapter(lessonsAdapter);
                        expandableListView.setOnChildClickListener(onLectureClickListener);
                    });
        } else if (position == 3) {
            ExpandableListView.OnChildClickListener onSeminarClickListener = (parent, v, groupPosition, childPosition, id) -> {
                Bundle bundle = new Bundle();
                bundle.putBoolean("isFromGroupPerformance", false);
                bundle.putBoolean("isLecture", false);
                TextView bonusPoints = v.findViewById(R.id.bonusPoints);
                bundle.putBoolean("isHasData", !"Нет данных".equals(bonusPoints.getText().toString()));
                Lesson lesson = studentPerformanceViewModel.getSeminarsByModules().getValue().
                        get(modules.get(groupPosition)).get(childPosition);
                bundle.putString("lessonDate", lesson.getDateAndTime().getDate() + " " + lesson.getDateAndTime().getHours() +
                        ":" + lesson.getDateAndTime().getMinutes());
                bundle.putInt("lessonId", lesson.getId());
                bundle.putInt("studentId", studentPerformanceViewModel.getStudent().getValue().getId());
                bundle.putInt("moduleNumber", modules.get(groupPosition));
                bundle.putInt("groupId", studentPerformanceViewModel.getStudent().getValue().getGroupId());
                bundle.putInt("subjectId", studentPerformanceViewModel.getSubjectInfo().getValue().getSubjectId());
                bundle.putInt("lecturerId", studentPerformanceViewModel.getSubjectInfo().getValue().getLecturerId());
                bundle.putInt("seminarianId", studentPerformanceViewModel.getSubjectInfo().getValue().getSeminarianId());
                bundle.putInt("semesterId", studentPerformanceViewModel.getSubjectInfo().getValue().getSemesterId());

                Navigation.findNavController(v).navigate(R.id.action_student_performance_to_dialog_student_lesson, bundle);
                return true;
            };

            studentPerformanceViewModel.getSeminarsByModules().observe(getViewLifecycleOwner(),
                    seminarsByModules -> {
                        if (seminarsByModules == null) {
                            return;
                        }

                        LessonsAdapter lessonsAdapter = new LessonsAdapter(getContext(), modules,
                                studentPerformanceViewModel.getModuleInfo().getValue(),
                                studentPerformanceViewModel.getStudentPerformanceInModules().getValue(),
                                seminarsByModules, studentPerformanceViewModel.getStudentLessonsByModules().getValue());
                        expandableListView.setAdapter(lessonsAdapter);
                        expandableListView.setOnChildClickListener(onSeminarClickListener);
                    });

            studentPerformanceViewModel.getStudentLessonsByModules().observe(getViewLifecycleOwner(),
                    studentLessonsByModules -> {
                        if (studentLessonsByModules == null) {
                            return;
                        }

                        LessonsAdapter lessonsAdapter = new LessonsAdapter(getContext(), modules,
                                studentPerformanceViewModel.getModuleInfo().getValue(),
                                studentPerformanceViewModel.getStudentPerformanceInModules().getValue(),
                                studentPerformanceViewModel.getSeminarsByModules().getValue(), studentLessonsByModules);
                        expandableListView.setAdapter(lessonsAdapter);
                        expandableListView.setOnChildClickListener(onSeminarClickListener);
                    });
        }

        return root;
    }
}